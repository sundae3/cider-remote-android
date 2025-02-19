package com.example.ciderremotetest1.viewmodel
import android.content.Context
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.ciderremotetest1.R
import com.example.ciderremotetest1.model.NowPlayingJsonMaker
import com.example.ciderremotetest1.model.Playbackdata
import com.example.ciderremotetest1.model.QueueJsonMaker
import com.example.ciderremotetest1.repository.PlaybackRepository
import com.example.ciderremotetest1.repository.PreferencesRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import com.example.ciderremotetest1.repository.SocketRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay


class MainViewModel(private val socketRepository: SocketRepository,private val playbackRepository: PlaybackRepository,private val preferencesRepository: PreferencesRepository) : ViewModel() {

    val withUnknownKeys = Json { ignoreUnknownKeys = true }
    var obj = withUnknownKeys.decodeFromString<Playbackdata>("""{
    "type": "playbackStatus.playbackTimeDidChange",
    "data": {
        "currentPlaybackDuration": 171.383,
        "currentPlaybackTime": 131.95317599999998,
        "currentPlaybackTimeRemaining": 39.429824000000025,
        "isPlaying": true
    }
}""")

    var trackobj = withUnknownKeys.decodeFromString<NowPlayingJsonMaker>("""{
        "status": "ok",
        "info" : {
            "name": "no name",
            "artistName": "no name",
            "albumName": "no name",
            "inFavorites": false,
            "inLibrary": false,
            "shuffleMode": 0,
            "repeatMode": 0,
            "playParams" : {
              "id": "1696378002",
              "kind": "song"
            },
            "artwork": {
              "url": "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/a0/c3/3c/a0c33ce0-6ff8-1c5c-f600-219791fcf328/23UMGIM73153.rgb.jpg/512x512sr.jpg"
        }
    }
}""")

    var queueobj = withUnknownKeys.decodeFromString<QueueJsonMaker>("""{
        "id": "1727145710",
        "attributes": {
            "name": "Horizon",
            "albumName": "Mahal - EP",
            "artistName": "Glass Beams",
            "artwork": {
              "url": "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/be/b0/87/beb087ba-6ed2-0010-3eef-904a03921281/5054429192247.png/512x512bb.jpg"
            }
        }
}""")



    val trackProgress: State<Float> = socketRepository.trackProgress

    val isUserInteracting : State<Boolean> = socketRepository._isUserInteracting

    val volumeState: State<Float> = socketRepository.volumeState

    val shuffleState : State<Int> = socketRepository.shuffleMode

    val repeatState: State<Int> = socketRepository.repeatMode

    val isCurrentlyPlaying : State<Boolean> = socketRepository.isCurrentlyPlaying

    val isUserInteractingVolume: State<Boolean> = socketRepository._isUserInteractingVolume

    val socketMessage: State<Playbackdata> = socketRepository.socketMessage

    val topColors: State<List<Color>> = playbackRepository.topColors

    val imageBitmap: State<ImageBitmap> = playbackRepository.imageBitmap


    private val _nowPlayingData = mutableStateOf(trackobj)
    val nowPlayingData: State<NowPlayingJsonMaker> get() = _nowPlayingData

//    private val _userUrl = mutableStateOf("")
//    val userUrl: State<String> get() = _userUrl

    private val _baseUserUrl = mutableStateOf("") // Nullable state
    val baseUserUrl: State<String> get() = _baseUserUrl

    private val _urlState = mutableStateOf("")  // Initialized with empty string
    val urlState: State<String> get() = _urlState  // Expose it as a State<String>

    private val _isLoading = mutableStateOf(false) // To track loading state
    val isLoading: State<Boolean> get() = _isLoading

    private val _queueOg = mutableListOf(queueobj)
    val queueOg: List<QueueJsonMaker> get() = _queueOg

    private val _queue = MutableLiveData<List<QueueJsonMaker>>(listOf(queueobj)) // or use StateFlow
    val queue: LiveData<List<QueueJsonMaker>> = _queue

//    val loader = ImageLoader(this)





    // Function to update the order of the items
    fun reorderItems(fromIndex: Int, toIndex: Int) {
        val currentQueue = _queue.value?.toMutableList() ?: return
        // Reorder the list
        println("From index: ${fromIndex}")
        println("From index: ${toIndex}")
//        println(toIndex)
        val item = currentQueue.removeAt(fromIndex)
        currentQueue.add(toIndex, item)
        _queue.value = currentQueue // Trigger UI update
//        println("reordering")
    }

