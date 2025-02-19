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
    val name: String,
    val artistName: String,
    val albumName: String,
    val artwork: Albumart,
    var inLibrary: Boolean,
    val inFavorites: Boolean,
    var shuffleMode: Int,
    var repeatMode: Int,
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
    val url: String
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
    val albumName: String,
    val artwork: Albumart,
    val name: String,
    val artistName: String,
)
