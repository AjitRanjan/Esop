package com.example.esop.login

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.esop.network.AppPreferences
import com.example.esop.network.Resource
import com.example.esop.ui.theme.dimens

enum class LoginType {
    AADHAAR,
    PASSWORD,
    MOBILEOTP
}

@SuppressLint("UnusedBoxWithConstraintsScope", "MissingPermission")
@Composable
fun LoginScreen(navController: NavHostController) {

    val dimens = MaterialTheme.dimens

    var loginType by rememberSaveable {
        mutableStateOf(LoginType.PASSWORD)
    }

    var mobile by rememberSaveable {
        mutableStateOf("")
    }

    var loginId by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var aadhaar by rememberSaveable {
        mutableStateOf("")
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    val viewModel: AuthViewModel = viewModel()

    val loginState by viewModel.loginState.collectAsState()

    var isNavigated by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val appPrefs = remember {
        AppPreferences(context)
    }

    // =========================
    // LOGIN RESPONSE
    // =========================

    LaunchedEffect(loginState) {

        when (loginState) {

            is Resource.Success -> {
                if (!isNavigated) {

                    isNavigated = true

                    navController.navigate("welcome") {

                        popUpTo("login") {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
                val response =
                    (loginState as Resource.Success<LoginResponse>).data

                if (response.message == "Login Successful") {

                    response.data?.let { user ->

                        appPrefs.saveUser(
                            UserDataStore(
                                id = user.id.toString(),
                                name = user.fullName,
                                email = user.email,
                                mobile = user.mobile,
                                designation = user.designation,
                                organization = user.organization,
                                isLoggedIn = true
                            )
                        )



                        if (!isNavigated) {

                            isNavigated = true

                            navController.navigate("welcome") {

                                popUpTo("login") {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        }
                    }


                }
            }

            else -> {}
        }
    }

    // =========================
    // UI
    // =========================

    BoxWithConstraints(

        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))

    ) {

        val scrollState = rememberScrollState()

        Column(

            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = dimens.screenPaddingHorizontal),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Spacer(modifier = Modifier.height(dimens.spaceXL))
            Spacer(modifier = Modifier.height(dimens.spaceXL))

            // =========================
            // LOGO
            // =========================

            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = Color(0xFF2563EB),
                modifier = Modifier.size(dimens.iconXL)
            )

            Spacer(modifier = Modifier.height(dimens.spaceM))

            Text(
                text = "eSOP",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2563EB)
            )

            Text(
                text = "Electronic Standard Operation Process",
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(dimens.spaceXL))

            Text(
                text = "Welcome Back",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Text(
                text = "Login to continue",
                fontSize = 15.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(dimens.spaceL))

            // =========================
            // AADHAAR LOGIN
            // =========================

            if (loginType == LoginType.AADHAAR) {

                OutlinedTextField(

                    value = aadhaar,

                    onValueChange = {

                        if (it.length <= 12 &&
                            it.all { ch -> ch.isDigit() }
                        ) {
                            aadhaar = it
                        }
                    },

                    label = {
                        Text("Aadhaar Number")
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(dimens.radiusM),

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }

            // =========================
            // MOBILE OTP LOGIN
            // =========================

            if (loginType == LoginType.MOBILEOTP) {

                OutlinedTextField(

                    value = mobile,

                    onValueChange = {

                        if (it.length <= 10 &&
                            it.all { ch -> ch.isDigit() }
                        ) {
                            mobile = it
                        }
                    },

                    label = {
                        Text("Mobile Number")
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(dimens.radiusM),

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }

            // =========================
            // PASSWORD LOGIN
            // =========================

            if (loginType == LoginType.PASSWORD) {

                OutlinedTextField(

                    value = loginId,

                    onValueChange = {
                        loginId = it
                    },

                    label = {
                        Text("Login ID")
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(dimens.radiusM)
                )

                Spacer(modifier = Modifier.height(dimens.spaceM))

                OutlinedTextField(

                    value = password,

                    onValueChange = {
                        password = it
                    },

                    label = {
                        Text("Password")
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(dimens.radiusM),

                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),

                    trailingIcon = {

                        IconButton(
                            onClick = {

                                passwordVisible =
                                    !passwordVisible
                            }
                        ) {

                            Icon(
                                imageVector =
                                    if (passwordVisible)
                                        Icons.Default.VisibilityOff
                                    else
                                        Icons.Default.Visibility,

                                contentDescription = null,

                                tint = Color(0xFF2563EB)
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(dimens.spaceS))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    Text(
                        text = "Forgot Password?",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2563EB)
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.spaceM))

            // =========================
            // ERROR
            // =========================

            if (error.isNotEmpty()) {

                Text(
                    text = error,
                    color = Color.Red
                )
            }

            Spacer(modifier = Modifier.height(dimens.spaceM))

            // =========================
            // SUBMIT BUTTON
            // =========================

            Button(

                onClick = {

                    error = ""

                    if (loginType == LoginType.AADHAAR) {

                        error =
                            if (aadhaar.length != 12)
                                "Aadhaar must be 12 digits"
                            else
                                "Login Success"

                    } else {

                        when {

                            loginId.isEmpty() -> {

                                error = "Enter Login ID"
                            }

                            password.isEmpty() -> {

                                error = "Enter Password"
                            }

                            else -> {

                                viewModel.login(
                                    loginId,
                                    password
                                )
                            }
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

            ) {

                if (loginState is Resource.Loading) {

                    CircularProgressIndicator(

                        modifier = Modifier.size(24.dp),

                        color = Color.White,

                        strokeWidth = 2.dp
                    )

                } else {

                    Text(
                        text = "Submit",
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.spaceM))

            // =========================
            // LOGIN BUTTONS
            // =========================

            LoginTypeButton(
                text = "Login With Aadhaar"
            ) {
                loginType = LoginType.AADHAAR
            }

            Spacer(modifier = Modifier.height(dimens.spaceS))

            OrDivider()

            Spacer(modifier = Modifier.height(dimens.spaceS))

            LoginTypeButton(
                text = "Login With Password"
            ) {
                loginType = LoginType.PASSWORD
            }

            Spacer(modifier = Modifier.height(dimens.spaceS))

            OrDivider()

            Spacer(modifier = Modifier.height(dimens.spaceS))

            LoginTypeButton(
                text = "Login With OTP"
            ) {
                loginType = LoginType.MOBILEOTP
            }

            Spacer(modifier = Modifier.height(dimens.spaceL))

            // =========================
            // REGISTER
            // =========================

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimens.spaceM),

                horizontalArrangement = Arrangement.Center,

                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "New user? ",
                    color = Color.Gray
                )

                Text(
                    text = "Register Now",
                    color = Color(0xFF2563EB),
                    fontWeight = FontWeight.Bold,

                    modifier = Modifier.clickable {

                        navController.navigate("signup")
                    }
                )
            }

            Spacer(modifier = Modifier.height(dimens.spaceXL))
        }
    }
}

@Composable
fun LoginTypeButton(
    text: String,
    onClick: () -> Unit
) {

    Button(

        onClick = onClick,

        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.buttonHeight),

        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF2563EB)
        ),

        border = BorderStroke(
            1.dp,
            Color(0xFF2563EB)
        ),

        shape = RoundedCornerShape(
            MaterialTheme.dimens.radiusM
        )

    ) {

        Text(text = text)
    }
}

@Composable
fun OrDivider() {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),

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
}


























//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.runtime.saveable.rememberSaveable
//
//
//import android.annotation.SuppressLint
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Shield
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.foundation.layout.BoxWithConstraints
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextAlign
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import com.example.esop.AadhaarMaskTransformation
//import com.example.esop.R
//import com.example.esop.network.AppPreferences
//import com.example.esop.network.Resource
//import com.example.esop.ui.theme.dimens
//
////// 🔥 Login Mode Enum
//
//
//enum class LoginType {
//    AADHAAR,
//    PASSWORD,
//    MOBILEOTP
//}
////
////// 🔥 Login Mode Enum
//@SuppressLint("UnusedBoxWithConstraintsScope", "MissingPermission")
//@Composable
//fun LoginScreen(navController: NavHostController) {
//
//    val dimens = MaterialTheme.dimens   // ✅ IMPORTANT
//
//    var loginType by rememberSaveable { mutableStateOf(LoginType.PASSWORD) }
//    var mobile by rememberSaveable { mutableStateOf("") }
//    var loginId by rememberSaveable { mutableStateOf("") }
//    var password by rememberSaveable { mutableStateOf("") }
//    var aadhaar by rememberSaveable { mutableStateOf("") }
//    var error by rememberSaveable { mutableStateOf("") }
//    val viewModel: AuthViewModel = viewModel()
//    val loginState by viewModel.loginState.collectAsState()
//    var isNavigated by remember { mutableStateOf(false) }
//    val scope = rememberCoroutineScope()
//
//    var showDialog by remember { mutableStateOf(false) }
//    var dialogMessage by remember { mutableStateOf("") }
//
//
//    val context = LocalContext.current
//    val appPrefs = remember { AppPreferences(context) }
//
//    LaunchedEffect(loginState) {
//        when (loginState) {
//
//            is Resource.Success -> {
//
//                val response =
//                    (loginState as Resource.Success<LoginResponse>).data
//
//                dialogMessage = response.message ?: "Login Successful"
//                showDialog = true
//                if (response.message == "Login Successful") {
//
//                    val user = response.data
//
//                    // ✅ 👉 YAHAN CALL KARNA HAI
//                    user?.let {
//                        appPrefs.saveUser(
//                            UserDataStore(
//                                id = user?.id.toString(),
//                                name = it.fullName,
//                                email = user.email,
//                                mobile = user.mobile,
//                                designation = user.designation,
//                                organization = user.organization,
//                                isLoggedIn = true
//                            )
//                        )
//                    }
//                    if (!isNavigated) {
//                        isNavigated = true
//                        navController.navigate("welcome") {
//                            popUpTo("login") { inclusive = true }
//                            launchSingleTop = true
//                        }
//                    }
//                }
//            }
//
//            is Resource.Error<*> -> {}
//            is Resource.Loading<*> -> {}
//            null -> {
//
//            }
//        }
//    }
//    Spacer(modifier = Modifier.height(dimens.spaceM))
//    BoxWithConstraints(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F7FA))
//    ) {
//        Spacer(modifier = Modifier.height(dimens.spaceM))
//        val screenWidth = maxWidth
//        val screenHeight = maxHeight
//        val context = LocalContext.current
//        Spacer(modifier = Modifier.height(dimens.spaceM))
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = dimens.screenPaddingHorizontal)
//                .verticalScroll(rememberScrollState()),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            Spacer(modifier = Modifier.height(dimens.spaceXL))
//            Spacer(modifier = Modifier.height(dimens.spaceXL))
//            Icon(
//                Icons.Default.Shield,
//                contentDescription = null,
//                tint = Color(0xFF2563EB),
//                modifier = Modifier.size(dimens.iconXL)
//            )
//
//
//            Text(
//                stringResource(R.string.esop),
//                fontSize = (screenWidth.value * 0.10).sp,
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFF2563EB)
//            )
//
//            Text(
//                text = stringResource(R.string.electronic_standard_operation_process),
//                textAlign = TextAlign.Center,
//                fontSize = 15.sp
//            )
//
//            Spacer(modifier = Modifier.height(dimens.spaceM))
//
//            Text(
//                stringResource(R.string.welcome_back),
//                fontWeight = FontWeight.Bold,
//                fontSize = 20.sp
//            )
//
//            Text(
//                stringResource(R.string.login_to_continue),
//                fontSize = 15.sp
//            )
//
//            Spacer(modifier = Modifier.height(dimens.spaceL))
//
//            when (loginType) {
//
//                LoginType.AADHAAR -> {
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
//                        shape = RoundedCornerShape(dimens.radiusM),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                    )
//                }
//                LoginType.MOBILEOTP -> {
//                    OutlinedTextField(
//                        value = mobile,
//                        onValueChange = {
//                            if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
//                                mobile = it
//                            }
//                        },
//                        label = { Text(stringResource(R.string.mobile_must_be_10_digits)) },
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(dimens.radiusM),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                    )
//                }
//                LoginType.PASSWORD -> {
//
//                    OutlinedTextField(
//                        value = loginId,
//                        onValueChange = {
////                            if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
////                                mobile = it
////                            }
//
////                            if (it.length <= 10) {
////                                loginId = it
////                            }
//
//                            if (it.isNotBlank()) {
//                                loginId = it
//                            }
//                        },
//                        label = { Text(stringResource(R.string.login_id)) },
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(dimens.radiusM),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
//                    )
//
//                    Spacer(modifier = Modifier.height(dimens.spaceM))
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
//                        shape = RoundedCornerShape(dimens.radiusM)
//                    )
//                }
//            }
//
//            if (loginType == LoginType.PASSWORD) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    Text(
//                        text = stringResource(R.string.forgot_password),
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF2563EB)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(dimens.spaceM))
//
//            if (error.isNotEmpty()) {
//                Text(error, color = Color.Red)
//            }
//
//            Spacer(modifier = Modifier.height(dimens.spaceM))
//
//            val buttonText = when (loginType) {
//                LoginType.PASSWORD -> stringResource(R.string.submit)
//                LoginType.AADHAAR ->stringResource(R.string.submit)
//                LoginType.MOBILEOTP -> stringResource(R.string.submit)
//            }
//
//
//            Button(
//                onClick = {
//                    error = ""
//
//                    if (loginType == LoginType.AADHAAR) {
//                        error = if (aadhaar.length != 12)
//                            context.getString(R.string.aadhaar_must_be_12_digits)
//                        else
//                            context.getString(R.string.login_success_aadhaar)
//                    } else {
//                        error = when {
//                            loginId.isEmpty() ->
//                                context.getString(R.string.must_be_login)
//
//                            password.isEmpty() ->
//                                context.getString(R.string.enter_password)
//                                else -> viewModel.login(loginId, password)
//                        }.toString()
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(dimens.buttonHeight),
//                shape = RoundedCornerShape(dimens.radiusM),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF2563EB)
//                )
//            )
//
//
//            {
//                Text(buttonText)   // ✅ changed here
//            }
//
//            Spacer(modifier = Modifier.height(dimens.spaceM))
//
//            val primaryColor = Color(0xFF2563EB)
//
//            Button(
//                onClick = { loginType = LoginType.AADHAAR },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(dimens.buttonHeight),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.White,
//                    contentColor = primaryColor
//                ),
//                border = BorderStroke(1.dp, primaryColor),
//                shape = RoundedCornerShape(dimens.radiusM)
//            ) {
//                Text(stringResource(R.string.login_with_aadhaar))
////
////                Text(buttonText)   // ✅ changed here
//            }
//
//            Spacer(modifier = Modifier.height(dimens.spaceS))
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
//                Divider(
//                    modifier = Modifier.weight(1f),
//                    thickness = 1.dp,
//                    color = Color.Gray
//                )
//
//                Text(
//                    text = "OR",
//                    modifier = Modifier.padding(horizontal = 8.dp),
//                    color = Color.Gray
//                )
//
//                Divider(
//                    modifier = Modifier.weight(1f),
//                    thickness = 1.dp,
//                    color = Color.Gray
//                )
//            }
//            Button(
//                onClick = { loginType = LoginType.PASSWORD },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(dimens.buttonHeight),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.White,
//                    contentColor = primaryColor
//                ),
//                border = BorderStroke(1.dp, primaryColor),
//                shape = RoundedCornerShape(dimens.radiusM)
//            ) {
////                Text(buttonText)   // ✅ changed here
//                Text(stringResource(R.string.login_with_password))
//            }
//
//
//            Spacer(modifier = Modifier.height(dimens.spaceM))
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
//                Divider(
//                    modifier = Modifier.weight(1f),
//                    thickness = 1.dp,
//                    color = Color.Gray
//                )
//
//                Text(
//                    text = "OR",
//                    modifier = Modifier.padding(horizontal = 8.dp),
//                    color = Color.Gray
//                )
//
//                Divider(
//                    modifier = Modifier.weight(1f),
//                    thickness = 1.dp,
//                    color = Color.Gray
//                )
//            }
//            Button(
//                onClick = { loginType = LoginType.MOBILEOTP },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(dimens.buttonHeight),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.White,
//                    contentColor = primaryColor
//                ),
//                border = BorderStroke(1.dp, primaryColor),
//                shape = RoundedCornerShape(dimens.radiusM)
//            ) {
//                Text(stringResource(R.string.login_with_otp))
////                Text(buttonText)   // ✅ changed here
//            }
//            Spacer(modifier = Modifier.height(dimens.spaceM))
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 16.dp),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
//                Text(
//                    text = "New user? ",
//                    color = Color.Gray
//                )
//
//                Text(
//                    text = "Register Now",
//                    color = primaryColor,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.clickable {
//                        navController.navigate("signup")   // ✅ NAVIGATION HERE
//                    }
//                )
//            }
////            Row(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(vertical = 16.dp),
////                horizontalArrangement = Arrangement.Center,
////                verticalAlignment = Alignment.CenterVertically
////            )
////            {
////
////                Text(
////                    text = "New user? ",
////                    color = Color.Gray
////                )
////
////                Text(
////                    text = "Register Now",
////                    color = primaryColor,
////                    fontWeight = FontWeight.Bold
////                )
////            }
//        }
//    }
//
//
////    LaunchedEffect(loginState) {
////        when (loginState) {
////
////            is Resource.Success -> {
////                snackbarHostState.showSnackbar(
////                    "Login Successful"
////                )
////            }
////
////            is Resource.Error -> {
////                snackbarHostState.showSnackbar(
////                    (loginState as Resource.Error).message
////                        ?: "Something went wrong"
////                )
////            }
////
////            is Resource.Loading -> {
////                // optional loader
////            }
////
////            else -> {}
////        }
////    }
//}





































































