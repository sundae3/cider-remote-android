package com.example.ciderremotetest1.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.palette.graphics.Palette
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.toBitmap
import com.example.ciderremotetest1.R
import com.example.ciderremotetest1.model.NowPlayingJsonMaker
import com.example.ciderremotetest1.model.QueueJsonMaker
import com.example.ciderremotetest1.model.isCurrentlyplayingJsonMaker
import com.example.ciderremotetest1.model.volumeStateJsonMaker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.lang.Exception

class PlaybackRepositoryImpl(private val context: Context) : PlaybackRepository {
    private val client = OkHttpClient()

    val sakiColorPalette = listOf(
        Color(0xFFFF3456),  // Bright pink/red (background and hair)
        Color(0xFFFFBBCC),  // Light pink (skin tone)
        Color(0xFF8A2BE2),  // Purple (eyes and ear details)
        Color(0xFF221133),  // Dark purple/black (outline and dark elements)
        Color(0xFFFFFFFF)   // White (hair accessories and highlights)
    )

    private val _topColors =  mutableStateOf<List<Color>>(sakiColorPalette)
    override val topColors: State<List<Color>> = _topColors


    fun createDummyImageBitmap(): ImageBitmap {
        // Create a 1x1 Bitmap
        val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        // Set the pixel to a color (e.g., white)
        bitmap.setPixel(0, 0, Color(0xFFcf164f).toArgb())

        // Convert the Bitmap to ImageBitmap (Compose-compatible)
        return bitmap.asImageBitmap()
    }

    // Now make _imageBitmap non-nullable
    private val _imageBitmap = mutableStateOf(createDummyImageBitmap())

    // This can be accessed using `imageBitmap` (which is immutable)
    override val imageBitmap: State<ImageBitmap> = _imageBitmap


    fun loadImageAndExtractColors(
        context: Context,
        url: String,
        width: Int,
        height: Int
    ) {
        val request = ImageRequest.Builder(context)
            .data(url)
            .target(
                onStart = {
                    // Optionally handle image loading start
                },
                onSuccess = { result ->
                    val bitmap = result.toBitmap()
                    _imageBitmap.value = bitmap.asImageBitmap()

                    // Extract 6 colors using enhanced palette generation
                    val safeBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                    val palette = Palette.Builder(safeBitmap)
                        .maximumColorCount(32)  // Increased for more color options
                        .addFilter { _, hsl ->
                            // Filter out extreme light/dark colors
                            hsl[2] in 0.1f..0.9f
                        }
                        .generate()

                    // Get colors by population and profile
                    val populationColors = palette.swatches
                        .sortedByDescending { it.population }
                        .distinctBy { swatch ->
                            Triple(
                                (swatch.rgb and 0xFF0000) shr 16,
                                (swatch.rgb and 0x00FF00) shr 8,
                                swatch.rgb and 0x0000FF
                            )
                        }
                        .take(3)
                        .map { Color(it.rgb) }

                    val profileColors = listOfNotNull(
                        palette.vibrantSwatch?.rgb?.let { Color(it) },
                        palette.lightVibrantSwatch?.rgb?.let { Color(it) },
                        palette.darkVibrantSwatch?.rgb?.let { Color(it) },
                        palette.mutedSwatch?.rgb?.let { Color(it) },
                        palette.lightMutedSwatch?.rgb?.let { Color(it) },
                        palette.darkMutedSwatch?.rgb?.let { Color(it) }
                    ).distinct()

                    // Combine and ensure 6 unique colors
                    val extractedColors = (populationColors + profileColors)
                        .distinct()
                        .take(6)

                    // Ensure we have 6 colors with fallbacks if needed
                    val finalColors = when {
                        extractedColors.isEmpty() -> List(6) { Color.Gray }
                        extractedColors.size < 6 -> {
                            val defaultColors = listOf(
                                Color.LightGray,
                                Color.Gray,
                                Color.DarkGray,
                                Color.White,
                                Color.Black,
                                Color.Gray
                            )
                            (extractedColors + defaultColors).distinct().take(6)
                        }
                        else -> extractedColors
                    }

                    _topColors.value = finalColors
//                    println("Extracted colors: ${finalColors.size}")
                    println(_topColors.value)
                },
                onError = { error ->
                    // Handle error, maybe set default colors
//                    _topColors.value = List(6) { Color.Gray }
                }
            )
            .build()

        val imageLoader = ImageLoader(context)
        imageLoader.enqueue(request)
    }


