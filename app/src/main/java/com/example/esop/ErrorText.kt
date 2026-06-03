package com.example.esop

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp


@Composable
fun ErrorText(error: String, spacing: Dp) {
    if (error.isNotEmpty()) {
        Spacer(modifier = Modifier.height(spacing))
        Text(error, color = Color.Red)
    }
}