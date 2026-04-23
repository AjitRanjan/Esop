package com.example.esop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp


@Composable
fun ForgotPassword(loginType: LoginType, spacing: Dp) {
    if (loginType == LoginType.PASSWORD) {
        Spacer(modifier = Modifier.height(spacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = stringResource(R.string.forgot_password),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2563EB)
            )
        }
    }
}