//@SuppressLint("UnusedBoxWithConstraintsScope", "MissingPermission")
//@Composable
//fun LoginScreen() {
//
//    val dimens = MaterialTheme.dimens
//
//    var loginType by rememberSaveable { mutableStateOf(LoginType.PASSWORD) }
//    var mobile by rememberSaveable { mutableStateOf("") }
//    var loginId by rememberSaveable { mutableStateOf("") }
//    var password by rememberSaveable { mutableStateOf("") }
//    var aadhaar by rememberSaveable { mutableStateOf("") }
//    var error by rememberSaveable { mutableStateOf("") }
//
//    val viewModel: AuthViewModel = viewModel()
//
//    // ✅ Snackbar
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    // ✅ Observe state
//    val loginState by viewModel.loginState.collectAsState()
//
//    val context = LocalContext.current
//
//    // ✅ Observe API response
//    LaunchedEffect(loginState) {
//        when (loginState) {
//
//            is Resource.Success -> {
//                snackbarHostState.showSnackbar("Login Successful")
//            }
//
//            is Resource.Error -> {
//                snackbarHostState.showSnackbar(
//                    (loginState as Resource.Error).message
//                        ?: "Something went wrong"
//                )
//            }
//
//            is Resource.Loading -> {
//                // optional loader
//            }
//
//            else -> {}
//        }
//    }
//
//    // ✅ Root Box for Snackbar
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F7FA))
//    ) {
//
//        // ✅ Snackbar UI
//        SnackbarHost(
//            hostState = snackbarHostState,
//            modifier = Modifier.align(Alignment.BottomCenter)
//        )
//
//        BoxWithConstraints(
//            modifier = Modifier.fillMaxSize()
//        ) {
//
//            val screenWidth = maxWidth
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = dimens.screenPaddingHorizontal)
//                    .verticalScroll(rememberScrollState()),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//
//                Spacer(modifier = Modifier.height(dimens.spaceXL))
//                Spacer(modifier = Modifier.height(dimens.spaceXL))
//
//                Icon(
//                    Icons.Default.Shield,
//                    contentDescription = null,
//                    tint = Color(0xFF2563EB),
//                    modifier = Modifier.size(dimens.iconXL)
//                )
//
//                Text(
//                    stringResource(R.string.esop),
//                    fontSize = (screenWidth.value * 0.10).sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFF2563EB)
//                )
//
//                Text(
//                    text = stringResource(R.string.electronic_standard_operation_process),
//                    textAlign = TextAlign.Center,
//                    fontSize = 15.sp
//                )
//
//                Spacer(modifier = Modifier.height(dimens.spaceM))
//
//                Text(
//                    stringResource(R.string.welcome_back),
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 20.sp
//                )
//
//                Text(
//                    stringResource(R.string.login_to_continue),
//                    fontSize = 15.sp
//                )
//
//                Spacer(modifier = Modifier.height(dimens.spaceL))
//
//                // ================= INPUT =================
//                when (loginType) {
//
//                    LoginType.AADHAAR -> {
//                        OutlinedTextField(
//                            value = aadhaar,
//                            onValueChange = {
//                                if (it.length <= 12 && it.all { ch -> ch.isDigit() }) {
//                                    aadhaar = it
//                                }
//                            },
//                            label = { Text(stringResource(R.string.aadhaar_number)) },
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    }
//
//                    LoginType.MOBILEOTP -> {
//                        OutlinedTextField(
//                            value = mobile,
//                            onValueChange = {
//                                if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
//                                    mobile = it
//                                }
//                            },
//                            label = { Text(stringResource(R.string.mobile_must_be_10_digits)) },
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    }
//
//                    LoginType.PASSWORD -> {
//
//                        OutlinedTextField(
//                            value = loginId,
//                            onValueChange = {
//                                loginId = it
//                            },
//                            label = { Text(stringResource(R.string.login_id)) },
//                            modifier = Modifier.fillMaxWidth()
//                        )
//
//                        Spacer(modifier = Modifier.height(dimens.spaceM))
//
//                        OutlinedTextField(
//                            value = password,
//                            onValueChange = { password = it },
//                            label = { Text(stringResource(R.string.password)) },
//                            visualTransformation = PasswordVisualTransformation(),
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(dimens.spaceM))
//
//                if (error.isNotEmpty()) {
//                    Text(error, color = Color.Red)
//                }
//
//                Spacer(modifier = Modifier.height(dimens.spaceM))
//
//                val buttonText = stringResource(R.string.submit)
//
//                // ================= BUTTON =================
//                Button(
//                    onClick = {
//                        error = ""
//
//                        if (loginType == LoginType.AADHAAR) {
//
//                            error = if (aadhaar.length != 12)
//                                context.getString(R.string.aadhaar_must_be_12_digits)
//                            else
//                                "Aadhaar Valid"
//
//                        } else {
//
//                            when {
//                                loginId.isEmpty() -> {
//                                    error = context.getString(R.string.must_be_login)
//                                }
//
//                                password.isEmpty() -> {
//                                    error = context.getString(R.string.enter_password)
//                                }
//
//                                else -> {
//                                    viewModel.login(loginId, password)
//                                }
//                            }
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(dimens.buttonHeight)
//                ) {
//                    Text(buttonText)
//                }
//
//                Spacer(modifier = Modifier.height(dimens.spaceM))
//
//                // ================= SWITCH BUTTONS =================
//
//                Button(
//                    onClick = { loginType = LoginType.AADHAAR },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Login with Aadhaar")
//                }
//
//                Spacer(modifier = Modifier.height(dimens.spaceS))
//
//                Button(
//                    onClick = { loginType = LoginType.PASSWORD },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Login with Password")
//                }
//
//                Spacer(modifier = Modifier.height(dimens.spaceS))
//
//                Button(
//                    onClick = { loginType = LoginType.MOBILEOTP },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Login with OTP")
//                }
//
//                Spacer(modifier = Modifier.height(dimens.spaceM))
//            }
//        }
//    }
//}

//@Preview(
//    showBackground = true,
//    showSystemUi = true,
//    name = "Login Screen Preview"
//)
//@Composable
//fun LoginScreenPreview() {
//    LoginScreen(navController)
//}