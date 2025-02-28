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
    suspend fun fetchNowPlayingData(url:String, token:String):NowPlayingJsonMaker?
    suspend fun fetchCurrentQueue(url: String, token:String): List<QueueJsonMaker>?
    suspend fun isCurrentlyPlaying(url: String, token:String): isCurrentlyplayingJsonMaker?
    suspend fun volumeStateFetcher(url: String, token:String): volumeStateJsonMaker?
    suspend fun makePostRequest(url: String, token:String):String
    suspend fun makePostRequestWithBody(url: String, token:String, body:String): String

    val topColors: State<List<Color>>
    val imageBitmap: State<ImageBitmap>

    fun cancelScope()

//    fun nowPlayingObjectSupplier(obj: NowPlayingJsonMaker,url:String):NowPlayingJsonMaker
}
