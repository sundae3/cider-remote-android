package com.example.ciderremotetest1.ui.components.screens.screen2

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
//import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
//import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ciderremotetest1.R
import com.example.ciderremotetest1.viewmodel.MainViewModel

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import android.net.Uri
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink

//@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen2(mainViewModel: MainViewModel) {
    var nowPlaying = mainViewModel.nowPlayingData.value.info
    var isConnected = mainViewModel.nowPlayingData.value.status
    val timeProgress2 by remember { mainViewModel.trackProgress }
    var timeProgress3 = "${timeProgress2.toInt()/60}:${timeProgress2.toInt()%60}"

    var total_num = nowPlaying.remainingTime+nowPlaying.currentPlaybackTime
    val base_value = mainViewModel.trackProgress.value*total_num
//                            var timeProgress3 = "${prog.toInt()/60}:${prog.toInt()%60}"
    val sec = if (base_value.toInt()%60 <10) "0${base_value.toInt()%60}" else base_value.toInt()%60
    val min = base_value.toInt()/60
    val sec_for_total  = if (total_num.toInt()%60 <10) "0${total_num.toInt()%60}" else total_num.toInt()%60
    val total_time_string = "${total_num.toInt()/60}:${sec_for_total}"

    var isInLibrary = mainViewModel.nowPlayingData.value.info.inLibrary
    var isInLibraryIcon = R.drawable.check_icon
    if(!isInLibrary) {
       isInLibraryIcon = R.drawable.plus_new_icon
    } else if(isInLibrary) {
        isInLibraryIcon = R.drawable.check_icon
    }
    var shuffleMode = mainViewModel.shuffleState.value
    var repeatMode = mainViewModel.repeatState.value
    var userUrl = mainViewModel.baseUserUrl.value
    var fixedArtworkUrl = nowPlaying.artwork.url.replace("{w}x{h}", "512x512")
    val playPauseUrl = "/api/v1/playback/playpause"
    val nextUrl = "/api/v1/playback/next"
    val previousUrl = "/api/v1/playback/previous"
    val addToLibraryUrl = "/api/v1/playback/add-to-library"
    val shuffleUrl = "/api/v1/playback/toggle-shuffle"
    val repeatUrl = "/api/v1/playback/toggle-repeat"
    val isUserInteracting = mainViewModel.isUserInteracting
    val isUserInteractingVolume = mainViewModel.isUserInteractingVolume
    val sliderValue by remember { mainViewModel.trackProgress }
    val sliderValueVolume by remember { mainViewModel.volumeState }
    var sliderValue2 by remember { androidx.compose.runtime.mutableStateOf(0.5f) }
    var isCurrentlyPlayin = mainViewModel.isCurrentlyPlaying