    // Remove the custom CoroutineScope for better management of suspend functions
    override suspend fun fetchNowPlayingData(url: String, token: String): NowPlayingJsonMaker? {
        println(url)
        return withContext(Dispatchers.IO) {  // Ensure the network request runs on the IO thread
            val request = Request.Builder()
                .url(url + "/api/v1/playback/now-playing")
                .header("apptoken", token)  // Add the Authorization header
                .build()

            try {
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: ""
                    val UnknownKeys = Json { ignoreUnknownKeys = true }
                    val po = UnknownKeys.decodeFromString<NowPlayingJsonMaker>(responseBody)

                    if (po.status == "ok") {
                        var fixedArtworkUrl = po.info.artwork.url.replace("{w}x{h}", "512x512")
                        loadImageAndExtractColors(context,fixedArtworkUrl, 512, 512)
                        po  // Return the parsed NowPlayingJsonMaker object
                    } else {
                        // Log if the status isn't "ok" and return null
                        Log.e("PlaybackRepository", "Error: Invalid status ${po.status}")
                        null
                    }
                } else {
                    // Log the error response code and throw an exception
                    Log.e(
                        "PlaybackRepository",
                        "Error: Network request failed with code ${response.code}"
                    )
                    throw Exception("Failed to fetch now playing data, response code: ${response.code}")
                }
            } catch (e: Exception) {
                // Log the exception details and throw a new exception to propagate the error
                Log.e("PlaybackRepository", "Exception occurred: ${e.message}")
                throw Exception("Failed to fetch now playing data", e)
            }
        }
    }

    override suspend fun fetchCurrentQueue(url: String, token: String): List<QueueJsonMaker>? {
        println(url)
        return withContext(Dispatchers.IO) {  // Ensure the network request runs on the IO thread
            val request = Request.Builder()
                .url(url + "/api/v1/playback/queue")
                .header("apptoken", token)  // Add the Authorization header
                .build()

            try {
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: ""
                    val UnknownKeys = Json { ignoreUnknownKeys = true }

//                    val po = UnknownKeys.decodeFromString<QueueJsonMaker>(responseBody)

                    if (responseBody != "") {
                       responseBody.let {
                            // Parse JSON response using kotlinx.serialization
                            UnknownKeys.decodeFromString<List<QueueJsonMaker>>(it)
                        }
                         // Return the parsed NowPlayingJsonMaker object
                    } else {
                        // Log if the status isn't "ok" and return null
                        Log.e("PlaybackRepository", "Error: Invalid status")
                        null
                    }
                } else {
                    // Log the error response code and throw an exception
                    Log.e(
                        "PlaybackRepository",
                        "Error: Network request failed with code ${response.code}"
                    )
                    throw Exception("Failed to fetch now playing data, response code: ${response.code}")
                }
            } catch (e: Exception) {
                // Log the exception details and throw a new exception to propagate the error
                Log.e("PlaybackRepository", "Exception occurred: ${e.message}")
                throw Exception("Failed to fetch now playing data", e)
            }
        }
    }

    override suspend fun isCurrentlyPlaying(url: String, token: String): isCurrentlyplayingJsonMaker? {
        return withContext(Dispatchers.IO) {  // Ensure the network request runs on the IO thread
            val request = Request.Builder()
                .url(url + "/api/v1/playback/is-playing")
                .header("apptoken", token)  // Add the Authorization header
                .build()

            try {
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: ""
                    val UnknownKeys = Json { ignoreUnknownKeys = true }
                    val po = UnknownKeys.decodeFromString<isCurrentlyplayingJsonMaker>(responseBody)

                    if (po.status == "ok") {
                        po  // Return the parsed NowPlayingJsonMaker object
                    } else {
                        // Log if the status isn't "ok" and return null
                        Log.e("PlaybackRepository", "Error: Invalid status ${po.status}")
                        null
                    }
                } else {
                    // Log the error response code and throw an exception
                    Log.e(
                        "PlaybackRepository",
                        "Error: Network request failed with code ${response.code}"
                    )
                    throw Exception("Failed to fetch now playing data, response code: ${response.code}")
                }
            } catch (e: Exception) {
                // Log the exception details and throw a new exception to propagate the error
                Log.e("PlaybackRepository", "Exception occurred: ${e.message}")
                throw Exception("Failed to fetch now playing data", e)
            }
        }
    }

    override suspend fun volumeStateFetcher(url: String, token: String): volumeStateJsonMaker? {
        return withContext(Dispatchers.IO) {  // Ensure the network request runs on the IO thread
            val request = Request.Builder()
                .url(url + "/api/v1/playback/volume")
                .header("apptoken", token)  // Add the Authorization header
                .build()

            try {
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: ""
                    val UnknownKeys = Json { ignoreUnknownKeys = true }
                    val po = UnknownKeys.decodeFromString<volumeStateJsonMaker>(responseBody)

                    if (po.status == "ok") {
                        po  // Return the parsed NowPlayingJsonMaker object
                    } else {
                        // Log if the status isn't "ok" and return null
                        Log.e("PlaybackRepository", "Error: Invalid status ${po.status}")
                        null
                    }
                } else {
                    // Log the error response code and throw an exception
                    Log.e(
                        "PlaybackRepository",
                        "Error: Network request failed with code ${response.code}"
                    )
                    throw Exception("Failed to fetch now playing data, response code: ${response.code}")
                }
            } catch (e: Exception) {
                // Log the exception details and throw a new exception to propagate the error
                Log.e("PlaybackRepository", "Exception occurred: ${e.message}")
                throw Exception("Failed to fetch now playing data", e)
            }
        }
    }

    override suspend fun makePostRequest(url: String, token: String): String {
        return withContext(Dispatchers.IO) {  // Dispatching to IO thread
            // JSON body for the POST request
            val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val jsonBody = """
                {
                   
                }
            """.trimIndent()

            val body = jsonBody.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url(url) // Use the provided URL for the POST request
                .header("apptoken", token)  // Add the Authorization header
                .post(body)  // Set the POST method with the body
                .build()

            try {
                val response = client.newCall(request).execute()

                // Handle the response
                return@withContext if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: "No Response Body"
                    Log.d("HttpRequest", "POST Response: $responseBody")
                    responseBody // Return the response body as a string
                } else {
                    Log.e("HttpRequest", "POST Request failed with code: ${response.code}")
                    "Error: ${response.code}" // Return error code as string
                }
            } catch (e: Exception) {
                // Catching any exception that may happen during the request
                Log.e("HttpRequest", "Error during POST request: ${e.message}")
                return@withContext "Error: ${e.message}" // Return error message as string
            }
        }
    }

    override suspend fun makePostRequestWithBody(url: String, token: String, body:String): String {
        return withContext(Dispatchers.IO) {  // Dispatching to IO thread
            // JSON body for the POST request
            val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val jsonBody = body.trimIndent()

            val body = jsonBody.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url(url) // Use the provided URL for the POST request
                .header("apptoken", token)  // Add the Authorization header
                .post(body)  // Set the POST method with the body
                .build()

            try {
                val response = client.newCall(request).execute()

                // Handle the response
                return@withContext if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: "No Response Body"
                    Log.d("HttpRequest", "POST Response: $responseBody")
                    responseBody // Return the response body as a string
                } else {
                    Log.e("HttpRequest", "POST Request failed with code: ${response.code}")
                    "Error: ${response.code}" // Return error code as string
                }
            } catch (e: Exception) {
                // Catching any exception that may happen during the request
                Log.e("HttpRequest", "Error during POST request: ${e.message}")
                return@withContext "Error: ${e.message}" // Return error message as string
            }
        }
    }

    override fun cancelScope() {
        // cancelScope is not necessary in this case since we are not using a custom CoroutineScope
    }
}
