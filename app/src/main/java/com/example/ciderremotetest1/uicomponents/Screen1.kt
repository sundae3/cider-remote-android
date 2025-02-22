package com.example.ciderremotetest1.uicomponents


//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
import com.example.ciderremotetest1.viewmodel.MainViewModel
//
//@Composable
//fun Screen1(mainViewModel: MainViewModel) {
//    val viewModel = mainViewModel
//    var name by remember { mutableStateOf("") }
//    var token by remember { mutableStateOf("") }
//    var url by remember { mutableStateOf("") }
//    var text by remember { mutableStateOf("") }
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//
////            TextField(
////                value = text,
////                onValueChange = { text = it },
////                label = { Text("Enter URL") },
////                modifier = Modifier // Optional: fill width as needed // Optional: add padding as needed
////                    .clip(RoundedCornerShape(5.dp)) // This makes the corners rounded
////                    .background(Color.Transparent)
////            )
//            TextField(
//                value = name,
//                onValueChange = { name = it },
//                label = { Text("Enter device name") },
//                modifier = Modifier // Optional: fill width as needed // Optional: add padding as needed
//                    .clip(RoundedCornerShape(5.dp)) // This makes the corners rounded
//                    .background(Color.Transparent)
//            )
//            TextField(
//                value = token,
//                onValueChange = { token = it },
//                label = { Text("Enter Token") },
//                modifier = Modifier // Optional: fill width as needed // Optional: add padding as needed
//                    .clip(RoundedCornerShape(5.dp)) // This makes the corners rounded
//                    .background(Color.Transparent)
//            )
//            TextField(
//                value = url,
//                onValueChange = { url = it },
//                label = { Text("Enter URL") },
//                modifier = Modifier // Optional: fill width as needed // Optional: add padding as needed
//                    .clip(RoundedCornerShape(5.dp)) // This makes the corners rounded
//                    .background(Color.Transparent)
//            )
//            Button(
////                onClick = { viewModel.saveUserUrl(text)},
//                onClick = { viewModel.addObjectToList(deviceName = name, url = url, token = token, method = "lan")},
//                modifier = Modifier.padding(top = 16.dp)
//            ) {
//                Text("Save URL")
//            }
//            Text(text = "Stored URL: ${viewModel.baseUserUrl.value}", modifier = Modifier.padding(top = 16.dp), color = Color.White)
//        }
//    }
//}

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
//import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import com.example.ciderremotetest1.R
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.times

