package com.example.esop.loginSelection

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.example.esop.LoginType
import com.example.esop.PasswordField
import com.example.esop.aadharField.AadhaarField
import com.example.esop.mobileField.MobileField

@Composable
fun LoginInputSection(
    loginType: LoginType,
    mobile: String,
    password: String,
    aadhaar: String,
    spacing: Dp,
    onMobileChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onAadhaarChange: (String) -> Unit,
//    onMobileOtp: (String) -> Unit
) {

    Spacer(modifier = Modifier.height(spacing * 2))

    when (loginType) {

        LoginType.AADHAAR -> {
            AadhaarField(aadhaar, onAadhaarChange)
        }

        LoginType.PASSWORD -> {
            MobileField(mobile, onMobileChange)
            Spacer(modifier = Modifier.height(spacing))
            PasswordField(password, onPasswordChange)
        }
        LoginType.MOBILEOTP -> {
            MobileField(mobile, onMobileChange)
        }
    }
}