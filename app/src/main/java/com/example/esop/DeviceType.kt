package com.example.esop



import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

enum class DeviceType {
    SMALL_PHONE,
    NORMAL_PHONE,
    LARGE_PHONE,
    TABLET
}

@Composable
fun getDeviceType(): DeviceType {






    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    return when {
        screenWidthDp < 360 -> DeviceType.SMALL_PHONE      // ~4 inch
        screenWidthDp < 600 -> DeviceType.NORMAL_PHONE     // ~6 inch
        screenWidthDp < 720 -> DeviceType.LARGE_PHONE      // ~7 inch
        else -> DeviceType.TABLET
    }
}






