package com.example.ciderremotetest1.repository
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.example.ciderremotetest1.model.NowPlayingJsonMaker
import com.example.ciderremotetest1.model.QueueJsonMaker
import com.example.ciderremotetest1.model.isCurrentlyplayingJsonMaker
import com.example.ciderremotetest1.model.volumeStateJsonMaker

interface PlaybackRepository {

    // Fetch the current now-playing data (HTTP)
    suspend fun fetchNowPlayingData(url:String):NowPlayingJsonMaker?
    suspend fun fetchCurrentQueue(url: String): List<QueueJsonMaker>?
    suspend fun isCurrentlyPlaying(url: String): isCurrentlyplayingJsonMaker?
    suspend fun volumeStateFetcher(url: String): volumeStateJsonMaker?
    suspend fun makePostRequest(url: String):String
    suspend fun makePostRequestWithBody(url: String, body:String): String

    val topColors: State<List<Color>>
    val imageBitmap: State<ImageBitmap>

    fun cancelScope()

//    fun nowPlayingObjectSupplier(obj: NowPlayingJsonMaker,url:String):NowPlayingJsonMaker
}
