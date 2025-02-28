package com.example.ciderremotetest1.ui.components.screens.screen1

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.ciderremotetest1.viewmodel.MainViewModel

@Composable
fun FormOverlay(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    field1: String,
    onField1Change: (String) -> Unit,
    field2: String,
    onField2Change: (String) -> Unit,
    field3: String,
    onField3Change: (String) -> Unit,
    field4: String,
    onField4Change: (String) -> Unit,
    mainViewModel: MainViewModel,
    relativeSizeInDpWidth: Dp,
    deviceFieldsResetter: () -> Unit
) {
    if (isVisible) {
        // Background Box with blur effect
        Box(
            modifier = Modifier
                .fillMaxSize()
//                    .background(color =Color.Transparent)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null // No ripple effect
                ) { /* Intercept clicks */ }
        )
        // Content Box with form, overlay, etc.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0f) )
                .padding(relativeSizeInDpWidth*4f)
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = Color.Black.copy(alpha = 0.4f))
                    .padding(relativeSizeInDpWidth*5f)
                ,
                verticalArrangement = Arrangement.Center
            ) {
                // Header with close button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp, top=10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Enter Device Details",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
//                        IconButton(onClick = onDismiss) {
//                            Icon(Icons.Default.Close, contentDescription = "Close Form", tint= Color.White)
//                        }
                }

                // Form Fields
                OutlinedTextField(
                    value = field1,
                    onValueChange = onField1Change,
                    label =  { Text("Device Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.LightGray,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.LightGray,
                        cursorColor = Color.White
                        // change to the color you want when focused
                        // change to the color you want when not focused
                    )
                )

                OutlinedTextField(
                    value = field2,
                    onValueChange = onField2Change,
                    label =  { Text("Url/Address") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.LightGray,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.LightGray,
                        cursorColor = Color.White
                    )
                )


                OutlinedTextField(
                    value = field3,
                    onValueChange = onField3Change,
                    label =  { Text("Api Token") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.LightGray,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.LightGray,
                        cursorColor = Color.White
                    )
                )

                OutlinedTextField(
                    value = field4,
                    onValueChange = onField4Change,
                    label =  { Text("Method: wan/lan") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.LightGray,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.LightGray,
                        cursorColor = Color.White
                    )
                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            // Log the values
                            println("Form submitted with values:")
                            println("Field 1: $field1")
                            println("Field 2: $field2")
                            println("Field 3: $field3")
                            // Dismiss the form
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", color= Color.White)
                    }

                    Button(
                        onClick = {
                            // Log the values
                            println("Form submitted with values:")
                            println("Field 1: $field1")
                            println("Field 2: $field2")
                            println("Field 3: $field3")
                            mainViewModel.manualDeviceSubmitter(deviceName = field1, url = field2, token = field3, method = field4.lowercase())
                            // Dismiss the form
                            deviceFieldsResetter()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Submit")
                    }
                }


            }
        }



    }
}

@Composable
fun DeviceNameFormOverlay(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    field1: String,
    onField1Change: (String) -> Unit,
    scannedStringValue: String,
    mainViewModel: MainViewModel,
    deviceNameField: String,
    relativeSizeInDpWidth: Dp,
    deviceNameFieldResetter: () -> Unit
) {
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
//                .background(color =Color.Transparent)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null // No ripple effect
                ) { /* Intercept clicks */ }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0f) )
                    .padding(relativeSizeInDpWidth*4f)
                    .zIndex(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = Color.Black.copy(alpha = 0.4f))
                        .padding(relativeSizeInDpWidth*5f)
                    ,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Header with close button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp, top=10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Confirm Device",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )
//                        IconButton(onClick = onDismiss) {
//                            Icon(Icons.Default.Close, contentDescription = "Close Form", tint = Color.White)
//                        }
                    }

                    // Form Fields
                    OutlinedTextField(
                        value = field1,
                        onValueChange = onField1Change,
                        label = { Text("Name the device") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.LightGray,
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.LightGray,
                            cursorColor = Color.White
                        )
                    )


                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                // Log the values
                                println("Form submitted with values:")
                                // Dismiss the form
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel", color= Color.White)
                        }

                        Button(
                            onClick = {
                                // Log the values
                                println("Form submitted with values:")
                                mainViewModel.qrScanSubmiter(scannedStringValue,field1)
                                // Dismiss the form
                                deviceNameFieldResetter()
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Submit")
                        }
                    }
                }
            }
        }

    }
}