package com.example.ciderremotetest1.ui.components.screens.screen1

import com.example.ciderremotetest1.viewmodel.MainViewModel
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.ciderremotetest1.R
import androidx.compose.ui.unit.times
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

@kotlin.OptIn(ExperimentalMaterial3Api::class)

@OptIn(ExperimentalGetImage::class)
@Composable
fun Screen1(mainViewModel: MainViewModel) {

    val objectListState by mainViewModel.objectListState.collectAsState()
    val selectedObjectState by mainViewModel.selectedObjectState.collectAsState()
    val lazyListState = rememberLazyListState()

    var QRScanDeviceNameField by remember { mutableStateOf("") }
    var deviceAddressField by remember { mutableStateOf("") }
    var tokenField by remember { mutableStateOf("") }
    var deviceNameField by remember { mutableStateOf("") }
    var connectionMethodField by remember { mutableStateOf("") }
    var isScanning by remember { mutableStateOf(false) }
    var isFormVisible by remember { mutableStateOf(false) }
    var isDeviceNameFormVisible by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var permissionDenied by remember { mutableStateOf(false) }

    var scannedString by remember { mutableStateOf("") }

    BackHandler(enabled = isScanning || isFormVisible || isDeviceNameFormVisible || showPermissionDialog) {
        when {
            showPermissionDialog -> showPermissionDialog = false
            isScanning -> isScanning = false
            isFormVisible -> isFormVisible = false
            isDeviceNameFormVisible -> isDeviceNameFormVisible = false
        }
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val relativeSizeInDpWidthDp = (1f/ 100f) * screenWidth.value.dp
    val relativeSizeInDpWidth = 8f

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            permissionDenied = false
            isScanning = true
        } else {
            permissionDenied = true
            showPermissionDialog = true
        }
    }
    val isAnyOverlayVisible = isScanning || isFormVisible || isDeviceNameFormVisible

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = !isAnyOverlayVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            // Main Content
            Scaffold(
                containerColor = Color.Transparent,
                floatingActionButton = {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                    ) {
                        // Form FAB
                        FloatingActionButton(
                            onClick = { isFormVisible = true },
                            containerColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.plus_circle),
                                contentDescription = "Show Form",
                                tint = Color.White
                            )
                        }

                        // Scanner FAB
                        FloatingActionButton(
                            onClick = {
                                when {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED -> {
                                        isScanning = true
                                        permissionDenied = false
                                    }

                                    else -> {
                                        permissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                }
                            },
                            containerColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.qr_code), // Your vector drawable XML resource
                                contentDescription = "QR Icon",
                            )
                        }
                    }
                }
            ) { paddingValues ->
                // main content
                DeviceListScreen(
                    paddingValues = paddingValues,
                    objectListState = objectListState,
                    selectedObjectState = selectedObjectState,
                    lazyListState = lazyListState,
                    relativeSizeInDpWidth = relativeSizeInDpWidth,
                    onSelectDevice = { deviceId ->
                        mainViewModel.saveSelectedObject(deviceId)
                    },
                    onDeleteDevice = { deviceId ->
                        mainViewModel.removeObjectFromList(deviceId)
                    },
                    shortenString = { string, limit ->
                        mainViewModel.shortenString(string, limit)
                    },
                    screenWidth
                )
            }
        }

        // Form Overlay
        AnimatedVisibility(
            visible = isFormVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FormOverlay(
                isVisible = true,  // Always true since AnimatedVisibility controls visibility
                onDismiss = { isFormVisible = false },
                field1 = deviceNameField,
                onField1Change = { deviceNameField = it },
                field2 = deviceAddressField,
                onField2Change = { deviceAddressField = it },
                field3 = tokenField,
                onField3Change = { tokenField = it },
                field4 = connectionMethodField,
                onField4Change = { connectionMethodField = it },
                mainViewModel = mainViewModel,
                relativeSizeInDpWidth = relativeSizeInDpWidthDp
            )
        }

        // Device Name Form Overlay
        AnimatedVisibility(
            visible = isDeviceNameFormVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            DeviceNameFormOverlay(
                isVisible = true,  // Always true since AnimatedVisibility controls visibility
                onDismiss = { isDeviceNameFormVisible = false },
                field1 = QRScanDeviceNameField,
                onField1Change = { QRScanDeviceNameField = it },
                mainViewModel = mainViewModel,
                scannedStringValue = scannedString,
                deviceNameField = deviceNameField,
                relativeSizeInDpWidth = relativeSizeInDpWidthDp
            )
        }


        // QR Scanner Overlay
        AnimatedVisibility(
            visible = isScanning,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            QRScanner(
                isScanning = isScanning,
                onScanComplete = { value ->
                    scannedString = value
                    isScanning = false
                    isDeviceNameFormVisible = true
                },
                onCloseScanner = {
                    isScanning = false
                },
                lifecycleOwner = lifecycleOwner
            )
        }

            // Permission Dialog
            if (showPermissionDialog) {
                AlertDialog(
                    onDismissRequest = { showPermissionDialog = false },
                    title = { Text("Camera Permission Required") },
                    text = { Text("The camera permission is required to use the QR scanner. Please grant the permission in app settings.") },
                    confirmButton = {
                        TextButton(onClick = { showPermissionDialog = false }) {
                            Text("OK")
                        }
                    }
                )
            }

    }
}