//    val pause_icon = R.drawable.pause_icon
//    val play_icon2 = R.drawable.play3_icon
    val play_icon = R.drawable.play_svgrepo_2
    var middle_button_icon = R.drawable.pause_icon2
    var shuffle_tint = Color.LightGray
    var repeat_tint = Color.LightGray
    var repeatIcon = R.drawable.repeat
    val mainbuttons_tint = MaterialTheme.colorScheme.background
    if(repeatMode == 1) {
        repeatIcon = R.drawable.repeat_once
        repeat_tint = Color.White
    } else if (repeatMode == 2) {
        repeat_tint = Color.White
    }
    if(shuffleMode == 1) {
        shuffle_tint = Color.White
    }
    if(!isCurrentlyPlayin.value) {
        middle_button_icon = play_icon
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val iconSize = remember(screenWidth) {
        minOf(screenWidth / 6, 100.dp) // Maximum size of 48.dp
    }

    val characterLimit = remember(screenWidth) {
        when {
            screenWidth < 360.dp -> 20 // Small phones
            screenWidth < 600.dp -> 30 // Normal phones
            screenWidth < 840.dp -> 40 // Large phones/Small tablets
            else -> 50 // Tablets and larger
        }
    }

    val relativeSizeInDpWidth5dp = (1f/ 100f) * screenWidth.value.dp

    if(userUrl != "" && isConnected !="") {

    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        mainViewModel.ImageLoader(fixedArtworkUrl)

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(start=15.dp, end=15.dp, top = 15.dp),) {
            Column {
                Text(
                    mainViewModel.trimText(nowPlaying.name, characterLimit),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(),
                    color = mainbuttons_tint
                )
                Text(
                    mainViewModel.trimText(nowPlaying.artistName, characterLimit),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top =relativeSizeInDpWidth5dp),
                    color = mainbuttons_tint
                )
            }
            Column() {
                    Icon(
                        painter = painterResource(id = isInLibraryIcon), // Your vector drawable XML resource
                        contentDescription = "Example Icon",
                        modifier = Modifier
                            .clickable(onClick = { mainViewModel.makePostUrl(addToLibraryUrl) })
                            .padding()
                            .size(30.dp),
//                            .fillMaxHeight(), // Icon size
                        tint = Color.White
                    )
//                }
            }
        }


        Slider(
            value = mainViewModel.trackProgress.value,
            modifier = Modifier.height(10.dp).padding(start = 10.dp, top = 30.dp, end =10.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color.White, // Remove thumb by setting it to transparent
                activeTrackColor = Color.White, // Customize the active part of the track
                inactiveTrackColor = MaterialTheme.colorScheme.surface, // Customize the inactive part of the track color is basically gray
            ),
            thumb = { CustomSliderThumb() },
            track = { positions ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    // Inactive (background) track
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xffc5c5c5))
                    )
                    // Active track
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(positions.value) // Use selectionFractions instead of activeRange
                            .fillMaxHeight()
                            .background(Color.White)
                    )
                }
            },

            onValueChange = { value:Float  ->
                // Check if the user is interacting manually
                mainViewModel.userIntearctionMeditor(true)
                // Update the slider value in ViewModel
                mainViewModel.trackProgressMediator(value)
            },
            onValueChangeFinished = {
                // The user has finished interacting with the slider
                if (isUserInteracting.value) {
                    // Send a server request to update the slider value (if not already handled)
//                    mainViewModel.makePostRequestWithBody("")
                    mainViewModel.onSliderValueChangeFinished(sliderValue)
//                    isUserInteracting = false
                    mainViewModel.userIntearctionMeditor(false)
                }
            }

        )

        Column {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(top = 35.dp, start = 22.dp, end = 22.dp)) {
                Text("${min}:${sec}", color = mainbuttons_tint)
                Text(total_time_string,  color = mainbuttons_tint)
            }
        }

        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding()
            ) {

                Button(
                    onClick = { mainViewModel.makePostUrl(shuffleUrl) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(iconSize* 0.2f),
                    modifier = Modifier
                        .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                )  {
                    Icon(
                        painter = painterResource(id = R.drawable.shuffle), // Your vector drawable XML resource
                        contentDescription = "Example Icon",
                        modifier = Modifier.size(iconSize * 0.4f), // Icon size
                        tint = shuffle_tint
                    )
                }

                Button(
                    onClick = { mainViewModel.makePostUrl(previousUrl) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(iconSize* 0.2f)
                )  {
                    Icon(
                        painter = painterResource(id = R.drawable.backward), // Your vector drawable XML resource
                        contentDescription = "Example Icon",
                        modifier = Modifier.size(iconSize * 0.7f), // Icon size
                        tint = mainbuttons_tint
                    )
                }
                Button(
                    onClick = { mainViewModel.makePostUrl(playPauseUrl) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(iconSize* 0.2f)
                )  {
                    Icon(
                        painter = painterResource(id = middle_button_icon), // Your vector drawable XML resource
                        contentDescription = "Example Icon",
                        modifier = Modifier.size(iconSize * 0.9f), // Icon size
                        tint = mainbuttons_tint
                    )
                }
                Button(
                    onClick = { mainViewModel.makePostUrl(nextUrl) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(iconSize* 0.2f)
                )  {
                    Icon(
                        painter = painterResource(id = R.drawable.fast_forward), // Your vector drawable XML resource
                        contentDescription = "Example Icon",
                        modifier = Modifier.size(iconSize * 0.7f), // Icon size
                        tint = mainbuttons_tint
                    )
                }

                Button(
                    onClick = { mainViewModel.makePostUrl(repeatUrl) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(iconSize* 0.2f),
                    modifier = Modifier
                        .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                )  {
                    Icon(
                        painter = painterResource(id = repeatIcon), // Your vector drawable XML resource
                        contentDescription = "Example Icon",
                        modifier = Modifier.size(iconSize * 0.4f), // Icon size
                        tint = repeat_tint
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start= 15.dp, end = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.speaker_x), // Your vector drawable XML resource
                    contentDescription = "Example Icon",
                    modifier = Modifier
//                    .padding(start = 4.dp)
                        .size(20.dp), // Icon size
                    tint = Color.White
                )

                Slider(
                    value = mainViewModel.volumeState.value,
                    valueRange = 0f..1f,
                    modifier = Modifier.height(10.dp).padding(start = 10.dp, end =10.dp).weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White, // Remove thumb by setting it to transparent
                        activeTrackColor = Color.White, // Customize the active part of the track
                        inactiveTrackColor = MaterialTheme.colorScheme.surface, // Customize the inactive part of the track gray like color
                    ),
                    thumb = { CustomSliderThumb() },
                    track = { positions ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                        ) {
                            // Inactive (background) track
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xffc5c5c5))
                            )
                            // Active track
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(positions.value) // Use selectionFractions instead of activeRange
                                    .fillMaxHeight()
                                    .background(Color.White)
                            )
                        }
                    },

                    onValueChange = { value:Float  ->
                        // Check if the user is interacting manually
                        mainViewModel.userIntearctionMediatorVolume(true)
                        // Update the slider value in ViewModel
                        mainViewModel.volumeStateMediator(value)
                    },
                    onValueChangeFinished = {
                        // The user has finished interacting with the slider
                        if (isUserInteractingVolume.value) {
                            // Send a server request to update the slider value (if not already handled)
//                    mainViewModel.makePostRequestWithBody("")
                            mainViewModel.onVolumeSliderValueChangeFinishedVolume(sliderValueVolume)
//                    isUserInteracting = false
                            mainViewModel.userIntearctionMediatorVolume(false)
                        }
                    }
                )

                Icon(
                    painter = painterResource(id = R.drawable.speaker_high), // Your vector drawable XML resource
                    contentDescription = "Example Icon",
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(20.dp), // Icon size
                    tint = Color.White
                )
            }
        }

    }

    } else if(isConnected == "" && userUrl != "") {
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()) {
            Text("Not Connected to Cider", color = Color.White, fontSize = 30.sp)
        }
    }
    else {
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()) {
            Text("Add device First", color = Color.White, fontSize = 30.sp)
//            Text("Setup guide")
            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                AnnotatedStringWithLinkSample()
            }
        }

    }
}

@Composable
fun CustomSliderThumb(
    size: Dp = 0.dp,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun AnnotatedStringWithLinkSample() {
    // Display multiple links in the text
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Normal)) {
                append("Setup guide ")
            }
            withLink(
                LinkAnnotation.Url(
                    "https://github.com/sundae3/cider-remote-android",
                    TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.secondary))
                )
            ) {
                append("here")
            }

        }
    , fontSize = 20.sp)
}

