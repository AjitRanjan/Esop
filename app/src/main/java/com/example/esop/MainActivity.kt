package com.example.esop

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.esop.homepage.HomePageScreen
import com.example.esop.login.LoginScreen
import com.example.esop.mytest.MyTest
import com.example.esop.network.AppPreferences
import com.example.esop.profile.CompleteProfileScreen
import com.example.esop.quetions_esop.Question
import com.example.esop.quetions_esop.TestScreen
import com.example.esop.util.ImeiUtils
import com.example.esop.CertificateScreen.ESOPCertificateScreen
import com.example.esop.ResultScreen.ESOPResultScreen
import faceembedding.TestInstructionsScreen
import signup.SignupScreen

class MainActivity : ComponentActivity() {

    private lateinit var appPrefs: AppPreferences


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        WindowInsetsControllerCompat(
            window,
            window.decorView
        ).let { controller ->

            controller.hide(
                WindowInsetsCompat.Type.systemBars()
            )

            controller.systemBarsBehavior =
                WindowInsetsControllerCompat
                    .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadApp(deviceId: String) {

        setContent {

            var questionList by remember {
                mutableStateOf<List<Question>>(emptyList())
            }
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
                    HomePageScreen(navController)
                }

//                composable("TestScreen") {
//                    TestScreen(navController)
//                }



                composable(
                    route = "TestScreen/{category}/{certytype}/{paacategory}"
                ) { backStackEntry ->

                    val category = backStackEntry.arguments?.getString("category") ?: ""

                    val certytype = backStackEntry.arguments?.getString("certytype") ?: ""
                    val paacategory = backStackEntry.arguments?.getString("paacategory") ?: ""

                    TestScreen(
                        navController = navController,
                        category = category,
                        certytype = certytype,
                        paacategory = paacategory
                    )
                }
//                composable("TestInstructionsScreen") {
//
//                    TestInstructionsScreen(
//                        navController = navController,
//                        appPreferences = appPrefs
//                    )
//
//                }
//                composable(
////                    route = "TestInstructionsScreen/{processgroup}"
//                        route = "TestInstructionsScreen/{processgroup}/{department}"
//                ) { backStackEntry ->
//
//                    val processgroup = backStackEntry.arguments?.getString("processgroup") ?: ""
//                    val department = backStackEntry.arguments?.getString("department") ?: ""
//
//                    TestInstructionsScreen(
//                        navController = navController,
//                        appPreferences = appPrefs,
//                        processgroup = processgroup,
//                        department = department
//                    )
//                }
                composable(
                    route = "TestInstructionsScreen/{processgroup}/{department}"
                ) { backStackEntry ->

                    val processgroup =
                        backStackEntry.arguments?.getString("processgroup") ?: ""

                    val department =
                        backStackEntry.arguments?.getString("department") ?: ""

                    TestInstructionsScreen(
                        navController = navController,
                        appPreferences = appPrefs,
                        processgroup = processgroup,
                        department = department
                    )
                }



                composable("ESOPResultScreen") {
                    ESOPResultScreen(
                        navController = navController,
                        appPreferences = appPrefs
                    )
                }


                composable("ESOPCertificateScreen") {
                    ESOPCertificateScreen(
                        navController = navController,
                        appPreferences = appPrefs
                    )
                }


//                composable("ESOPCertificateScreen") {
//
//
//                    ESOPCertificateScreen(
//                        navController = navController
//                    )
//                }


                composable("CompleteProfileScreen") {


                    CompleteProfileScreen(
                        navController = navController
                    )
                }


//                composable(
//                    route = "MyTestScreen/{loginId}/{emailId}"
//                ) { backStackEntry ->
//
//                    val loginId =
//                        backStackEntry.arguments?.getString("loginId") ?: ""
//
//                    val emailId =
//                        backStackEntry.arguments?.getString("emailId") ?: ""
//
//                    val resultViewModel: ResultViewModel = viewModel()
//
//                    MyTestScreen(
//                        navController = navController,
//                        viewModel = resultViewModel,
//                        loginId = loginId,
//                        userEmail = emailId
//                    )
//                }



                composable("MyTest") {
                    MyTest(
                        navController = navController,
                        appPreferences = appPrefs
                    )
                }




                

                
            }
        }
    }
}