    fun removeItemFromQueue(itemId:String) {
//        val queueStor = _queue.value
        val index = _queue.value?.indexOfFirst { it.id == itemId }
        _queue.value = _queue.value?.filterNot { it.id == itemId }
        viewModelScope.launch {
            val data = playbackRepository.makePostRequestWithBody(_baseUserUrl.value+"/api/v1/playback/queue/remove-by-index", """{"index": ${index?.plus(
                1
            )}}""")
            println(itemId+1)
            if(data == null) {
                _queue.value = _queueOg
            }
        }
    }

    fun queueClickHandler(itemIndex: Int) {
        viewModelScope.launch {
            val data = playbackRepository.makePostRequestWithBody(_baseUserUrl.value+"/api/v1/playback/queue/change-to-index", """{"index": ${itemIndex+1}}""")
            println(itemIndex)
        }

    }


    init {
        // Load the URL when the ViewModel is created
        loadUrl()
        observeSocketState()
    }


    // Save the user input URL
    fun saveUserUrl(url: String) {
        viewModelScope.launch {
            preferencesRepository.saveUserUrl(url)
            loadUrlSocket(socketRepository)
        }
    }

    fun makePostUrl(url:String) {
        viewModelScope.launch {
            val postResponse  = playbackRepository.makePostRequest(_baseUserUrl.value+url)
            if (postResponse != "Error" && postResponse != "Error: No Response Body") {
//                if(url == "/api/v1/playback/playpause") {
////                    socketRepository.updatePlayPause(!isCurrentlyPlaying.value)
//                }
                try {
                    val data =
                        playbackRepository.fetchNowPlayingData(_baseUserUrl.value)
                    if (data != null) {
                        _nowPlayingData.value = data
                        if(url == "/api/v1/playback/add-to-library") {
                            _nowPlayingData.value.info.inLibrary = true
                        }
                        println(_nowPlayingData)
                    } // Update the LiveData with the result
                } catch (e: Exception) {
                    // Handle error (e.g., show error message)
                    println(e.message)
                }
            }
        }
    }


    // Load the URL from DataStore
    fun loadUrlSocket(socketRepository:SocketRepository) {
        viewModelScope.launch {
            preferencesRepository.getUserUrl.collect { url ->
                _baseUserUrl.value = url
//                println(_userUrl.value)
                if(url != "") {
                    socketRepository.setupSocketListeners(_baseUserUrl.value)
                }
            }
        }
    }

    private fun observeSocketState() {
    viewModelScope.launch {
        socketRepository.socketState
            .collect { state ->
                // Handle each state differently
                when (state) {
                    "playbackStatus.nowPlayingItemDidChange" -> {
                        val data = playbackRepository.fetchNowPlayingData(_baseUserUrl.value)
                        val queue =  playbackRepository.fetchCurrentQueue(baseUserUrl.value)

                        queue?.let {

                                val modifiedQueue = it.toMutableList().apply {
                                    removeAt(0) // Remove the first element
                                }

                                _queueOg.clear()
                                _queueOg.addAll(modifiedQueue)
                                _queue.value = modifiedQueue // Set the value to the new list
                            }

                        if (data != null) {
                            _nowPlayingData.value = data
                            socketRepository.updateShuffleState(data.info.shuffleMode)
                            socketRepository.updateRepeatState(data.info.repeatMode)

                            println(_nowPlayingData)
                        }
                    }

                    else -> {
                        // Optionally handle unknown states or log them
                        println("Unknown state: $state")
                    }
                }
            }
    }
    }


    fun loadUrl() {
        viewModelScope.launch {
            preferencesRepository.getUserUrl.collect { url ->
//                _userUrl.value = url
                _baseUserUrl.value = url
//                println(_queue[0])
                if(_baseUserUrl.value != "") {

                    try {
                        socketRepository.setupSocketListeners(_baseUserUrl.value)
                        val queue =  playbackRepository.fetchCurrentQueue(baseUserUrl.value)
//                    _queue.clear()
                        if(queue?.isEmpty() == false) {
                            queue?.let {

                                val modifiedQueue = it.toMutableList().apply {
                                    removeAt(0) // Remove the first element
                                }
                                _queueOg.clear()
                                _queueOg.addAll(modifiedQueue)
                                _queue.value = modifiedQueue // Set the value to the new list
                            }
                        }

                    } catch (e:Exception) {
                        println(e.message)
                    }


                    println(_queue)
                    try {
                        val data =
                            playbackRepository.fetchNowPlayingData(_baseUserUrl.value)
                        if (data != null) {
                            _nowPlayingData.value = data
                            socketRepository.updateShuffleState(data.info.shuffleMode)
                            socketRepository.updateRepeatState(data.info.repeatMode)
                        } // Update the LiveData with the result
                    } catch (e: Exception) {
                        // Handle error (e.g., show error message)
                        println(e.message)
                    }
                    try {
                        val status =
                            playbackRepository.isCurrentlyPlaying(_baseUserUrl.value)
                        if (status != null) {
                            socketRepository.updatePlayPause(status.is_playing)
                        } // Update the LiveData with the result
                    } catch (e: Exception) {
                        // Handle error (e.g., show error message)
                        println(e.message)
                    }
                    try {
                        val state = playbackRepository.volumeStateFetcher(_baseUserUrl.value)
                        if(state != null) {
                            println(state.volume)
                            socketRepository.volumeStateChanger(state.volume)
                        }

                    }  catch (e: Exception) {
                        // Handle error (e.g., show error message)
                        println(e.message)
                    }
                }
//                println(_baseUserUrl)
            }
        }
    }


