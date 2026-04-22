package com.example.esop




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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.tooling.preview.Preview

//// 🔥 Login Mode Enum
//enum class LoginType {
//    AADHAAR,
//    PASSWORD
//}
//
//@SuppressLint("UnusedBoxWithConstraintsScope")
//@Composable
//fun LoginScreen() {
//
//    var loginType by remember { mutableStateOf(LoginType.PASSWORD) }
//
//    var mobile by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var aadhaar by remember { mutableStateOf("") }
//
//    var error by remember { mutableStateOf("") }
//
//    BoxWithConstraints(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F7FA))
//    )
//    {
//
//        val screenWidth = maxWidth
//        val screenHeight = maxHeight
//        val context = LocalContext.current
//        val padding = screenWidth * 0.08f
//        val spacing = screenHeight * 0.02f
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = padding)
//                .verticalScroll(rememberScrollState()),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            Spacer(modifier = Modifier.height(screenHeight * 0.05f))
//
//            Icon(Icons.Default.Shield, null, tint = Color(0xFF2563EB),
//                modifier = Modifier.size(screenWidth * 0.15f))
//
//            Text(
//                stringResource(R.string.esop), fontSize = (screenWidth.value * 0.10).sp,
//                fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
//
//
//            Text(
//                text = stringResource(R.string.electronic_standard_operation_process),
//                textAlign = TextAlign.Center,
//                color = Color.Black,
//                fontSize = 15.sp
//            )
//
//
////            Spacer(modifier = Modifier.height(screenHeight * 0.05f))
//            Spacer(modifier = Modifier.height(spacing))
//
//            Text(
//                stringResource(R.string.welcome_back),
//                fontWeight = FontWeight.Bold,
//                fontSize = 20.sp,
//                textAlign = TextAlign.Center)
//
//
//            Text(
//                stringResource(R.string.login_to_continue),
//                fontSize = 15.sp,
//                textAlign = TextAlign.Center)
//
//            Spacer(modifier = Modifier.height(spacing * 2))
//
//
//
//            Spacer(modifier = Modifier.height(spacing * 2))
//
//            // 🔥 Dynamic Fields
//            when (loginType) {
//
//                LoginType.AADHAAR -> {
//
//                    OutlinedTextField(
//                        value = aadhaar,
//                        onValueChange = {
//                            if (it.length <= 12 && it.all { ch -> ch.isDigit() }) {
//                                aadhaar = it
//                            }
//                        },
//                        label = { Text(stringResource(R.string.aadhaar_number)) },
//                        visualTransformation = AadhaarMaskTransformation(),
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp)
//                    )
//                }
//
//                LoginType.PASSWORD -> {
//
//                    OutlinedTextField(
//                        value = mobile,
//                        onValueChange = {
//                            if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
//                                mobile = it
//                            }
//                        },
//                        label = { Text(stringResource(R.string.mobile_number)) },
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp)
//                    )
//
//                    Spacer(modifier = Modifier.height(spacing))
//
//                    OutlinedTextField(
//                        value = password,
//                        onValueChange = { password = it },
//                        label = { Text(stringResource(R.string.password)) },
//                        visualTransformation = PasswordVisualTransformation(),
//                        trailingIcon = {
//                            Icon(Icons.Default.Visibility, null)
//                        },
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp)
//                    )
//                }
//            }
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.End   // 🔥 Right align
//            ) {
//                Text(
//                    text = stringResource(R.string.forgot_password),
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 16.sp,
//                    color = Color(0xFF2563EB)
//                )
//            }
//            Spacer(modifier = Modifier.height(spacing))
//
//
//
//            // ❌ Error Message
//            if (error.isNotEmpty()) {
//                Text(error, color = Color.Red)
//            }
//            Spacer(modifier = Modifier.height(spacing))
//
//            // 🔘 Login Button
//            Button(
//                onClick = {
//
//                    error = ""
//
//                    if (loginType == LoginType.AADHAAR) {
//                        if (aadhaar.length != 12) {
//                            error = context.getString(R.string.aadhaar_must_be_12_digits)
//                        } else {
//                            error = context.getString(R.string.login_success_aadhaar)
//                        }
//                    } else {
//                        if (mobile.length != 10) {
//                            error = context.getString(R.string.mobile_must_be_10_digits)
//                        } else if (password.isEmpty()) {
//                            error = context.getString(R.string.enter_password)
//                        } else {
//                            error = "Login Success (Password)"
//                        }
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(screenHeight * 0.07f),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF2563EB)   // 🔥 Only this added
//                )
//            ) {
//                Text(stringResource(R.string.login))
//            }
//
//            Spacer(modifier = Modifier.height(spacing))
//            Spacer(modifier = Modifier.height(spacing))
//
//            // 🔘 Toggle Buttons
//
//
//
//            val primaryColor = Color(0xFF2563EB)
//
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//
//                Button(
//                    onClick = { loginType = LoginType.AADHAAR },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(screenHeight * 0.07f),   // 🔥 Dynamic height
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.White,
//                        contentColor = primaryColor
//                    ),
//                    border = BorderStroke(1.dp, primaryColor),
//                    elevation = ButtonDefaults.buttonElevation(0.dp),
//                    shape = RoundedCornerShape(12.dp)   // 🔥 Rounded added
//                ) {
//                    Text(stringResource(R.string.login_with_aadhaar))
//                }
//
//                Spacer(modifier = Modifier.height(10.dp))
//
//                Button(
//                    onClick = { loginType = LoginType.PASSWORD },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(screenHeight * 0.07f),   // 🔥 Same height
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.White,
//                        contentColor = primaryColor
//                    ),
//                    border = BorderStroke(1.dp, primaryColor),
//                    elevation = ButtonDefaults.buttonElevation(0.dp),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Text(stringResource(R.string.login_with_password))
//                }
//            }
//
//
//
//            Spacer(modifier = Modifier.height(spacing))
//        }
//    }
//}






