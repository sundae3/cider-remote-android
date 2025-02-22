package com.example.ciderremotetest1.uicomponents

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ciderremotetest1.viewmodel.MainViewModel

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import com.example.ciderremotetest1.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import androidx.compose.foundation.Canvas
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp


data class CircleInfo(
    val color: Color,
    val position: Offset,
    val animatedPosition: Animatable<Offset, AnimationVector2D>
)

@Composable
fun MyApp(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val imageBitmap = mainViewModel.imageBitmap.value
    var offset by remember { mutableStateOf(0f) }
    val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow)
    val colors2 = mainViewModel.topColors.value
    val numberOfCircles: Int = 20
    val blurRadius = 100.dp
    val updateIntervalMillis: Long = 15000

    val density = LocalDensity.current
    val blurRadiusPx = with(density) { blurRadius.toPx() }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    // Define max content width
    val maxContentWidth = 600.dp

    // Calculate responsive dimensions
    val bottomNavHeight = remember(screenHeight) {
        minOf(screenHeight * 0.15f, 80.dp)
    }

    val contentPadding = remember(screenHeight) {
        minOf(screenHeight * 0.025f, 20.dp)
    }

    val circles = remember(colors2) {
        List(numberOfCircles) {
            CircleInfo(
                color = colors2.getOrNull(Random.nextInt(colors2.size))
                    ?.copy(alpha = 0.6f)
                    ?: Color.Gray.copy(alpha = 0.6f),
                position = Offset(
                    Random.nextFloat() * 1000f,
                    Random.nextFloat() * 2000f
                ),
                animatedPosition = Animatable(
                    Offset(
                        Random.nextFloat() * 1000f,
                        Random.nextFloat() * 2000f
                    ),
                    Offset.VectorConverter
                )
            )
        }
    }

    LaunchedEffect(Unit, colors2) {
        while (true) {
            println(mainViewModel.topColors.value)
            circles.forEach { circle ->
                launch {
                    circle.animatedPosition.animateTo(
                        targetValue = Offset(
                            Random.nextFloat() * 1000f,
                            Random.nextFloat() * 2000f
                        ),
                        animationSpec = tween(
                            durationMillis = updateIntervalMillis.toInt(),
                            easing = LinearEasing
                        )
                    )
                }
            }
            delay(updateIntervalMillis)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        // Background layer spans full width
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        renderEffect = BlurEffect(
                            radiusX = 200f,
                            radiusY = 200f,
                        )
                    }
                    .background(
                        brush = ShaderBrush(
                            ImageShader(imageBitmap)
                        )
                    )
            ) {
                circles.forEach { circle ->
                    drawCircle(
                        color = circle.color,
                        radius = blurRadiusPx,
                        center = circle.animatedPosition.value
                    )
                }
            }
        }

        // Overlay layer spans full width
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        // Content layer with width constraint
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = maxContentWidth),
//                .padding(horizontal = contentPadding),
            color = Color.Transparent
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = Color.Transparent,
                bottomBar = {
                    BottomNavigationBar(
                        navController = navController,
                        bottomNavHeight = bottomNavHeight
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(top = screenHeight/30)
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "screen2"
                    ) {
                        composable("screen1") { Screen1(mainViewModel) }
                        composable("screen2") { Screen2(mainViewModel) }
                        composable("screen3") { Screen3(mainViewModel) }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController,bottomNavHeight: Dp) {

    val play_icon = R.drawable.play_circle
    val play_filled = R.drawable.play_circle_fill
    val settings_icon = R.drawable.settings_gear
    val settings_fill= R.drawable.gear_fill
    var list_icon = R.drawable.rows_plus_top
    var list_fill = R.drawable.rows_plus_top_fill

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Calculate responsive padding
    val horizontalPadding = remember(screenWidth) {
        minOf(screenWidth * 0.04f, 16.dp) // 4% of screen width, max 16.dp
    }

    val itemSpacing = remember(screenWidth) {
        minOf(screenWidth * 0.03f, 12.dp) // 3% of screen width, max 12.dp
    }


    NavigationBar(
        modifier = Modifier
            .height(bottomNavHeight)
            .padding(horizontal = horizontalPadding),
        containerColor = Color.Transparent
    ) {
        NavigationBarItem(
            selected = currentRoute == "screen1", // Check if screen1 is selected,
            onClick = { navController.navigate("screen1") },
            label = { Text("Setup", color = Color.White) },
            icon = {
                Icon(

                    painter = if (currentRoute == "screen1") painterResource(id = settings_fill) else painterResource(id = settings_icon),
                    modifier = Modifier.size(25.dp),
                    contentDescription = "Setup",
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White,
                indicatorColor = Color.Black.copy(alpha = 0.2f)
            ),
            modifier = Modifier.padding(top = 8.dp)
        )

        // Controls Tab
        NavigationBarItem(
            selected = currentRoute == "screen2", // Check if screen2 is selected
            onClick = { navController.navigate("screen2") },
            label = { Text("Controls", color = Color.White) },
            icon = {
                Icon(
                    painter = if (currentRoute == "screen2") painterResource(id = play_filled) else painterResource(id = play_icon),
                    modifier = Modifier.size(25.dp),
                    contentDescription = "Controls",
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White,
                indicatorColor = Color.Black.copy(alpha = 0.2f)
            ),
            modifier = Modifier.padding(top = 8.dp)
        )

        // Queue Tab
        NavigationBarItem(
            selected = currentRoute == "screen3", // Check if screen3 is selected
            onClick = { navController.navigate("screen3") },
            label = { Text("Queue", color = Color.White) },
            icon = {
                Icon(
                    painter = if (currentRoute == "screen3") painterResource(id = list_fill) else painterResource(id = list_icon),
                    modifier = Modifier.size(25.dp),
                    contentDescription = "Queue",
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White,
                indicatorColor = Color.Black.copy(alpha = 0.2f)
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}




