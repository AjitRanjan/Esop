package com.example.esop

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.esop.login.LoginScreen
import com.example.esop.network.AppPreferences
import com.example.esop.profile.CompleteProfileScreen
import com.example.esop.util.ImeiUtils
import faceembedding.TestScreen
import signup.SignupScreen


class MainActivity : ComponentActivity() {

    private lateinit var appPrefs: AppPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appPrefs = AppPreferences(this)

        // Device ID Fetch First
        val deviceId = ImeiUtils.getAndroidId(this)

        if (deviceId.isNotEmpty()) {

            loadApp(deviceId)

        } else {

            Toast.makeText(
                this,
                "Unable to get Device ID",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun loadApp(deviceId: String) {

        setContent {

            val navController = rememberNavController()

            val userMobile by appPrefs.mobile.collectAsState(initial = null)
            val userEmail by appPrefs.userEmail.collectAsState(initial = null)

            val startDestination =
                if (!userMobile.isNullOrEmpty() &&
                    !userEmail.isNullOrEmpty()
                ) {
                    "welcome"
                } else {
                    "login"
                }

            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {

                composable("login") {
                    LoginScreen(navController)
                }

                composable("signup") {
                    SignupScreen(navController)
                }

                composable("welcome") {
                    WelcomeScreen(navController)
                }

                composable("TestScreen") {
                    TestScreen()
                }

                composable("CompleteProfileScreen") {
                    CompleteProfileScreen(navController)
                }
            }
        }
    }
}