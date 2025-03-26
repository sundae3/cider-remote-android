package com.example.ciderremotetest1.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
//@OptIn(ExperimentalSerializationApi::class)
@OptIn(ExperimentalSerializationApi::class)

@Serializable
@JsonIgnoreUnknownKeys
data class Playbackdata(
    @EncodeDefault
    val type : String = "playbackStatus.playbackTimeDidChange",
    val data: PlaybackStatus
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class PlaybackStatus(
    @EncodeDefault
    val currentPlaybackDuration: Float = 0f,
    val currentPlaybackTime: Float = 0f,
    val currentPlaybackTimeRemaining: Float = 0f,
    val isPlaying: Boolean = false
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class Checker(
    val type: String
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class NowPlayingJsonMaker(
    val status: String,
    val info: Trackinfo
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class Trackinfo(
    @EncodeDefault
    val name: String = "No name",
    val artistName: String = "Unknown",
    val albumName: String = "Not Found",
    @EncodeDefault
    val artwork: Albumart = Albumart(), // Provide default instance
    var inLibrary: Boolean,
    val inFavorites: Boolean = false,
    var shuffleMode: Int = 0,
    var repeatMode: Int = 0,
    val playParams: PlayParams,
    @EncodeDefault
    val currentPlaybackTime: Float = 0f,
    val remainingTime: Float = 0f
)
@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class PlayParams(
    val id: String,
    val kind: String
)


@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class Albumart(
    @EncodeDefault
    val url: String = ""
)


@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class isCurrentlyplayingJsonMaker (
    val status: String,
    val is_playing: Boolean
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class volumeStateJsonMaker (
    val status: String,
    val volume: Float
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class volumeSocketStateJsonMaker (
    val type: String,
    val data: Float
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class shuffleAndRepeatJsonMaker (
    val type: String,
    val data: Int
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class currentplayBackStatusChecker (
    val type: String,
    val data: Status
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class Status(
    val state: String
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
@Immutable //its for scroll smooth testing
data class QueueJsonMaker(
    val id: String,
    val attributes : trackdata
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
@Immutable //its for scroll smooth testing
data class trackdata(
    @EncodeDefault
    val albumName: String = "Unknown",
    @EncodeDefault
    val artwork: Albumart = Albumart(), // Provide default instance
    val name: String = "No name",
    val artistName: String = "Not Found",
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class CiderInstanceData(
    @EncodeDefault
    val id: String = "",
    val deviceName: String = "",
    val url: String = "",
    val method: String = "",
    val token: String = ""
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class QRScanJson(
    @EncodeDefault
    val address: String,
    val token: String,
    val method: String,
    val os: String = "win"
)
