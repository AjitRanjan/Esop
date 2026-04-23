package com.example.esop
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview


import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.esop.ui.theme.dimens

//// 🔥 Login Mode Enum


enum class LoginType {
    AADHAAR,
    PASSWORD,
    MOBILEOTP
}

// 🔥 Login Mode Enum
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun LoginScreen() {

    val dimens = MaterialTheme.dimens   // ✅ IMPORTANT

    var loginType by rememberSaveable { mutableStateOf(LoginType.PASSWORD) }
    var mobile by rememberSaveable { mutableStateOf("") }
    var loginId by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var aadhaar by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf("") }
    Spacer(modifier = Modifier.height(dimens.spaceM))
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        Spacer(modifier = Modifier.height(dimens.spaceM))
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val context = LocalContext.current
        Spacer(modifier = Modifier.height(dimens.spaceM))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimens.screenPaddingHorizontal)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(dimens.spaceXL))
            Spacer(modifier = Modifier.height(dimens.spaceXL))
//            Spacer(modifier = Modifier.height(dimens.spaceM))
            Icon(
                Icons.Default.Shield,
                contentDescription = null,
                tint = Color(0xFF2563EB),
                modifier = Modifier.size(dimens.iconXL)
            )


            Text(
                stringResource(R.string.esop),
                fontSize = (screenWidth.value * 0.10).sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2563EB)
            )

            Text(
                text = stringResource(R.string.electronic_standard_operation_process),
                textAlign = TextAlign.Center,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(dimens.spaceM))

            Text(
                stringResource(R.string.welcome_back),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Text(
                stringResource(R.string.login_to_continue),
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(dimens.spaceL))

            when (loginType) {

                LoginType.AADHAAR -> {
                    OutlinedTextField(
                        value = aadhaar,
                        onValueChange = {
                            if (it.length <= 12 && it.all { ch -> ch.isDigit() }) {
                                aadhaar = it
                            }
                        },
                        label = { Text(stringResource(R.string.aadhaar_number)) },
                        visualTransformation = AadhaarMaskTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(dimens.radiusM),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                LoginType.MOBILEOTP -> {
                    OutlinedTextField(
                        value = mobile,
                        onValueChange = {
                            if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
                                mobile = it
                            }
                        },
                        label = { Text(stringResource(R.string.mobile_must_be_10_digits)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(dimens.radiusM),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                LoginType.PASSWORD -> {

                    OutlinedTextField(
                        value = loginId,
                        onValueChange = {
//                            if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
//                                mobile = it
//                            }

                            if (it.length <= 10) {
                                loginId = it
                            }

                        },
                        label = { Text(stringResource(R.string.login_id)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(dimens.radiusM),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )

                    Spacer(modifier = Modifier.height(dimens.spaceM))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(R.string.password)) },
                        visualTransformation = PasswordVisualTransformation(),
                        trailingIcon = {
                            Icon(Icons.Default.Visibility, null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(dimens.radiusM)
                    )
                }
            }

            if (loginType == LoginType.PASSWORD) {
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

            Spacer(modifier = Modifier.height(dimens.spaceM))

            if (error.isNotEmpty()) {
                Text(error, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(dimens.spaceM))

            val buttonText = when (loginType) {
                LoginType.PASSWORD -> stringResource(R.string.submit)
                LoginType.AADHAAR ->stringResource(R.string.submit)
                LoginType.MOBILEOTP -> stringResource(R.string.submit)
            }


            Button(
                onClick = {
                    error = ""

                    if (loginType == LoginType.AADHAAR) {
                        error = if (aadhaar.length != 12)
                            context.getString(R.string.aadhaar_must_be_12_digits)
                        else
                            context.getString(R.string.login_success_aadhaar)
                    } else {
                        error = when {
//                            loginId.length != 10 ->
                            loginId.isEmpty() ->
                                context.getString(R.string.must_be_login)
                            password.isEmpty() ->
                                context.getString(R.string.enter_password)
                            else -> "Login Success (Password)"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.buttonHeight),
                shape = RoundedCornerShape(dimens.radiusM),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB)
                )
            )


            {
//                Text(stringResource(R.string.login))
                Text(buttonText)   // ✅ changed here
            }

            Spacer(modifier = Modifier.height(dimens.spaceM))

            val primaryColor = Color(0xFF2563EB)

            Button(
                onClick = { loginType = LoginType.AADHAAR },
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
                Text(stringResource(R.string.login_with_aadhaar))
//
//                Text(buttonText)   // ✅ changed here
            }

            Spacer(modifier = Modifier.height(dimens.spaceS))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Divider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.Gray
                )

                Text(
                    text = "OR",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color.Gray
                )

                Divider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.Gray
                )
            }
            Button(
                onClick = { loginType = LoginType.PASSWORD },
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
//                Text(buttonText)   // ✅ changed here
                Text(stringResource(R.string.login_with_password))
            }


            Spacer(modifier = Modifier.height(dimens.spaceM))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Divider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.Gray
                )

                Text(
                    text = "OR",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color.Gray
                )

                Divider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.Gray
                )
            }
            Button(
                onClick = { loginType = LoginType.MOBILEOTP },
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
                Text(stringResource(R.string.login_with_otp))
//                Text(buttonText)   // ✅ changed here
            }
            Spacer(modifier = Modifier.height(dimens.spaceM))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "New user? ",
                    color = Color.Gray
                )

                Text(
                    text = "Register Now",
                    color = primaryColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}




//@Composable
//fun LoginScreen() {
//
////    val dimens = LocalDimens.current   // ✅ USE THIS
//    val dimens = MaterialTheme.dimens
//    var loginType by rememberSaveable { mutableStateOf(LoginType.PASSWORD) }
//    var mobile by rememberSaveable { mutableStateOf("") }
//    var password by rememberSaveable { mutableStateOf("") }
//    var aadhaar by rememberSaveable { mutableStateOf("") }
//
//    var error by rememberSaveable { mutableStateOf("") }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F7FA))
//    ) {
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(
//                    horizontal = dimens.screenPaddingHorizontal,
//                    vertical = dimens.screenPaddingVertical
//                )
//                .verticalScroll(rememberScrollState()),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            HeaderSection()
//
//            Spacer(modifier = Modifier.height(dimens.spaceXL))
//
//            LoginInputSection(
//                loginType = loginType,
//                mobile = mobile,
//                password = password,
//                aadhaar = aadhaar,
//                spacing = dimens.spaceM,
//                onMobileChange = { mobile = it },
//                onPasswordChange = { password = it },
//                onAadhaarChange = { aadhaar = it }
//            )
//
//            Spacer(modifier = Modifier.height(dimens.spaceL))
//
//            SwitchLoginButtons(
//                loginType = loginType,
//                onSwitch = { loginType = it }
////                onOtp = { loginType = it }
//            )
//        }
//    }
//}



//@Preview(
//    name = "Small Phone",
//    device = "spec:width=360dp,height=640dp,dpi=320",
//    showBackground = true
//)
//@Composable
//fun PreviewSmallPhone() {
//    LoginScreen()
//}
//
//@Preview(
//    name = "Large Phone",
//    device = "spec:width=411dp,height=891dp,dpi=420",
//    showBackground = true
//)
//@Composable
//fun PreviewLargePhone() {
//    LoginScreen()
//}
//
//@Preview(
//    name = "Tablet",
//    device = "spec:width=800dp,height=1280dp,dpi=240",
//    showBackground = true
//)
//@Composable
//fun PreviewTablet() {
//    LoginScreen()
//}
//@Preview(showBackground = true)
//@Composable
//fun PreviewLoginScreen() {
//    LoginScreen()
//}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Login Screen Preview"
)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}