package com.example.esop.login

import android.annotation.SuppressLint
import android.widget.Toast
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
import com.example.esop.token.GetToken
import com.example.esop.token.TokenViewModel
import com.example.esop.ui.theme.dimens
import com.example.esop.util.ImeiUtils

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

    var loginId by rememberSaveable { mutableStateOf("") }
    var authToken by rememberSaveable { mutableStateOf("") }

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
    var loginApiCalled by remember { mutableStateOf(false) }
    var tokenRequestStarted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel()

    val loginState by viewModel.loginState.collectAsState()

    var isNavigated by remember {
        mutableStateOf(false)
    }
    val viewModelToken: TokenViewModel = viewModel()

    val tokenState by viewModelToken.tokenState.collectAsState()



    val versionName = remember {
        context.packageManager
            .getPackageInfo(context.packageName, 0)
            .versionName
    }


    val appPrefs = remember {
        AppPreferences(context)
    }
    val deviceId = ImeiUtils.getAndroidId(context)
    LaunchedEffect(tokenState) {

        // Button click nahi hua to kuch mat karo
        if (!tokenRequestStarted) return@LaunchedEffect

        when (val state = tokenState) {

            is Resource.Success -> {

                val response = state.data

                if (response.responseDesc == "OK" && !loginApiCalled) {

                    loginApiCalled = true

                    authToken = response.authToken

                    viewModel.login(
                        loginId,
                        password
                    )
                }
            }

            is Resource.Error -> {

                val errorMessage = (tokenState as Resource.Error).message

                Toast.makeText(
                    context,
                    errorMessage,
                    Toast.LENGTH_LONG
                ).show()

                tokenRequestStarted = false
            }

            else -> {}
        }
    }




    LaunchedEffect(loginState) {

        val state = loginState

        when (state) {

            is Resource.Success -> {

                val response = state.data

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
                                processGroup = user.processGroup,
                                loginId = user.loginId,
                                usertype = user.usertype,
                                processGroupId = user.processGroupId.toInt(),
                                organizationId = user.organizationId.toInt(),
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

                } else {
                    Toast.makeText(
                        context,
                        response.message ?: "Login failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            is Resource.Error -> {

                Toast.makeText(
                    context,
                    state.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            is Resource.Loading -> {
                // Show Loader
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

                                tokenRequestStarted = true
                                loginApiCalled = false
                                viewModelToken.getToken(
                                    versionName.toString(),
                                    deviceId,
                                    ""

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
