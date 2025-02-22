package com.example.ciderremotetest1

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Color


@Composable
fun CiderRemoteTest1Theme(content: @Composable () -> Unit) {
    val lightColors = lightColorScheme(
        primary = Color(0xFFfa2d48),   // Purple
        secondary = Color(0xFFF48FB1), // Teal
        background = Color(0xFFFFFFFF), // Light background
        surface = Color(0xFFF5F5F5),    // Light surface color
        onPrimary = Color.White,        // Text on primary
        onSecondary = Color.Black,      // Text on secondary
        onBackground = Color.Black,     // Text on background
        onSurface = Color.Black,         // Text on surface
    )

    // Define the colors for the dark theme
    val darkColors = darkColorScheme(
//        primary = Color(0xFFfa2d48),   // Purple (same as light theme)
        primary = Color(0xFFE91E63),   // Purple (same as light theme)
        secondary = Color(0xFFF48FB1), // Teal (same as light theme)
        background = Color(0xFFFFFFFF), // Dark background
        surface = Color(0xFF1E1E1E),    // Dark surface color
        onPrimary = Color.White,        // Text on primary
        onSecondary = Color.Black,      // Text on secondary
        onBackground = Color.White,     // Text on background
        onSurface = Color.White         // Text on surface
    )

    // Detect system theme (light or dark) and apply the respective color scheme
    val colorScheme = if (isSystemInDarkTheme()) darkColors else lightColors

    // Apply the theme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        shapes = Shapes(),
        content = content // Apply theme to content
    )
}
