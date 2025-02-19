package com.example.ciderremotetest1.uicomponents


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ciderremotetest1.viewmodel.MainViewModel

@Composable
fun Screen1(mainViewModel: MainViewModel) {
    val viewModel = mainViewModel
    var text by remember { mutableStateOf("") }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter URL") },
                modifier = Modifier // Optional: fill width as needed // Optional: add padding as needed
                    .clip(RoundedCornerShape(5.dp)) // This makes the corners rounded
                    .background(Color.Transparent)
            )
            Button(
                onClick = { viewModel.saveUserUrl(text)},
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Save URL")
            }
            Text(text = "Stored URL: ${viewModel.baseUserUrl.value}", modifier = Modifier.padding(top = 16.dp), color = Color.White)
        }
    }
}