    fun userIntearctionMeditor(status: Boolean) {
        socketRepository.userInteractionChanger(status)
    }

    fun trackProgressMediator(sliderValue: Float) {
        socketRepository.trackProgressChanger(sliderValue)
    }


    fun onSliderValueChangeFinished(sliderValue: Float) {
        userIntearctionMeditor(false) // User has finished interacting
        // Send the updated value to the server
        viewModelScope.launch {

            socketRepository.interactionFlagChanger(true)
            val posinsec = sliderValue*socketMessage.value.data.currentPlaybackDuration.toInt()
            val body = """{"position": ${posinsec}}"""
            var res = playbackRepository.makePostRequestWithBody(_baseUserUrl.value+"/api/v1/playback/seek", body)
            println(sliderValue)
            delay(1000)
            socketRepository.interactionFlagChanger(false)
        }
    }

    fun userIntearctionMediatorVolume(status: Boolean) {
        socketRepository.userInteractionChangerVolume(status)
    }
    fun volumeStateMediator(sliderValue: Float) {
        socketRepository.volumeStateChanger(sliderValue)
    }

    fun onVolumeSliderValueChangeFinishedVolume(sliderValue: Float) {
        userIntearctionMeditor(false) // User has finished interacting
        // Send the updated value to the server
        viewModelScope.launch {

//            socketRepository.interactionFlagChanger(true)
            val posinsec = sliderValue
            val body = """{"volume": ${posinsec}}"""
            var res = playbackRepository.makePostRequestWithBody(_baseUserUrl.value+"/api/v1/playback/volume", body)
            println(sliderValue)
            delay(1000)
//            socketRepository.interactionFlagChanger(false)
        }
    }


    fun findMovedItem(id:String) {
        // Assuming one item is moved
        viewModelScope.launch {
            var og_index = -1
            var changed_index = -1
            for ((index, item) in _queueOg.withIndex()) {
                if (item.id == id ) {
                    og_index = index+1
                }
            }
            for ((index2, item) in _queue.value?.withIndex()!!) {
                if (item.id == id ) {
                    changed_index = index2+1
                }
            }
            if(og_index !=-1 && changed_index != -1) {
               val data = playbackRepository.makePostRequestWithBody(_baseUserUrl.value+"/api/v1/playback/queue/move-to-position", """{"startIndex": ${og_index}, "destinationIndex": ${changed_index}}""")
                if(data != null) {
                    _queueOg.clear()
                    _queueOg.addAll(_queue.value!!)
                }
                println("""{"startIndex": ${og_index}, "destinationIndex": ${changed_index}} """)
            }
        }
    }

    fun trimText(text: String, maxLength: Int): String {
        return if (text.length > maxLength) {
            text.take(maxLength) + "..." // Add ellipsis if the text exceeds the limit
        } else {
            text
        }
    }



    @Composable
    fun ImageLoader(imgUrl: String) {
        AsyncImage(
            model = imgUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clip(RoundedCornerShape(12.dp)),
            placeholder = painterResource(id = R.drawable.placeholder_transparent),

            )
    }

    @Composable
    fun QueueImageLoader(imgUrl: String) {
        AsyncImage(
            model = imgUrl,
            contentDescription = null,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .size(50.dp)
            ,
            placeholder = painterResource(id = R.drawable.placeholder_transparent)

        )
    }

    override fun onCleared() {
        super.onCleared()
        // Disconnect socket through the repository
        viewModelScope.launch {
            socketRepository.disconnectSocket()
        }
    }
}