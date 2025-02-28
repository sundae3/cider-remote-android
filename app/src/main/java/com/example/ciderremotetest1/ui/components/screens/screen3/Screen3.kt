package com.example.ciderremotetest1.ui.components.screens.screen3

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.ciderremotetest1.viewmodel.MainViewModel
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration

import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import kotlinx.coroutines.delay
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalWearMaterialApi::class
)
@Composable
fun Screen3(mainViewModel: MainViewModel) {
    val view = LocalView.current

    val list2 by mainViewModel.queue.observeAsState(emptyList())


    // Lazy list state for scrolling
    val lazyListState = rememberLazyListState()

    // Reorderable state for drag-and-drop
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        // Update the list on reorder
        mainViewModel.reorderItems(from.index, to.index)

        // Perform haptic feedback for the user during drag
        ViewCompat.performHapticFeedback(
            view,
            HapticFeedbackConstantsCompat.SEGMENT_FREQUENT_TICK
        )
    }

    val targetId = mainViewModel.nowPlayingData.value.info.playParams.id

    LaunchedEffect(targetId) {
        if (targetId != null) {
            // Find the index of the item with the targetId
            val targetIndex = list2.indexOfFirst { it.id == targetId }

            // If the item is found, scroll to it
            if (targetIndex != -1) {
                delay(500)
                lazyListState.animateScrollToItem(targetIndex)
            }
        }
    }

    val targetIndex = list2.indexOfFirst { it.id == targetId }

    // Split the list into reorderable and non-reorderable items based on the target index
    val reorderableItems = list2.filterIndexed { index, _ -> index > targetIndex }
    val nonReorderableItems = list2.filterIndexed { index, _ -> index <= targetIndex }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val relativeSizeInDpHeight = (0.1f/ 100f) * screenHeight.value.dp

    val characterLimit = remember(screenWidth) {
        when {
            screenWidth < 360.dp -> 20 // Small phones
            screenWidth < 600.dp -> 30 // Normal phones
            screenWidth < 840.dp -> 40 // Large phones/Small tablets
            else -> 50 // Tablets and larger
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = PaddingValues(0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
//            var hasStickyHeaderShown = false  // To control sticky header visibility


            items(nonReorderableItems, key = { it.id }) { item ->
                // For non-reorderable items
                var fixedArtworkUrl = item.attributes.artwork.url.replace("{w}x{h}", "128x128")
                Surface(
                    shadowElevation = 0.dp,
                    color = Color.Transparent,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 5.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null // Disables the overlay effect (click feedback)
                            ) {
                                mainViewModel.queueClickHandler(list2.indexOfFirst { it.id == item.id })
                            },
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            mainViewModel.QueueImageLoader(fixedArtworkUrl)
                            Column(modifier = Modifier.padding()) {
                                Text(
                                    text = mainViewModel.trimText(
                                        item.attributes.name,
                                        characterLimit
                                    ),
                                    Modifier.padding(horizontal = 8.dp),
                                    color = Color.White,
                                    fontSize = 16.sp,
                                )
                                Text(
                                    text = mainViewModel.trimText(
                                        item.attributes.name,
                                        characterLimit
                                    ),
                                    Modifier.padding(
                                        horizontal = 8.dp,
                                        vertical = relativeSizeInDpHeight
                                    ),
                                    color = Color.LightGray,
                                    fontSize = 12.sp,
                                )
                            }
                        }

                    }
                }
            }


            items(reorderableItems, key = { it.id }) { item ->
                var fixedArtworkUrl = item.attributes.artwork.url.replace("{w}x{h}", "128x128")

                if (item.attributes.artistName != "") {
                    // Reorderable item with haptic feedback and drag handle
                    ReorderableItem(reorderableLazyListState, key = item.id) { isDragging ->
                        val elevation by animateDpAsState(if (isDragging) 1.dp else 0.dp)

                        val swipeState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { states ->
                                if (states == SwipeToDismissBoxValue.EndToStart) {
                                    mainViewModel.removeItemFromQueue(item.id)
                                    true
                                } else {
                                    false
                                }
                            }
                        )

                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.Transparent,
                            shadowElevation = elevation
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                // Background swipe area
                                SwipeToDismissBox(
                                    state = swipeState,
                                    enableDismissFromStartToEnd = false,
                                    backgroundContent = {
                                        // Detect swipe direction (dismissed to the end)
                                        val isBeingDismissed =
                                            swipeState.currentValue == SwipeToDismissBoxValue.EndToStart ||
                                                    swipeState.targetValue == SwipeToDismissBoxValue.EndToStart

                                        // Animate the background color (could adjust this if you want to use an actual color animation)
                                        val backgroundColor by animateColorAsState(
                                            targetValue = if (isBeingDismissed) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            label = "background color"
                                        )

                                        // Animate the appearance of the delete icon (using alpha or scale)
                                        val iconAlpha by animateFloatAsState(
                                            targetValue = if (isBeingDismissed) 1f else 0f, // Fade in when being dismissed
                                            animationSpec = tween(durationMillis = 600) // Adjust the duration of the fade-in animation
                                        )

                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(backgroundColor)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxSize(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                if (isBeingDismissed) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Delete,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier
                                                            .padding(end = 20.dp)
                                                            .alpha(iconAlpha) // Apply the animated alpha value
                                                    )
                                                }
                                            }
                                        }
                                    }
                                ) {
                                    // Content area with transparent background
                                    Surface(
                                        modifier = Modifier.padding(top = 3.dp),
                                        color = Color.Transparent
                                    ) {
                                        Column(verticalArrangement = Arrangement.Center) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(horizontal = 15.dp, vertical = 5.dp)
                                                    .clickable(
                                                        interactionSource = remember { MutableInteractionSource() },
                                                        indication = null // Disables the overlay effect (click feedback)
                                                    ) {
                                                        mainViewModel.queueClickHandler(list2.indexOfFirst { it.id == item.id })
                                                    },
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Column(
                                                        modifier = Modifier.padding(),
                                                        verticalArrangement = Arrangement.Center
                                                    ) {
                                                        mainViewModel.QueueImageLoader(
                                                            fixedArtworkUrl
                                                        )
                                                    }
                                                    Column(modifier = Modifier.padding()) {
                                                        Text(
                                                            text = mainViewModel.trimText(
                                                                item.attributes.name,
                                                                characterLimit
                                                            ),
                                                            modifier = Modifier.padding(
                                                                start = 8.dp,
                                                                end = 8.dp,
                                                                bottom = relativeSizeInDpHeight
                                                            ),
                                                            color = Color.White,
                                                            fontSize = 16.sp,
                                                        )
                                                        Text(
                                                            text = mainViewModel.trimText(
                                                                item.attributes.name,
                                                                characterLimit
                                                            ),
                                                            modifier = Modifier.padding(
                                                                start = 8.dp,
                                                                end = 8.dp,
                                                            ),
                                                            color = Color.LightGray,
                                                            fontSize = 12.sp,
                                                        )
                                                    }
                                                }

                                                IconButton(
                                                    modifier = Modifier.draggableHandle(
                                                        onDragStarted = {
                                                            ViewCompat.performHapticFeedback(
                                                                view,
                                                                HapticFeedbackConstantsCompat.GESTURE_START
                                                            )
                                                        },
                                                        onDragStopped = {
                                                            ViewCompat.performHapticFeedback(
                                                                view,
                                                                HapticFeedbackConstantsCompat.GESTURE_END
                                                            )
                                                            mainViewModel.findMovedItem(item.id)
                                                        },
                                                    ),
                                                    onClick = {},
                                                ) {
                                                    Icon(
                                                        Icons.Rounded.Menu,
                                                        contentDescription = "Reorder",
                                                        tint = Color.LightGray
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

    }
}
