package com.example.esop

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp


@Composable
fun LoginButton(screenHeight: Dp, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight * 0.07f),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2563EB)
        )
    ) {
        Text(stringResource(R.string.login))
    }
}