// 🔥 Login Mode Enum
enum class LoginType {
    AADHAAR,
    PASSWORD
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun LoginScreen() {

    // ✅ Rotation Fix
    var loginType by rememberSaveable { mutableStateOf(LoginType.PASSWORD) }
    var mobile by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var aadhaar by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf("") }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {

        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val context = LocalContext.current

        // ✅ Screen size logic (external function)
        val configuration = LocalConfiguration.current

        val screenConfig = getScreenConfig(
            screenWidth = screenWidth,
            screenHeight = screenHeight,
            screenWidthDp = configuration.screenWidthDp
        )

        val padding = screenConfig.padding
        val spacing = screenConfig.spacing

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(screenHeight * 0.05f))

            Icon(
                Icons.Default.Shield,
                null,
                tint = Color(0xFF2563EB),
                modifier = Modifier.size(screenWidth * 0.15f)
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
                color = Color.Black,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(spacing))

            Text(
                stringResource(R.string.welcome_back),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            Text(
                stringResource(R.string.login_to_continue),
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(spacing * 2))

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
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number   // 🔥 Numeric keyboard
                        )
                    )
                }

                LoginType.PASSWORD -> {

                    OutlinedTextField(
                        value = mobile,
                        onValueChange = {
                            if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
                                mobile = it
                            }
                        },
                        label = { Text(stringResource(R.string.mobile_number)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number   // 🔥 Numeric keyboard
                        )
                    )

                    Spacer(modifier = Modifier.height(spacing))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(R.string.password)) },
                        visualTransformation = PasswordVisualTransformation(),
                        trailingIcon = {
                            Icon(Icons.Default.Visibility, null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
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
                        fontSize = 16.sp,
                        color = Color(0xFF2563EB)
                    )
                }
            }
            Spacer(modifier = Modifier.height(spacing))

            if (error.isNotEmpty()) {
                Text(error, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(spacing))

            Button(
                onClick = {

                    error = ""

                    if (loginType == LoginType.AADHAAR) {
                        if (aadhaar.length != 12) {
                            error = context.getString(R.string.aadhaar_must_be_12_digits)
                        } else {
                            error = context.getString(R.string.login_success_aadhaar)
                        }
                    } else {
                        if (mobile.length != 10) {
                            error = context.getString(R.string.mobile_must_be_10_digits)
                        } else if (password.isEmpty()) {
                            error = context.getString(R.string.enter_password)
                        } else {
                            error = "Login Success (Password)"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.07f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB)
                )
            ) {
                Text(stringResource(R.string.login))
            }

            Spacer(modifier = Modifier.height(spacing))
            Spacer(modifier = Modifier.height(spacing))

            val primaryColor = Color(0xFF2563EB)

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    onClick = { loginType = LoginType.AADHAAR },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.07f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = primaryColor
                    ),
                    border = BorderStroke(1.dp, primaryColor),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.login_with_aadhaar))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { loginType = LoginType.PASSWORD },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.07f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = primaryColor
                    ),
                    border = BorderStroke(1.dp, primaryColor),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.login_with_password))
                }
            }

            Spacer(modifier = Modifier.height(spacing))
        }
    }
}



