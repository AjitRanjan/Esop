package com.example.esop.headerSelection



import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.esop.R
import com.example.esop.ui.theme.dimens


@Composable
fun HeaderSection() {

    val dimens = MaterialTheme.dimens

    Spacer(modifier = Modifier.height(dimens.space2XL))

    Icon(
        Icons.Default.Shield,
        contentDescription = null,
        tint = Color(0xFF2563EB),
        modifier = Modifier.size(dimens.iconXL)   // ✅ from Dimens
    )

    Text(
        text = stringResource(R.string.esop),
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF2563EB)
    )

    Text(
        text = stringResource(R.string.electronic_standard_operation_process),
        textAlign = TextAlign.Center
    )
}

//@Composable
//fun HeaderSection(screenWidth: Dp, screenHeight: Dp) {
//
//    Spacer(modifier = Modifier.height(screenHeight * 0.05f))
//
//    Icon(
//        Icons.Default.Shield,
//        null,
//        tint = Color(0xFF2563EB),
//        modifier = Modifier.size(screenWidth * 0.15f)
//    )
//
//    Text(
//        stringResource(R.string.esop),
//        fontSize = (screenWidth.value * 0.10).sp,
//        fontWeight = FontWeight.Bold,
//        color = Color(0xFF2563EB)
//    )
//
//    Text(
//        text = stringResource(R.string.electronic_standard_operation_process),
//        textAlign = TextAlign.Center
//    )
//}