package com.example.ciderremotetest1.repository


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.ciderremotetest1.model.Checker
import com.example.ciderremotetest1.model.Playbackdata
import com.example.ciderremotetest1.model.currentplayBackStatusChecker
import com.example.ciderremotetest1.model.shuffleAndRepeatJsonMaker
import com.example.ciderremotetest1.model.volumeSocketStateJsonMaker
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import androidx.compose.runtime.State

class SocketRepository {

    // Using MutableState to hold the socket message

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
    private val _socketMessage = mutableStateOf<Playbackdata>(obj)
    val socketMessage: MutableState<Playbackdata> get() = _socketMessage

    private val _socketState: MutableStateFlow<String> = MutableStateFlow("")
    val socketState: StateFlow<String> = _socketState  // Expose StateFlow to ViewModel

    private val _trackProgress = mutableStateOf(0f)
    val trackProgress: MutableState<Float> get() = _trackProgress

    private val _volumeState = mutableStateOf(0f)
    val volumeState: MutableState<Float> get() = _volumeState

    private val _shuffleMode = mutableStateOf(0)
    val shuffleMode: State<Int> = _shuffleMode

    private val _isCurrentlyPlaying = mutableStateOf(false) // To track loading state
    val isCurrentlyPlaying: State<Boolean> = _isCurrentlyPlaying

    private val _repeatMode = mutableStateOf(0)
    val repeatMode: State<Int> = _repeatMode

    var interactionFlagged = mutableStateOf(false)
    var _isUserInteracting = mutableStateOf(false)

    var _isUserInteractingVolume = mutableStateOf(false)

    //    val socketMessage: State<Playbackdata?> get() = _socketMessage

    // Other repository methods...
//    private val socket = IO.socket("") // Replace with your actual socket server URL
    private lateinit var socket: Socket  // Declare socket without initialization here
//    init {
//        setupSocketListeners(url)
//    }
     fun interactionFlagChanger(flag: Boolean) {
        interactionFlagged.value = flag
    }

    fun userInteractionChanger(status: Boolean) {

        _isUserInteracting.value=status
        println(status)
    }

    fun trackProgressChanger(sliderValue: Float) {
        _trackProgress.value = sliderValue
    }

    fun userInteractionChangerVolume(status: Boolean) {
        _isUserInteractingVolume.value = status
    }

    fun volumeStateChanger(sliderValue: Float) {
        _volumeState.value = sliderValue
    }

    // Setting up socket listeners
    fun setupSocketListeners(url: String) {
        socket = IO.socket(url)  // Initialize socket with the provided URL

        socket.on("API:Playback") { args ->
            // Parse the incoming data from socket into Playbackdata
            val UnknownKeys = Json { ignoreUnknownKeys = true }
            val kk  = UnknownKeys.decodeFromString<Checker>(args[0].toString())
            if (kk.type == "playbackStatus.playbackTimeDidChange") {
                _socketState.value = "playbackStatus.playbackTimeDidChange"
                val tem = UnknownKeys.decodeFromString<Playbackdata>(args[0].toString())
                if (tem.data.currentPlaybackTime != 0f) {
                    obj = UnknownKeys.decodeFromString<Playbackdata>(args[0].toString())
                    _socketMessage.value = obj
                    var ela_num = obj.data.currentPlaybackTime
                    var totalnum =obj.data.currentPlaybackDuration
                    var prog = (ela_num / totalnum)
                    if (!_isUserInteracting.value) {
                        if(!interactionFlagged.value) {
                            _trackProgress.value = prog

                            println(_trackProgress.value)
//                            _currentTimeString.value = "${min}:${sec}"

                        }
                        // If no user interaction, update progress normally
                    }
                    if(!_isUserInteractingVolume.value) {
                        _volumeState.value
                    }
//                    println(_socketMessage.value)
                }
            }
            else if(kk.type == "playbackStatus.nowPlayingItemDidChange") {
                _socketState.value = "playbackStatus.nowPlayingItemDidChange"
            } else if(kk.type == "playbackStatus.playbackStateDidChange") {
                val po = UnknownKeys.decodeFromString<currentplayBackStatusChecker>(args[0].toString())
                println(po)
                if(po.data.state == "paused") {
//                    _isPlayingNotif.value = false
//                    updatePlaybackState(false)
                    updatePlayPause(false)
                } else if (po.data.state == "playing") {
//                    _isPlayingNotif.value = true
//                    updatePlaybackState(true)
                    updatePlayPause(true)
                }
//                _isPlayingNotif.value = isPlayingNotif.value
            } else if(kk.type == "playerStatus.volumeDidChange") {
                _socketState.value = "playerStatus.volumeDidChange"
                val ggp = UnknownKeys.decodeFromString<volumeSocketStateJsonMaker>(args[0].toString())
                _volumeState.value = ggp.data
            } else if (kk.type == "playerStatus.shuffleModeDidChange") {
                val okp = UnknownKeys.decodeFromString<shuffleAndRepeatJsonMaker>(args[0].toString())
                _socketState.value = "playerStatus.shuffleModeDidChange"
                _shuffleMode.value = okp.data
//                updateShuffleState(okp.data)
            }
            else if (kk.type == "playerStatus.repeatModeDidChange") {
                val okt = UnknownKeys.decodeFromString<shuffleAndRepeatJsonMaker>(args[0].toString())
                _socketState.value = "playerStatus.repeatModeDidChange"
                _repeatMode.value = okt.data
            }
        }

        socket.connect() // Connect to the socket server
    }


    fun updateShuffleState(isPlaying: Int) {
        _shuffleMode.value = isPlaying
    }

    fun updateRepeatState(isPlaying: Int) {
        _repeatMode.value = isPlaying
    }

    fun updatePlayPause(isPlaying: Boolean) {
        _isCurrentlyPlaying.value = isPlaying
    }

    // Disconnect socket when it's no longer needed
    suspend fun disconnectSocket() {
        withContext(Dispatchers.IO) {
            if (::socket.isInitialized) {
                socket.disconnect()
            }
        }
    }
}