@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Login Screen Preview"
)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
//
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




//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.foundation.layout.BoxWithConstraints

//@Composable
//fun LoginScreen() {
//
//    var mobile by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//
//    BoxWithConstraints(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F7FA))
//    ) {
//
//        val screenWidth = maxWidth
//        val screenHeight = maxHeight
//
//        val horizontalPadding = screenWidth * 0.08f
//        val verticalSpacing = screenHeight * 0.02f
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = horizontalPadding)
//                .verticalScroll(rememberScrollState()),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            Spacer(modifier = Modifier.height(screenHeight * 0.05f))
//
//            Icon(
//                imageVector = Icons.Default.Shield,
//                contentDescription = null,
//                tint = Color(0xFF2563EB),
//                modifier = Modifier.size(screenWidth * 0.15f)
//            )
//
//            Spacer(modifier = Modifier.height(verticalSpacing))
//
//            Text(
//                text = "eSOP",
//                fontSize = (screenWidth.value * 0.08).sp,
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFF2563EB)
//            )
//
//            Text(
//                text = "Electronic Standard\nOperation Process",
//                fontSize = (screenWidth.value * 0.035).sp,
//                textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(verticalSpacing * 2))
//
//            Text(
//                text = "Welcome Back!",
//                fontSize = (screenWidth.value * 0.05).sp,
//                fontWeight = FontWeight.SemiBold
//            )
//
//            Text(
//                text = "Login to continue",
//                fontSize = (screenWidth.value * 0.035).sp,
//                color = Color.Gray
//            )
//
//            Spacer(modifier = Modifier.height(verticalSpacing * 2))
//
//            OutlinedTextField(
//                value = mobile,
//                onValueChange = { mobile = it },
//                label = { Text("Mobile Number") },
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp)
//            )
//
//            Spacer(modifier = Modifier.height(verticalSpacing))
//
//            OutlinedTextField(
//                value = password,
//                onValueChange = { password = it },
//                label = { Text("Password") },
//                visualTransformation = PasswordVisualTransformation(),
//                trailingIcon = {
//                    Icon(Icons.Default.Visibility, contentDescription = "")
//                },
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp)
//            )
//
//            Spacer(modifier = Modifier.height(verticalSpacing))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.End
//            ) {
//                Text(
//                    text = "Forgot Password?",
//                    color = Color(0xFF2563EB),
//                    fontSize = (screenWidth.value * 0.032).sp
//                )
//            }
//
//            Spacer(modifier = Modifier.height(verticalSpacing))
//
//            Button(
//                onClick = {
//                    // TODO: Login logic
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(screenHeight * 0.07f),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Text("Login")
//            }
//
//            Spacer(modifier = Modifier.height(verticalSpacing))
//
//            Text("or", color = Color.Gray)
//
//            Spacer(modifier = Modifier.height(verticalSpacing))
//
//            OutlinedButton(
//                onClick = {
//                    // TODO: OTP Login
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(screenHeight * 0.07f),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Text("Login with OTP")
//            }
//
//            Spacer(modifier = Modifier.height(verticalSpacing))
//
//            Row {
//                Text("New user? ")
//                Text(
//                    text = "Register Now",
//                    color = Color(0xFF2563EB),
//                    fontWeight = FontWeight.Bold
//                )
//            }
//
//            Spacer(modifier = Modifier.height(verticalSpacing * 2))
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewLoginScreen() {
//    LoginScreen()
//}