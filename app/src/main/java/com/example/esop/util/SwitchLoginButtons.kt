package com.example.esop.util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.esop.R
import com.example.esop.login.LoginType
import com.example.esop.ui.theme.dimens

@Composable
fun SwitchLoginButtons(
    loginType: LoginType,
    onSwitch: (LoginType) -> Unit,
//    onOtp: (LoginType) -> Unit,

) {


    val dimens = MaterialTheme.dimens
    val primaryColor = Color(0xFF2563EB)

    Button(
        onClick = { onSwitch(LoginType.AADHAAR) },
        modifier = Modifier
            .fillMaxWidth()
            .height(dimens.buttonHeight),   // ✅ from Dimens
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = primaryColor
        ),
        border = BorderStroke(1.dp, primaryColor),
        shape = RoundedCornerShape(dimens.radiusM)
    ) {
        Text(stringResource(R.string.login_with_aadhaar))
    }

    Spacer(modifier = Modifier.height(dimens.spaceS))

    Button(
        onClick = { onSwitch(LoginType.PASSWORD) },
        modifier = Modifier
            .fillMaxWidth()
            .height(dimens.buttonHeight),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = primaryColor
        ),
        border = BorderStroke(1.dp, primaryColor),
        shape = RoundedCornerShape(dimens.radiusM)
    ) {
        Text(stringResource(R.string.login_with_password))
    }


//    Spacer(modifier = Modifier.height(dimens.spaceS))
//    Button(
//        onClick = { onSwitch(LoginType.MOBILEOTP) },
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(dimens.buttonHeight),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color.White,
//            contentColor = primaryColor
//        ),
//        border = BorderStroke(1.dp, primaryColor),
//        shape = RoundedCornerShape(dimens.radiusM)
//    ) {
//        Text(stringResource(R.string.login_with_otp))
//    }

}


//@Preview(showBackground = true)
//@Composable
//fun SwitchLoginButtonsPreview() {
//
//    // Provide Dimens manually for preview
//    CompositionLocalProvider(
////        LocalDimens provides CompactDimens
//    ) {
//
//        MaterialTheme {
//
//            SwitchLoginButtons(
//                loginType = LoginType.PASSWORD,
//                onSwitch = {}
//            )
//        }
//    }
//}

//@Composable
//fun SwitchLoginButtons(
//    screenHeight: Dp,
//    loginType: LoginType,
//    onSwitch: (LoginType) -> Unit
//) {
//
//    val primaryColor = Color(0xFF2563EB)
//
//    Spacer(modifier = Modifier.height(16.dp))
//
//    Button(
//        onClick = { onSwitch(LoginType.AADHAAR) },
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(screenHeight * 0.07f),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color.White,
//            contentColor = primaryColor
//        ),
//        border = BorderStroke(1.dp, primaryColor)
//    ) {
//        Text(stringResource(R.string.login_with_aadhaar))
//    }
//
//    Spacer(modifier = Modifier.height(10.dp))
//
//    Button(
//        onClick = { onSwitch(LoginType.PASSWORD) },
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(screenHeight * 0.07f),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color.White,
//            contentColor = primaryColor
//        ),
//        border = BorderStroke(1.dp, primaryColor)
//    ) {
//        Text(stringResource(R.string.login_with_password))
//    }
//}