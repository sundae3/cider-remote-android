package com.example.ciderremotetest1.ui.components.screens.screen1


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ciderremotetest1.R
import com.example.ciderremotetest1.model.CiderInstanceData

@Composable
fun DeviceListScreen(
    paddingValues: PaddingValues,
    objectListState: List<CiderInstanceData>,
    selectedObjectState: CiderInstanceData,
    lazyListState: LazyListState,
    relativeSizeInDpWidth: Float,
    onSelectDevice: (String) -> Unit,
    onDeleteDevice: (String) -> Unit,
    shortenString: (String, Int) -> String,
    screenWidth:Dp
) {
    // Convert Float values to Dp
    val relativeSizeInDp = relativeSizeInDpWidth.dp
//    val iconSizeDp = iconSize.dp

    val characterLimitDeviceNameDp = remember(screenWidth) {
        when {
            screenWidth < 400.dp -> 20 // Small phones
            screenWidth < 600.dp -> 30 // Normal phones
            screenWidth < 840.dp -> 40 // Large phones/Small tablets
            else -> 50 // Tablets and larger
        }
    }

    val characterLimitDeviceDataDp = remember(screenWidth) {
        when {
            screenWidth < 300.dp -> 20
            screenWidth < 361.dp -> 25 // Small phones
            screenWidth < 600.dp -> 30 // Normal phones
            screenWidth < 840.dp -> 50 // Large phones/Small tablets
            else -> 60 // Tablets and larger
        }
    }
        val iconSizeDp = remember(screenWidth) {
        minOf(screenWidth / 6, 100.dp) // Maximum size of 48.dp
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Selected Device section
            Text(
                "Selected Device",
                fontSize = 25.sp,
                color = Color.White,
                modifier = Modifier.padding(
                    start = (relativeSizeInDp * 2.5f) + (iconSizeDp * 0.1f),
                    bottom = relativeSizeInDp * 3f
                )
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(relativeSizeInDp / 2f),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = relativeSizeInDp,
                            vertical = relativeSizeInDp * 2.5f
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            if (selectedObjectState.url != "") {
                                Button(
                                    onClick = { },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                    contentPadding = PaddingValues(iconSizeDp * 0.1f),
                                    modifier = Modifier
                                        .defaultMinSize(
                                            minWidth = 1.dp,
                                            minHeight = 1.dp
                                        )
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.radio_button_fill),
                                        contentDescription = "Radio Icon",
                                    )
                                }

                                Column(modifier = Modifier.padding(start = relativeSizeInDp * 2f)) {
                                    Text(
                                        shortenString(
                                            selectedObjectState.deviceName,
                                            characterLimitDeviceNameDp
                                        ),
                                        color = Color.White,
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        shortenString(
                                            selectedObjectState.url,
                                            characterLimitDeviceDataDp
                                        ),
                                        color = Color.LightGray,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        selectedObjectState.method,
                                        color = Color.LightGray,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // All Devices Header
            Text(
                "All Devices",
                fontSize = 25.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(
                        start = (relativeSizeInDp * 2.5f) + (iconSizeDp * 0.1f),
                        bottom = relativeSizeInDp * 3f,
                        top = relativeSizeInDp * 3f
                    )
            )

            Spacer(modifier = Modifier.height(relativeSizeInDp))

            // 2. Device List Section
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = lazyListState
            ) {
                items(objectListState, key = { it.id }) { item ->
                    DeviceCard(
                        device = item,
                        relativeSizeInDp = relativeSizeInDp,
                        iconSizeDp = iconSizeDp,
                        characterLimitDeviceName = characterLimitDeviceNameDp,
                        characterLimitDeviceData = characterLimitDeviceDataDp,
                        onSelectDevice = onSelectDevice,
                        onDeleteDevice = onDeleteDevice,
                        shortenString = shortenString
                    )
                }
            }
        }
    }
}

@Composable
private fun DeviceCard(
    device: CiderInstanceData,
    relativeSizeInDp: Dp,
    iconSizeDp: Dp,
    characterLimitDeviceName: Int,
    characterLimitDeviceData: Int,
    onSelectDevice: (String) -> Unit,
    onDeleteDevice: (String) -> Unit,
    shortenString: (String, Int) -> String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(relativeSizeInDp / 2f),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        var showDeleteDialog by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = relativeSizeInDp,
                    vertical = relativeSizeInDp * 2.5f
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Button(
                        onClick = { onSelectDevice(device.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(iconSizeDp * 0.1f),
                        modifier = Modifier
                            .defaultMinSize(
                                minWidth = 1.dp,
                                minHeight = 1.dp
                            )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.radio_button),
                            contentDescription = "Radio Icon",
                        )
                    }

                    Column(modifier = Modifier.padding(start = relativeSizeInDp * 2f)) {
                        Text(
                            shortenString(
                                device.deviceName,
                                characterLimitDeviceName
                            ),
                            color = Color.White,
                            fontSize = 20.sp
                        )
                        Text(
                            shortenString(
                                device.url,
                                characterLimitDeviceData
                            ),
                            color = Color.LightGray,
                            fontSize = 15.sp
                        )
                        Text(
                            device.method,
                            color = Color.LightGray,
                            fontSize = 15.sp
                        )
                    }
                }

                Button(
                    onClick = { showDeleteDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(iconSizeDp * 0.1f),
                    modifier = Modifier
                        .defaultMinSize(
                            minWidth = 1.dp,
                            minHeight = 1.dp
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.x_circle),
                        contentDescription = "Delete Icon",
                    )
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Confirm Delete") },
                text = { Text("Are you sure you want to delete this item?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDeleteDevice(device.id)
                            showDeleteDialog = false
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}