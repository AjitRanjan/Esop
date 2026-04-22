package com.example.esop



import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ScreenConfig(
    val padding: Dp,
    val spacing: Dp
)

@Composable
fun getScreenConfig(
    screenWidth: Dp,
    screenHeight: Dp,
    screenWidthDp: Int
): ScreenConfig {

    val deviceType = when {
        screenWidthDp < 360 -> 0
        screenWidthDp < 600 -> 1
        screenWidthDp < 720 -> 2
        else -> 3
    }

    val padding = when (deviceType) {
        0 -> screenWidth * 0.05f
        1 -> screenWidth * 0.08f
        2 -> screenWidth * 0.10f
        else -> screenWidth * 0.15f
    }

    val spacing = when (deviceType) {
        0 -> screenHeight * 0.015f
        1 -> screenHeight * 0.02f
        2 -> screenHeight * 0.025f
        else -> screenHeight * 0.03f
    }

    return ScreenConfig(padding, spacing)
}