@kotlin.OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier, // Default empty modifier
    colors: TextFieldColors = TextFieldDefaults.colors( // Default color settings
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Transparent,
        unfocusedLabelColor = Color.White,
        focusedLabelColor = Color.White,
        cursorColor = Color.White
//        focusedIndicatorColor = Color.Transparent,
//        unfocusedIndicatorColor = Color.Transparent
    )
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        colors = colors
    )
}

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
    relativeSizeInDpWidth: Dp
) {
    if (isVisible) {
            // Background Box with blur effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = mainViewModel.topColors.value[0])
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
                        .background(color = Color.Black.copy(alpha = 0.6f))
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
                        label =  {Text("Device Name")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    )

                    OutlinedTextField(
                        value = field2,
                        onValueChange = onField2Change,
                        label =  {Text("Url/Address")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    )


                    OutlinedTextField(
                        value = field3,
                        onValueChange = onField3Change,
                        label =  {Text("Api Token")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    )

                    OutlinedTextField(
                        value = field4,
                        onValueChange = onField4Change,
                        label =  {Text("Method: wan/lan")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                    )


//                    var text by remember { mutableStateOf("") }
//
//                    OutlinedTextField(
//                        value = text,
//                        onValueChange = { text = it },
//                        label = { Text("Label") }
//                    )

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
                            Text("Cancel", color=Color.White)
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
    relativeSizeInDpWidth: Dp
) {
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = mainViewModel.topColors.value[0])
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
                        .background(color = Color.Black.copy(alpha = 0.6f))
                        .padding(relativeSizeInDpWidth*10f)
                    ,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Header with close button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
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
                        label = {Text("Name the device")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
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
                            Text("Cancel", color=Color.White)
                        }

                        Button(
                            onClick = {
                                // Log the values
                                println("Form submitted with values:")
                                mainViewModel.qrScanSubmiter(scannedStringValue,field1)
                                // Dismiss the form
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

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val relativeSizeInDpWidth = (1f/ 100f) * screenWidth.value.dp
    val iconSize = remember(screenWidth) {
        minOf(screenWidth / 6, 100.dp) // Maximum size of 48.dp
    }
    val characterLimitDeviceName = remember(screenWidth) {
        when {
            screenWidth < 400.dp -> 20 // Small phones
            screenWidth < 600.dp -> 30 // Normal phones
            screenWidth < 840.dp -> 40 // Large phones/Small tablets
            else -> 50 // Tablets and larger
        }
    }

    val characterLimitDeviceData = remember(screenWidth) {
        when {
            screenWidth < 300.dp -> 20
            screenWidth < 361.dp -> 25 // Small phones
            screenWidth < 600.dp -> 30 // Normal phones
            screenWidth < 840.dp -> 50 // Large phones/Small tablets
            else -> 60 // Tablets and larger
        }
    }

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

    Box(modifier = Modifier.fillMaxSize()) {
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
                        containerColor = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Show Form", tint = Color.White)
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
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center,
//                            modifier = Modifier.padding(horizontal = 15.dp)
//                        ) {
//                            Text("Scan", modifier = Modifier.padding(end = 5.dp))
                            Icon(
                                painter = painterResource(R.drawable.qr_code), // Your vector drawable XML resource
                                contentDescription = "QR Icon",
                            )
//                        }

                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 1. Fixed card section
                    Text("Selected Device", fontSize = 25.sp, color = Color.White, modifier = Modifier.padding(start= (relativeSizeInDpWidth*2.5f)+iconSize* 0.1f,bottom = relativeSizeInDpWidth*3f))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(relativeSizeInDpWidth/2),
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
                                .padding(horizontal = relativeSizeInDpWidth , vertical = relativeSizeInDpWidth*2.5f)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row {
                                    if(selectedObjectState.url != "") {
                                        Button(
                                            onClick = { },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                            contentPadding = PaddingValues(iconSize * 0.1f),
                                            modifier = Modifier
                                                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.radio_button_fill), // Your vector drawable XML resource
                                                contentDescription = "Radio Icon",
                                            )
                                        }

                                        Column(modifier = Modifier.padding(start = relativeSizeInDpWidth*3f)) {
                                            Text(
                                                mainViewModel.shortenString(
                                                    selectedObjectState.deviceName,
                                                    characterLimitDeviceName
                                                ), color = Color.White, fontSize = 20.sp
                                            )
                                            Text(
                                                mainViewModel.shortenString(
                                                    selectedObjectState.url,
                                                    characterLimitDeviceData
                                                ), color = Color.LightGray, fontSize = 15.sp
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
                    Text("All Devices", fontSize = 25.sp, color = Color.White,
                        modifier = Modifier
                            .padding(start= (relativeSizeInDpWidth*2.5f)+iconSize* 0.1f,
                                     bottom = relativeSizeInDpWidth*3f,
                                     top = relativeSizeInDpWidth*3f
                            )
                    )

                    Spacer(modifier = Modifier.height(relativeSizeInDpWidth))

                    // 2. Lazy Column with Cards - will scroll independently
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), // This makes the LazyColumn take remaining space,
                         state = lazyListState
                    ) {
                        items(objectListState, key = { it.id }) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(relativeSizeInDpWidth/2),
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
                                        .padding(horizontal = relativeSizeInDpWidth , vertical = relativeSizeInDpWidth*2.5f)
                                ) {

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row {
                                            Button(
                                                onClick = { mainViewModel.saveSelectedObject(item.id) },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                                contentPadding = PaddingValues(iconSize* 0.1f),
                                                modifier = Modifier
                                                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                                            ) {
                                                Icon(
                                                    painter = painterResource(R.drawable.radio_button), // Your vector drawable XML resource
                                                    contentDescription = "Radio Icon",
                                                )
                                            }
                                            Column(modifier = Modifier.padding(start = relativeSizeInDpWidth*3f)) {
                                                Text(mainViewModel.shortenString(item.deviceName, characterLimitDeviceName), color = Color.White, fontSize = 20.sp)
                                                Text(mainViewModel.shortenString(item.url, characterLimitDeviceData), color = Color.LightGray, fontSize = 15.sp)
                                                Text(item.method, color = Color.LightGray, fontSize = 15.sp)
                                            }
                                        }

                                        Button(
                                            onClick = { showDeleteDialog = true },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                            contentPadding = PaddingValues(iconSize* 0.1f),
                                            modifier = Modifier
                                                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.x_circle), // Your vector drawable XML resource
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
                                                    // Add your delete logic here
                                                    // mainViewModel.deleteObject(item.id)
                                                    mainViewModel.removeObjectFromList(item.id)
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
                    }
                }
            }
        }

        // Form Overlay
        FormOverlay(
            isVisible = isFormVisible,
            onDismiss = { isFormVisible = false },
            field1 = deviceNameField,
            onField1Change = { deviceNameField = it },
            field2 = deviceAddressField,
            onField2Change = { deviceAddressField = it },
            field3 = tokenField,
            onField3Change = { tokenField = it },
            field4 = connectionMethodField,
            onField4Change = {connectionMethodField = it},
            mainViewModel = mainViewModel,
            relativeSizeInDpWidth = relativeSizeInDpWidth
        )

        DeviceNameFormOverlay(
            isVisible = isDeviceNameFormVisible,
            onDismiss = {isDeviceNameFormVisible= false},
            field1 = QRScanDeviceNameField,
            onField1Change = {QRScanDeviceNameField = it},
            mainViewModel = mainViewModel,
            scannedStringValue = scannedString,
            deviceNameField = deviceNameField,
            relativeSizeInDpWidth = relativeSizeInDpWidth
        )

        // QR Scanner Overlay
        if (isScanning) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .zIndex(1f)
            ) {
                val executor = remember { Executors.newSingleThreadExecutor() }
                var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

                AndroidView(
                    factory = { context ->
                        PreviewView(context).apply {
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { previewView ->
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                    cameraProviderFuture.addListener({
                        cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build()
                        preview.setSurfaceProvider(previewView.surfaceProvider)

                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()

                        val scanner = BarcodeScanning.getClient()
                        val analyzer = ImageAnalysis.Analyzer { imageProxy ->
                            val mediaImage = imageProxy.image
                            if (mediaImage != null) {
                                val image = InputImage.fromMediaImage(
                                    mediaImage,
                                    imageProxy.imageInfo.rotationDegrees
                                )
                                scanner.process(image)
                                    .addOnSuccessListener { barcodes ->
                                        for (barcode in barcodes) {
                                            barcode.rawValue?.let { value ->
                                                scannedString = value
                                                isScanning = false
                                                isDeviceNameFormVisible = true

                                                // Cleanup resources
                                                cameraProvider?.unbindAll()
                                                scanner.close()
                                                executor.shutdown()
                                            }
                                        }
                                    }
                                    .addOnCompleteListener {
                                        imageProxy.close()
                                    }
                            } else {
                                imageProxy.close()
                            }
                        }

                        imageAnalysis.setAnalyzer(
                            executor,
                            analyzer
                        )

                        try {
                            cameraProvider?.unbindAll()
                            cameraProvider?.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, ContextCompat.getMainExecutor(context))
                }

// Clean up when the composable is disposed
                DisposableEffect(Unit) {
                    onDispose {
                        cameraProvider?.unbindAll()
                        executor.shutdown()
                    }
                }

                // Close button for scanner
                IconButton(
                    onClick = { isScanning = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close Scanner",
                        tint = Color.White
                    )
                }
            }
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
