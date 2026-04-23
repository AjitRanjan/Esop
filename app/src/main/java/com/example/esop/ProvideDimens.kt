package com.example.esop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import com.example.esop.ui.theme.CompactDimens
import com.example.esop.ui.theme.CompactSmallDimens
import com.example.esop.ui.theme.ExpandedDimens
import com.example.esop.ui.theme.MediumDimens

@Composable
fun ProvideDimens(content: @Composable () -> Unit) {

    val configuration = LocalConfiguration.current

    val dimens = when {
        configuration.screenWidthDp <= 360 -> CompactSmallDimens
        configuration.screenWidthDp < 600 -> CompactDimens
        configuration.screenWidthDp < 840 -> MediumDimens
        else -> ExpandedDimens
    }

    CompositionLocalProvider(
//        LocalDimens provides dimens,
        content = content
    )
}