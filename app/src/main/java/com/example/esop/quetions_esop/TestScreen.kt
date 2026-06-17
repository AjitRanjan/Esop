package com.example.esop.quetions_esop



import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.esop.network.AppPreferences
import com.example.esop.network.Resource
import com.example.esop.profile.CompletePofileScreenViewModel
import com.example.esop.profile.ProfileViewModel
import com.example.esop.profile.Repositry.UpdateProfileViewModel
import com.example.esop.vibrate.FaceVerificationUtils
import com.example.esop.vibrate.VibrateWhileDialogVisible
import kotlinx.coroutines.delay
import java.util.concurrent.Executors
// ...existing code...


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.esop.ui.theme.dimens
import com.example.esop.util.helper.LegendItem
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.example.esop.AswersOptionSubmit.Repositry.InsertExamState
import com.example.esop.AswersOptionSubmit.Repositry.InsertViewModel
import com.example.esop.AswersOptionSubmit.SubmitAnswer
import com.example.esop.AswersOptionSubmit.SubmitExamRequest
import com.example.esop.fialAnsweredSubmitApi.FinalInsertViewModel
import com.example.esop.fialAnsweredSubmitApi.ResultInsertReq
import faceembedding.Summary
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import kotlin.math.sqrt

@Composable
fun TestScreen(
    navController: NavController,
    questionViewModel: QuestionViewModel = viewModel() ,
    insertViewModel: InsertViewModel = viewModel(),
    fialinsertViewModel: FinalInsertViewModel = viewModel(),
    category : String,
    certytype : String,
    paacategory: String
) {

    val submitState = insertViewModel.state
    val finalsubmitState = fialinsertViewModel.state
    val questionState = questionViewModel.uiState
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    lateinit var appPrefs: AppPreferences
    val previewView = remember {
        PreviewView(context)
    }
    var showLoading by remember { mutableStateOf(false) }
    val cameraExecutor = remember {
        Executors.newSingleThreadExecutor()
    }

    // Blink tracking state per face (trackingId -> state)
    data class BlinkState(
        var eyesWereOpen: Boolean = false,
        var blinkDetected: Boolean = false,
        var imageCaptured: Boolean = false,
        var lastSeenMs: Long = System.currentTimeMillis()
    )

    val blinkStates = remember { mutableStateMapOf<Int, BlinkState>() }

    var imageCapture by remember {
        mutableStateOf<ImageCapture?>(null)
    }

    var processgroup by remember { mutableStateOf("") }
    var UserName by remember { mutableStateOf("") }
    var CanddidateId by remember { mutableStateOf("") }
    var usertypedesc by remember { mutableStateOf("") }
    var firstEmbedding by remember {
        mutableStateOf<FloatArray?>(null)
    }

    var resultText by remember { mutableStateOf("No Face Captured") }
    var questionCode by remember { mutableStateOf(2) }

    var showDialog by remember {
        mutableStateOf(false)
    }
    var totalQuestions by remember { mutableIntStateOf(0) }
    var easyCount by remember { mutableIntStateOf(0) }
    var mediumCount by remember { mutableIntStateOf(0) }
    var hardCount by remember { mutableIntStateOf(0) }
    var numberofAttempt by remember { mutableIntStateOf(0) }

    var easyPercentage by remember { mutableDoubleStateOf(0.0) }
    var mediumPercentage by remember { mutableDoubleStateOf(0.0) }
    var hardPercentage by remember { mutableDoubleStateOf(0.0) }


    val markedQuestions = remember {
        mutableStateListOf<Int>()
    }
    var questionList by remember {
        mutableStateOf<List<Question>>(emptyList())
    }
    var blurScreen by remember {
        mutableStateOf(false)
    }

    var showCameraPreview by remember {
        mutableStateOf(true)
    }
    val dimens = MaterialTheme.dimens
    var showButton by remember {
        mutableStateOf(true)
    }
    val answeredQuestions = remember {
        mutableStateMapOf<Int, String>()
    }

    val reviewQuestions = remember {
        mutableStateListOf<Int>()
    }
    var showQuestionPalette by remember {
        mutableStateOf(false)
    }
    val versionName = remember {
        context.packageManager
            .getPackageInfo(context.packageName, 0)
            .versionName
    }
    var loginId by remember { mutableStateOf("") }

    var DepartMentpedesc by remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }
    val answeredCount = answeredQuestions.size

    val reviewCount = reviewQuestions.size

    val markedCount = markedQuestions.size

    val notAnsweredCount = (questionList?.size ?: 0) - answeredCount

    var showReviewScreen by remember {
        mutableStateOf(false)
    }
    var isQuestionLoaded by remember { mutableStateOf(false) }
    var apiCalled by remember {
        mutableStateOf(false)
    }
    var submitRequestJson = ""
    val profileViewModel: ProfileViewModel = viewModel()
    appPrefs = AppPreferences(context)
    val userEmail by appPrefs.userEmail.collectAsState(initial = null)
    val usertypeEternalExteral by appPrefs.usertype.collectAsState(initial = null)
    val userloginId by appPrefs.loginId.collectAsState(initial = null)
    val Department by appPrefs.department.collectAsState(initial = null)
    loginId = userloginId.toString()
    val currentLoginId = loginId
    val usertypeEnEx = usertypeEternalExteral
    val currentEmail = userEmail?.toString().orEmpty()
    val currentVersion = versionName?.toString().orEmpty()
    var showDialogTime by remember { mutableStateOf(false) }
    val TypeCertificate by appPrefs.TypeCertificate.collectAsState(initial = null)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(
                context,
                "Camera Permission Denied",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    var isNavigated by remember {
        mutableStateOf(false)
    }
    var selectedAnswer by remember {
        mutableStateOf("")
    }
    val questionFontSize = when {
        dimens.screenPaddingHorizontal >= 32.dp -> 24.sp   // Expanded
        dimens.screenPaddingHorizontal >= 24.dp -> 22.sp   // Medium
        else -> 18.sp                                      // Compact
    }



//val dimens = MaterialTheme.dimens







//    UI State






    LaunchedEffect(category, certytype, paacategory) {



        Toast.makeText(
            context,
            category+"/"+certytype+"/"+paacategory,
            Toast.LENGTH_LONG
        ).show()
    }



    LaunchedEffect(finalsubmitState) {

        when (submitState) {

            is InsertExamState.Loading -> {

                Log.d(
                    "SUBMIT_LOADING",
                    "Loading..."
                )
            }

            is InsertExamState.Success -> {

                val response =
                    submitState.response

                Toast.makeText(
                    context,
                    response.responseDesc,
                    Toast.LENGTH_LONG
                ).show()

                if (!isNavigated) {
                    isNavigated = true

                    navController.navigate("welcome") {
                        popUpTo("CompleteProfileScreen") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }

                response.wrappedLista.forEach {


                }
            }

            is InsertExamState.Error -> {

                Toast.makeText(
                    context,
                    submitState.message,
                    Toast.LENGTH_LONG
                ).show()

                Log.d(
                    "SUBMIT_ERROR",
                    submitState.message
                )
            }

            else -> {}
        }
    }
    LaunchedEffect(submitState) {

        when (submitState) {

            is InsertExamState.Loading -> {

                Log.d(
                    "SUBMIT_LOADING",
                    "Loading..."
                )
            }

            is InsertExamState.Success -> {

                val response =
                    submitState.response



                Log.d(
                    "SUBMIT_SUCCESS",
                    Gson().toJson(response)
                )

                response.wrappedLista.forEach {




                    numberofAttempt=(it.numberofAttempt ?: 0) + 1

                    val request = ResultInsertReq(

                        loginId = loginId,
                        emailId = currentEmail,

                        totalQuestion = it.totalQuestions,
                        wrongAns = it.wrongAns,

                        // Increment attempt count by 1
                        numberofAttempt = (it.numberofAttempt ?: 0) + 1,

                        notattempteQuestion = it.notattempteQuestion,
                        scoredPercentage = it.scoredPercentage,
                        passingPercentage = it.passingPercentage,
                        correctAns = it.correctAns,
                        finalResult = it.result,
                        issueCertificate = "Yes",
                        departmentCetegory = category,
                        userTypeIe=usertypeEnEx.toString(),
                        paaCategory= paacategory,
                        certificateType= certytype
                    )


                    fialinsertViewModel.FinalinsertSubmit(request)
                }
            }

            is InsertExamState.Error -> {

                Toast.makeText(
                    context,
                    submitState.message,
                    Toast.LENGTH_LONG
                ).show()

                Log.d(
                    "SUBMIT_ERROR",
                    submitState.message
                )
            }

            else -> {}
        }
    }
    @Composable
    fun ExamScreen(
        questionList: List<Question>
    ) {



        var currentQuestionIndex by remember {
            mutableIntStateOf(0)
        }

        var selectedAnswer by remember {
            mutableStateOf("")
        }

        val currentQuestion = (questionList ?: emptyList())
            .getOrNull(currentQuestionIndex)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F3FA))
        )



        {

            // ================= HEADER =================


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(

                                Color(0xFF2563EB),
                                Color(0xFFD9CCE9)
//                                Color(0xFF8E6BC7),
//                                Color(0xFFD9CCE9)
                            )
                        )
                    )
                    .padding(dimens.spaceM),

                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )



            {

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(dimens.radiusM)
                ) {

                    Column(
                        modifier = Modifier.padding(dimens.spaceM)
                    ) {

                        Text(
                            text = "Candidate ID :"+CanddidateId,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(
                                dimens.spaceXS
                            )
                        )

                        Text(
                            text = "Candidate Name :"+UserName
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.width(
                        dimens.spaceM
                    )
                )



                Box(
                    contentAlignment = Alignment.Center
                )
                {

//                    val totalTime = 15 // 15 seconds
                    val totalTime = 60 * 60 // 60 minutes

                    var timeLeft by remember {
                        mutableStateOf(totalTime)
                    }

                    LaunchedEffect(Unit) {

                        while (timeLeft > 0) {
                            delay(1000)
                            timeLeft--
                        }
                        blurScreen = false
                        showDialog = false
                        showDialogTime = true
                        // Timer finished
//                        showDialog = true
                    }

                    CircularProgressIndicator(
                        progress = {
                            timeLeft.toFloat() / totalTime.toFloat()
                        },
                        modifier = Modifier.size(dimens.iconXL),
                        strokeWidth = dimens.space2XS,
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.25f)
                    )

                    Text(
                        text = String.format(
                            "%02d:%02d",
                            timeLeft / 60,
                            timeLeft % 60
                        ),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (showDialogTime) {

                    AlertDialog(
                        onDismissRequest = { }, // dismiss nahi hoga

                        title = {
                            Text("Time Over")
                        },

                        text = {
                            Text(
                                "Time is over. Please complete your test."
                            )
                        },

                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showReviewScreen=true
                                }
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }

// Alert Dialog
//                if (showDialogTime) {
//                    showDialog = false
//                    blurScreen = false
//
//                    AlertDialog(
//                        onDismissRequest = { },
//                        title = {
//                            Text("Time Over")
//                        },
//                        text = {
//                            Text(
//                                "Your test time has been completed."
//                            )
//                        },
//                        confirmButton = {
//                            TextButton(
//                                onClick = {
//                                    navController.popBackStack()
////                        showDialogExam = false
//                                }
//                            ) {
//                                Text("OK")
//                            }
//                        }
//                    )}

                IconButton(
                    onClick = {}
                ) {
                    IconButton(
                        onClick = {
//                            showQuestionPalette = true

                            if (answeredQuestions.size == questionList.size) {

                                val remainingQuestions =
                                    questionList.size - answeredQuestions.size

                                Toast.makeText(
                                    context,
                                    "Please attempt all questions. Remaining: $remainingQuestions",
                                    Toast.LENGTH_LONG
                                ).show()


                            }

                            val submitList = answeredQuestions.map { entry ->

                                SubmitAnswer(
                                    question_id =
                                        questionList[entry.key].questionId,

                                    answer_given =
                                        entry.value
                                )
                            }

                            val request = SubmitExamRequest(

                                courseType = numberofAttempt,
                                courseName = usertypedesc,
                                certificateType = certytype,
                                email = currentEmail,
                                loginId = currentLoginId,
                                answers = submitList,
                                userTypeIe=usertypeEnEx.toString(),
                                paaCategory=paacategory
                            )

                            submitRequestJson = GsonBuilder()
                                .setPrettyPrinting()
                                .create()
                                .toJson(request)
                            println(submitRequestJson)

                            showQuestionPalette = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(dimens.iconM)
                        )
                    }
                }
            }

            // ================= QUESTION =================

            val scrollState = rememberScrollState()

            currentQuestion?.let { question ->

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(
                            horizontal = dimens.screenPaddingHorizontal,
                            vertical = dimens.spaceM
                        )
                ) {

                    Text(
                        text = "Question ${currentQuestionIndex + 1}/${questionList.size}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(
                            dimens.spaceM
                        )
                    )
                    Text(
                        text = question.questionTitle,
                        fontSize = questionFontSize,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = (questionFontSize.value + 6).sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier = Modifier.height(
                            dimens.spaceL
                        )
                    )

                    question.options.forEach { option ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = dimens.spaceXS
                                )
                                .clickable {

                                    selectedAnswer =
                                        option.option_Key
                                },

                            shape = RoundedCornerShape(
                                dimens.radiusM
                            ),

                            colors = CardDefaults.cardColors(
                                containerColor =
                                    if (selectedAnswer == option.option_Key)
                                        Color(0xFFE7D9F8)
                                    else
                                        Color.White
                            )
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        dimens.spaceS
                                    ),

                                verticalAlignment = Alignment.Top
                            ) {

                                RadioButton(
                                    selected =
                                        selectedAnswer ==
                                                option.option_Key,

                                    onClick = {

                                        selectedAnswer =
                                            option.option_Key
                                    }
                                )

                                Spacer(
                                    modifier = Modifier.width(
                                        dimens.spaceXS
                                    )
                                )

                                Text(
                                    text = option.option_value,
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier.height(
                            dimens.spaceL
                        )
                    )
                }
            }
            // ================= ACTION BUTTONS =================
            val buttonList = listOf(
                "Save & Next" to Color(0xFF4CAF50),
                "Save & Review" to Color(0xFFFFC107),
                "Mark" to Color(0xFF03A9F4),
                "Clear" to Color(0xFF9E9E9E))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimens.screenPaddingHorizontal
                    ),

                horizontalArrangement = Arrangement.spacedBy(
                    dimens.spaceXS
                )
            )
            {

                buttonList.forEach { (text, color) ->

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(
                                RoundedCornerShape(
                                    dimens.radiusM
                                )
                            )
                            .background(color)
                            .clickable {

                                when (text) {

                                    "Save & Next" -> {
                                        if (selectedAnswer.isNotEmpty()) {
                                            answeredQuestions[currentQuestionIndex] = selectedAnswer
                                        }

                                        // ✅ Save & Next karne par Review aur Marked se hata do
                                        reviewQuestions.remove(currentQuestionIndex)
                                        markedQuestions.remove(currentQuestionIndex)

                                        if (currentQuestionIndex < questionList.lastIndex) {
                                            currentQuestionIndex++
                                            selectedAnswer = answeredQuestions[currentQuestionIndex] ?: ""
                                        }
                                    }

                                    "Save & Review" -> {
                                        if (selectedAnswer.isNotEmpty()) {
                                            answeredQuestions[currentQuestionIndex] = selectedAnswer
                                        }

                                        // ✅ Review mein add karo, Marked se hata do
                                        if (!reviewQuestions.contains(currentQuestionIndex)) {
                                            reviewQuestions.add(currentQuestionIndex)
                                        }
                                        markedQuestions.remove(currentQuestionIndex)

                                        if (currentQuestionIndex < questionList.lastIndex) {
                                            currentQuestionIndex++
                                            selectedAnswer = answeredQuestions[currentQuestionIndex] ?: ""
                                        }
                                    }

                                    "Mark" -> {
                                        // ✅ Mark mein add karo, Review se hata do
                                        if (!markedQuestions.contains(currentQuestionIndex)) {
                                            markedQuestions.add(currentQuestionIndex)
                                        }
                                        reviewQuestions.remove(currentQuestionIndex)

                                        // Answer save karo agar diya hai
                                        if (selectedAnswer.isNotEmpty()) {
                                            answeredQuestions[currentQuestionIndex] = selectedAnswer
                                        }
                                    }

                                    "Clear" -> {
                                        selectedAnswer = ""
                                        // ✅ Clear karne par saari states hata do
                                        answeredQuestions.remove(currentQuestionIndex)
                                        reviewQuestions.remove(currentQuestionIndex)
                                        markedQuestions.remove(currentQuestionIndex)
                                    }

//                                    "Clear" -> {
//                                        selectedAnswer = ""
//                                    }
//                                    "Save & Next" -> {
//
//                                        if (selectedAnswer.isNotEmpty()) {
//                                            answeredQuestions[currentQuestionIndex] =
//                                                selectedAnswer
//                                        }
//
//                                        if (currentQuestionIndex < questionList.lastIndex) {
//
//                                            currentQuestionIndex++
//                                            selectedAnswer = ""
//                                        }
//                                    }
//                                    "Save & Review" -> {
//
//                                        if (!reviewQuestions.contains(currentQuestionIndex)) {
//
//                                            reviewQuestions.add(
//                                                currentQuestionIndex
//                                            )
//                                        }
//
//                                        if (selectedAnswer.isNotEmpty()) {
//
//                                            answeredQuestions[currentQuestionIndex] =
//                                                selectedAnswer
//                                        }
//
//                                        if (currentQuestionIndex < questionList.lastIndex) {
//
//                                            currentQuestionIndex++
//                                            selectedAnswer = ""
//                                        }
//                                    }
//                                    "Mark" -> {
//
//                                        if (!markedQuestions.contains(currentQuestionIndex)) {
//
//                                            markedQuestions.add(currentQuestionIndex)
//
//
//
//                                        }
//
//                                    }


                                }
                            },

                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = text,

                            color =
                                if (text == "Save & Review")
                                    Color.Black
                                else
                                    Color.White,

                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(
                    dimens.spaceS
                )
            )


//            Spacer(modifier = Modifier.height(dimens.spaceS))
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),

                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                OutlinedButton(
                    modifier = Modifier
                        .width(90.dp)
                        .height(34.dp),

                    shape = RoundedCornerShape(8.dp),

                    onClick = {

                        if (currentQuestionIndex > 0) {

                            currentQuestionIndex--

                            selectedAnswer =
                                answeredQuestions[currentQuestionIndex]
                                    ?: ""
                        }
                    }
                ) {

                    Text(
                        text = "Previous",
                        fontSize = 10.sp
                    )
                }

                OutlinedButton(
                    modifier = Modifier
                        .width(90.dp)
                        .height(34.dp),

                    shape = RoundedCornerShape(8.dp),

                    onClick = {

                        if (currentQuestionIndex < questionList.lastIndex) {

                            currentQuestionIndex++

                            selectedAnswer =
                                answeredQuestions[currentQuestionIndex]
                                    ?: ""
                        }
                    }
                ) {

                    Text(
                        text = "Next",
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(
                    dimens.spaceM
                )
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimens.screenPaddingHorizontal,
                        vertical = dimens.spaceM
                    )
                    .height(dimens.buttonHeight),

                shape = RoundedCornerShape(
                    dimens.radiusL
                ),

                onClick = {


                    if (answeredQuestions.size == questionList.size) {

                        val remainingQuestions =
                            questionList.size - answeredQuestions.size

                        Toast.makeText(
                            context,
                            "Please attempt all questions. Remaining: $remainingQuestions",
                            Toast.LENGTH_LONG
                        ).show()

                        return@Button
                    }

                    val submitList = answeredQuestions.map { entry ->

                        SubmitAnswer(
                            question_id =
                                questionList[entry.key].questionId,

                            answer_given =
                                entry.value
                        )
                    }

                    val request = SubmitExamRequest(

                        courseType = numberofAttempt,
                        courseName = usertypedesc,
                        certificateType = certytype,
                        email = currentEmail,
                        loginId = currentLoginId,
                        answers = submitList,
                        userTypeIe=usertypeEnEx.toString(),
                        paaCategory=paacategory
                    )

                    submitRequestJson = GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .toJson(request)
                    println(submitRequestJson)

                    showQuestionPalette = true

                }
                ,

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),

                contentPadding = PaddingValues(0.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF2563EB),
                                    Color(0xFFD9CCE9)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Submit Exam",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        if (showQuestionPalette) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .zIndex(10f)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Question Index",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )

                        IconButton(
                            onClick = {
                                showQuestionPalette = false
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close"
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        LegendItem(
                            Color(0xFF9E9E9E),
                            "Not Answered"
                        )

                        LegendItem(
                            Color(0xFF4CAF50),
                            "Answered"
                        )

                        LegendItem(
                            Color(0xFFFFC107),
                            "Review"
                        )

                        LegendItem(
                            Color(0xFF03A9F4),
                            "Marked"
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        modifier = Modifier.weight(1f)
                    ) {

                        items(questionList.size) { index ->

                            val bgColor = when {

                                reviewQuestions.contains(index) ->
                                    Color(0xFFFFC107)

                                markedQuestions.contains(index) ->
                                    Color(0xFF03A9F4)

                                answeredQuestions.containsKey(index) ->
                                    Color(0xFF4CAF50)

                                else ->
                                    Color(0xFF9E9E9E)
                            }

                            Box(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(60.dp)
                                    .background(
                                        bgColor,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable {

                                        currentQuestionIndex = index
                                        showQuestionPalette = false
                                    },

                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    text = "${index + 1}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        OutlinedButton(
                            modifier = Modifier
                                .width(130.dp)
                                .height(48.dp),

                            onClick = {

                                if (currentQuestionIndex > 0) {

                                    currentQuestionIndex--
                                    showQuestionPalette = false
                                }
                            }
                        ) {

                            Text("Previous")
                        }

                        OutlinedButton(
                            modifier = Modifier
                                .width(130.dp)
                                .height(48.dp),

                            onClick = {
                                showQuestionPalette = false

                                showReviewScreen = true
                            if (currentQuestionIndex < questionList.lastIndex) {

                                currentQuestionIndex++
                                showQuestionPalette = false
                            }
                            }
                        ) {

                            Text("Next")
                        }
                    }
                }
            }
        }


        if (showReviewScreen) {
            showDialogTime=false
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .zIndex(20f)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    Text(
                        text = "Review Your Test",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceEvenly
                    ) {

                        SummaryCard(
                            "Answered",
                            answeredCount.toString(),
                            Color(0xFF4CAF50)
                        )

                        SummaryCard(
                            "Not Answered",
                            notAnsweredCount.toString(),
                            Color(0xFFF44336)
                        )

                        SummaryCard(
                            "Marked",
                            markedCount.toString(),
                            Color(0xFFFFC107)
                        )

                        SummaryCard(
                            "Total",
                            questionList.size.toString(),
                            Color(0xFF2196F3)
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {

                        items(questionList.size) { index ->

                            val status = when {

                                reviewQuestions.contains(index) ->
                                    "Marked for Review"

                                markedQuestions.contains(index) ->
                                    "Marked"

                                answeredQuestions.containsKey(index) ->
                                    "Answered"

                                else ->
                                    "Not Answered"
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),

                                horizontalArrangement =
                                    Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = "Q. ${index + 1}"
                                )

                                Text(
                                    text = status
                                )
                            }

                            Divider()
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = {

                                // Previous = dismiss
                                showReviewScreen = false
                            }
                        ) {

                            Text("Back to Test")
                        }

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {

                                val submitList =
                                    answeredQuestions.map { entry ->

                                        SubmitAnswer(

                                            question_id =
                                                questionList[entry.key].questionId,

                                            answer_given =
                                                entry.value
                                        )
                                    }

                                val request =
                                    SubmitExamRequest(

                                        courseType = numberofAttempt+1,

                                        courseName = usertypedesc,

                                        certificateType = certytype,

                                        loginId = currentLoginId,

                                        email = currentEmail,

                                        answers = submitList,
                                        userTypeIe=usertypeEnEx.toString(),
                                        paaCategory = if (processgroup.equals("PAA", ignoreCase = true)) {
                                            processgroup
                                        } else {
                                            "No"
                                        }
                                    )

                                insertViewModel.insertSubmit(request)
                            }
                        ) {

                            Text("Submit Test")
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(questionState) {

        when (questionState) {

            is QuestionUiState.Success -> {

                val response =
                    (questionState as QuestionUiState.Success)
                        .response

                questionList = response.Questions


                val summary = response.summary

                if (summary != null) {

                    totalQuestions = summary.totalQuestions
                    easyCount = summary.easyCount
                    mediumCount = summary.mediumCount
                    hardCount = summary.hardCount
                    numberofAttempt = summary.numberofAttempt

                    easyPercentage = summary.easyPercentage
                    mediumPercentage = summary.mediumPercentage
                    hardPercentage = summary.hardPercentage

                } else {

                    totalQuestions = 0
                    easyCount = 0
                    mediumCount = 0
                    hardCount = 0
                    numberofAttempt = 0

                    easyPercentage = 0.0
                    mediumPercentage = 0.0
                    hardPercentage = 0.0
                }
            }
            is QuestionUiState.Error -> {

                Toast.makeText(
                    context,
                    (questionState as QuestionUiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {}
        }
    }


    ExamScreen(questionList = questionList)

    val profileState by profileViewModel.profileState.collectAsState()
    LaunchedEffect(profileState) {

        when (val state = profileState) {

            is Resource.Success -> {

                val response = state.data

                if (response.responseDesc == "OK") {

                    response.wrappedList.forEach { item ->

                        val validationFields = listOf(
                            "Email" to item.email,
                            "First Name" to item.firstname,
                            "Last Name" to item.lastname,
                            "Age" to item.age?.toString(),
                            "Gender" to item.gender?.toString(),
                            "Pincode" to item.pincode?.toString(),
                            "City" to item.city,
                            "Address" to item.address,
                            "Mobile Number" to item.mobile,
                            "Designation" to item.designation,
                            "Process Group" to item.process_group,
                            "Organization" to item.organization,
                            "Functionary" to item.functionary,
                            "User Department Type " to item.usertypedesc,
                            "State" to item.state,
                            "District" to item.district


                        )
                        UserName =item.firstname+item.lastname
                        CanddidateId =item.loginId
                        processgroup =item.process_group
                        usertypedesc =item.usertypedesc
//                        Operation


                        val missingField = validationFields.firstOrNull {
                            it.second.isNullOrBlank() ||
                                    it.second.equals("null", true)
                        }

                        if (missingField != null) {

                            Toast.makeText(
                                context,
                                "Please complete Your Profile  ${missingField.first} then start exam",
                                Toast.LENGTH_LONG
                            ).show()

                        } else
//                            questionViewModel.fetchQuestions(questionCode.toString())


                        {
                            val hasCameraPermission =
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED

                            if (hasCameraPermission) {




                                FaceVerificationUtils.startCameraWithAnalyzer(
                                    context = context,
                                    lifecycleOwner = lifecycleOwner,
                                    previewView = previewView,
                                    onReady = { capture ->
                                        imageCapture = capture
                                    },
                                    onFace = { face ->
                                        // face is FaceData forwarded from FaceVerificationUtils
                                        val left = face.leftEyeOpenProbability
                                        val right = face.rightEyeOpenProbability

                                        // If ML Kit couldn't compute eye probabilities, ignore this detection
                                        if (left == null || right == null) {
                                            face.trackingId?.let { id ->
                                                blinkStates[id]?.lastSeenMs = System.currentTimeMillis()
                                            }
                                        } else {
                                            // maintain per-face state using trackingId if available
                                            val id = face.trackingId ?: face.hashCode()
                                            val state = blinkStates.getOrPut(id) { BlinkState() }
                                            state.lastSeenMs = System.currentTimeMillis()

                                            // Detect blink sequence: open -> closed -> open
                                            if (!state.eyesWereOpen) {
                                                if (left > 0.8f && right > 0.8f) {
                                                    state.eyesWereOpen = true
                                                    state.blinkDetected = false
                                                    state.imageCaptured = false
                                                }
                                            } else {
                                                if (!state.blinkDetected && left < 0.3f && right < 0.3f) {
                                                    state.blinkDetected = true
                                                }

                                                if (state.blinkDetected && left > 0.8f && right > 0.8f) {
                                                    if (!state.imageCaptured) {
                                                        state.imageCaptured = true

                                                        // Capture image on blink (ensure imageCapture available)
                                                        imageCapture?.let { cap ->
                                                            FaceVerificationUtils.captureImage(
                                                                imageCapture = cap,
                                                                cameraExecutor = cameraExecutor,
                                                                context = context
                                                            ) { bitmap ->
                                                                try {
                                                                    firstEmbedding = FaceVerificationUtils.createEmbedding(bitmap)
                                                                    showCameraPreview = false
                                                                    Toast.makeText(context, "Blink Detected & Face Captured", Toast.LENGTH_SHORT).show()
                                                                } catch (e: Exception) {
                                                                    e.printStackTrace()
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            // cleanup stale states (not seen for >5s)
                                            val now = System.currentTimeMillis()
                                            val stale = blinkStates.filterValues { now - it.lastSeenMs > 5_000L }.keys
                                            stale.forEach { blinkStates.remove(it) }
                                        }
                                    }
                                )

                            } else {
                                permissionLauncher.launch(
                                    Manifest.permission.CAMERA
                                )
                            }
                        }
                    }
                }
            }


            is Resource.Error -> {
                showLoading = false

            }

            is Resource.Loading -> {
                showLoading = false
                Log.d(
                    "PROFILE",
                    "Loading..."
                )
            }

            else -> {
                showLoading = false
            }
        }
    }
    if (showLoading) {
        Dialog(onDismissRequest = { }) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(Color.White, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.Blue
                )
            }
        }
    }

//    LaunchedEffect(firstEmbedding) {
//
//        while (firstEmbedding != null && !apiCalled) {
//
//            delay(2000)
//
//            FaceVerificationUtils.captureImage(
//                imageCapture = imageCapture,
//                cameraExecutor = cameraExecutor,
//                context = context
//            ) { bitmap ->
//
//                val currentEmbedding =
//                    FaceVerificationUtils.createEmbedding(bitmap)
//
//                val savedEmbedding = firstEmbedding
//
//                if (savedEmbedding != null) {
//
//                    val distance =
//                        FaceVerificationUtils.compareEmbeddings(
//                            savedEmbedding,
//                            currentEmbedding
//                        )
//                    if (distance < 2000) {
//
//                        questionViewModel.fetchQuestions(
//                            category = Department.toString()
//                        )
//
//                        showDialog = false
//                        blurScreen = false
//
//                    }
//                    else {
//
//                        blurScreen = true
//                        showDialog = true
//                    }
//                }
//            }
//        }
//    }


    LaunchedEffect(firstEmbedding) {
        while (firstEmbedding != null && !apiCalled) {
//        while (firstEmbedding != null) {

            delay(2000)

            captureImage(
                imageCapture = imageCapture,
                cameraExecutor = cameraExecutor,
                context = context
            ) { bitmap ->

                val currentEmbedding =
                    createEmbedding(bitmap)

                val savedEmbedding =
                    firstEmbedding

                if (savedEmbedding != null) {

                    val distance =
                        compareEmbeddings(
                            savedEmbedding,
                            currentEmbedding
                        )

                    if (distance < 2000) {

                        questionViewModel.fetchQuestions(
                            category = category,
                            certytype = certytype,
                            paacategory = if (processgroup.equals("PAA", true))
                                processgroup
                            else
                                "No"
                        )



//                        questionViewModel.fetchQuestions(
//                            category = Department.toString()
//                        )
                        resultText =
                            "✅ Genuine Person"

                        blurScreen = false

                        showDialog = false

                    } else {

                        resultText =
                            "❌ Face Not Matched"

                        blurScreen = true

                        showDialog = true
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (blurScreen) {
                        Modifier.graphicsLayer {
                            alpha = 0.3f
                        }
                    } else {
                        Modifier
                    }
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showCameraPreview) {
                AndroidView(
                    factory = {
                        previewView
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            if (showButton) {

                Button(
                    onClick = {
                        showButton = false
                        profileViewModel.getProfile(
                            appVersion = currentVersion,
                            loginId = currentLoginId,
                            email = currentEmail
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text("Capture First Face")
                }
            }
        }

        if (showDialog) {
            VibrateWhileDialogVisible(showDialog)

            AlertDialog(
                onDismissRequest = {},
                title = {
                    Text("Security Alert")
                },
                text = {
                    Text("Face Not Matched or Object Detected")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            blurScreen = false
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }}}


    // START CAMERA
    private fun startCamera(
        context: android.content.Context,
        previewView: PreviewView,
        onReady: (ImageCapture) -> Unit
    ) {

        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({

            val cameraProvider =
                cameraProviderFuture.get()

            val preview =
                Preview.Builder().build()

            preview.setSurfaceProvider(
                previewView.surfaceProvider
            )

            val imageCapture =
                ImageCapture.Builder()
                    .setCaptureMode(
                        ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
                    )
                    .build()

            val cameraSelector =
                CameraSelector.DEFAULT_FRONT_CAMERA

            try {

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    context as androidx.lifecycle.LifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )

                onReady(imageCapture)

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    context,
                    "Camera Start Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }, ContextCompat.getMainExecutor(context))
    }

    // CAPTURE IMAGE
    private fun captureImage(
        imageCapture: ImageCapture?,
        cameraExecutor: ExecutorService,
        context: android.content.Context,
        onBitmapReady: (Bitmap) -> Unit
    ) {

        val capture = imageCapture

        if (capture == null) {

            Toast.makeText(
                context,
                "Camera Not Ready",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        capture.takePicture(

            cameraExecutor,

            object : ImageCapture.OnImageCapturedCallback() {

                override fun onCaptureSuccess(
                    image: ImageProxy
                ) {

                    try {

                        val bitmap =
                            imageProxyToBitmap(image)

                        onBitmapReady(bitmap)

                    } catch (e: Exception) {

                        e.printStackTrace()

                    } finally {

                        image.close()
                    }
                }

                override fun onError(
                    exception: ImageCaptureException
                ) {

                    exception.printStackTrace()

                    Toast.makeText(
                        context,
                        "Capture Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    // IMAGE TO BITMAP
    private fun imageProxyToBitmap(
        image: ImageProxy
    ): Bitmap {

        val buffer: ByteBuffer =
            image.planes[0].buffer

        val bytes =
            ByteArray(buffer.remaining())

        buffer.get(bytes)

        return BitmapFactory.decodeByteArray(
            bytes,
            0,
            bytes.size
        )
    }

    // SIMPLE FACE EMBEDDING
    private fun createEmbedding(
        bitmap: Bitmap
    ): FloatArray {

        val resized =
            Bitmap.createScaledBitmap(
                bitmap,
                32,
                32,
                true
            )

        val embedding =
            FloatArray(32 * 32)

        var index = 0

        for (x in 0 until 32) {

            for (y in 0 until 32) {

                val pixel =
                    resized.getPixel(x, y)

                val r =
                    (pixel shr 16) and 0xff

                val g =
                    (pixel shr 8) and 0xff

                val b =
                    pixel and 0xff

                val gray =
                    (r + g + b) / 3f

                embedding[index] = gray

                index++
            }
        }

        return embedding
    }

    // COMPARE EMBEDDING
    private fun compareEmbeddings(
        emb1: FloatArray,
        emb2: FloatArray
    ): Float {

        var sum = 0f

        for (i in emb1.indices) {

            val diff =
                emb1[i] - emb2[i]

            sum += diff * diff
        }

        return sqrt(sum)
    }

























//@Composable
//fun TestScreen(
//    navController: NavController,
//    viewModel: CompletePofileScreenViewModel = viewModel(),
//    updateModel: UpdateProfileViewModel = viewModel(),
//    questionViewModel: QuestionViewModel = viewModel() ,
//    insertViewModel: InsertViewModel = viewModel(),
//    fialinsertViewModel: FinalInsertViewModel = viewModel()
//) {
//    val state = viewModel.state
//    val UpdateUI = updateModel.Updatestate
//    val submitState = insertViewModel.state
//    val finalsubmitState = fialinsertViewModel.state
//    val questionState = questionViewModel.uiState
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    lateinit var appPrefs: AppPreferences
//    val previewView = remember {
//        PreviewView(context)
//    }
////    val questionViewModel: EsopQuestionModel = viewModel()
//    var showLoading by remember { mutableStateOf(false) }
//    val cameraExecutor = remember {
//        Executors.newSingleThreadExecutor()
//    }
//
//    // Blink tracking state per face (trackingId -> state)
//    data class BlinkState(
//        var eyesWereOpen: Boolean = false,
//        var blinkDetected: Boolean = false,
//        var imageCaptured: Boolean = false,
//        var lastSeenMs: Long = System.currentTimeMillis()
//    )
//
//    val blinkStates = remember { mutableStateMapOf<Int, BlinkState>() }
//
//    var imageCapture by remember {
//        mutableStateOf<ImageCapture?>(null)
//    }
//
//    var UserName by remember { mutableStateOf("") }
//    var CanddidateId by remember { mutableStateOf("") }
//    var usertypedesc by remember { mutableStateOf("") }
//    var firstEmbedding by remember {
//        mutableStateOf<FloatArray?>(null)
//    }
//
//    var resultText by remember { mutableStateOf("No Face Captured") }
//    var questionCode by remember { mutableStateOf(2) }
//
//    var showDialog by remember {
//        mutableStateOf(false)
//    }
//    var totalQuestions by remember { mutableIntStateOf(0) }
//    var easyCount by remember { mutableIntStateOf(0) }
//    var mediumCount by remember { mutableIntStateOf(0) }
//    var hardCount by remember { mutableIntStateOf(0) }
//    var numberofAttempt by remember { mutableIntStateOf(0) }
//
//    var easyPercentage by remember { mutableDoubleStateOf(0.0) }
//    var mediumPercentage by remember { mutableDoubleStateOf(0.0) }
//    var hardPercentage by remember { mutableDoubleStateOf(0.0) }
//
//
//    val markedQuestions = remember {
//        mutableStateListOf<Int>()
//    }
//    var questionList by remember {
//        mutableStateOf<List<Question>>(emptyList())
//    }
//    var blurScreen by remember {
//        mutableStateOf(false)
//    }
//
//    var showCameraPreview by remember {
//        mutableStateOf(true)
//    }
//    val dimens = MaterialTheme.dimens
//    var showButton by remember {
//        mutableStateOf(true)
//    }
//    val answeredQuestions = remember {
//        mutableStateMapOf<Int, String>()
//    }
//
//    val reviewQuestions = remember {
//        mutableStateListOf<Int>()
//    }
//    var showQuestionPalette by remember {
//        mutableStateOf(false)
//    }
//    val versionName = remember {
//        context.packageManager
//            .getPackageInfo(context.packageName, 0)
//            .versionName
//    }
//    var loginId by remember { mutableStateOf("") }
//    var DepartMentpedesc by remember { mutableStateOf("") }
//
//    DisposableEffect(Unit) {
//        onDispose {
//            cameraExecutor.shutdown()
//        }
//    }
//    val answeredCount = answeredQuestions.size
//
//    val reviewCount = reviewQuestions.size
//
//    val markedCount = markedQuestions.size
//
//    val notAnsweredCount =
//        questionList.size - answeredCount
//
//    var showReviewScreen by remember {
//        mutableStateOf(false)
//    }
//
//     var submitRequest: SubmitExamRequest? = null
//     var submitRequestJson = ""
//    val profileViewModel: ProfileViewModel = viewModel()
//    appPrefs = AppPreferences(context)
//    val userEmail by appPrefs.userEmail.collectAsState(initial = null)
//    val userMobile by appPrefs.mobile.collectAsState(initial = null)
//    val userloginId by appPrefs.loginId.collectAsState(initial = null)
//    val department by appPrefs.department.collectAsState(initial = "")
//    val userusertype by appPrefs.usertype.collectAsState(initial = null)
//    loginId = userloginId.toString()
//    DepartMentpedesc = department.toString()
//
//
//    val currentLoginId = loginId
//    val currentEmail = userEmail?.toString().orEmpty()
//    val currentVersion = versionName?.toString().orEmpty()
//    val permissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { granted ->
//        if (!granted) {
//            Toast.makeText(
//                context,
//                "Camera Permission Denied",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
//    var isNavigated by remember {
//        mutableStateOf(false)
//    }
//
//    val questionFontSize = when {
//        dimens.screenPaddingHorizontal >= 32.dp -> 24.sp   // Expanded
//        dimens.screenPaddingHorizontal >= 24.dp -> 22.sp   // Medium
//        else -> 18.sp                                      // Compact
//    }
////val dimens = MaterialTheme.dimens
//
//
//
//
//
//
//
////    UI State
//
//    LaunchedEffect(finalsubmitState) {
//
//        when (submitState) {
//
//            is InsertExamState.Loading -> {
//
//                Log.d(
//                    "SUBMIT_LOADING",
//                    "Loading..."
//                )
//            }
//
//            is InsertExamState.Success -> {
//
//                val response =
//                    submitState.response
//
//                Toast.makeText(
//                    context,
//                    response.responseDesc,
//                    Toast.LENGTH_LONG
//                ).show()
//
//                if (!isNavigated) {
//                        isNavigated = true
//
//                        navController.navigate("welcome") {
//                            popUpTo("CompleteProfileScreen") {
//                                inclusive = true
//                            }
//                            launchSingleTop = true
//                        }
//                    }
//
//                response.wrappedLista.forEach {
//
//
//                }
//            }
//
//            is InsertExamState.Error -> {
//
//                Toast.makeText(
//                    context,
//                    submitState.message,
//                    Toast.LENGTH_LONG
//                ).show()
//
//                Log.d(
//                    "SUBMIT_ERROR",
//                    submitState.message
//                )
//            }
//
//            else -> {}
//        }
//    }
//    LaunchedEffect(submitState) {
//
//        when (submitState) {
//
//            is InsertExamState.Loading -> {
//
//                Log.d(
//                    "SUBMIT_LOADING",
//                    "Loading..."
//                )
//            }
//
//            is InsertExamState.Success -> {
//
//                val response =
//                    submitState.response
//
//
//
//                Log.d(
//                    "SUBMIT_SUCCESS",
//                    Gson().toJson(response)
//                )
//
//                response.wrappedLista.forEach {
//
//
//
//                    val request =
//                        ResultInsertReq(
//
//                            loginId = loginId,
//
//                            emailId = currentEmail,
//
//                            totalQuestion = it.totalQuestions,
//
//                            wrongAns = it.wrongAns,
//
//                            numberofAttempt = it.numberofAttempt+1,
//
//                            notattempteQuestion = it.notattempteQuestion,
//                            scoredPercentage=it.scoredPercentage,
//                            passingPercentage=it.passingPercentage,
//                            correctAns=it.correctAns,
//                            finalResult=it.result,
//                            issueCertificate="Yes",
//
//
//                        )
//
//                    fialinsertViewModel.FinalinsertSubmit(request)
//
//
//                }
//            }
//
//            is InsertExamState.Error -> {
//
//                Toast.makeText(
//                    context,
//                    submitState.message,
//                    Toast.LENGTH_LONG
//                ).show()
//
//                Log.d(
//                    "SUBMIT_ERROR",
//                    submitState.message
//                )
//            }
//
//            else -> {}
//        }
//    }
//
//
//
//    @Composable
//    fun ExamScreen(
//        questionList: List<Question>
//    ) {
//
//
//
//        var currentQuestionIndex by remember {
//            mutableIntStateOf(0)
//        }
//
//        var selectedAnswer by remember {
//            mutableStateOf("")
//        }
//
//        val currentQuestion = questionList.getOrNull(currentQuestionIndex)
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color(0xFFF7F3FA))
//        )
//
//
//
//        {
//
//            // ================= HEADER =================
//
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(
//                        Brush.horizontalGradient(
//                            listOf(
//
//                                    Color(0xFF2563EB),
//                                Color(0xFFD9CCE9)
////                                Color(0xFF8E6BC7),
////                                Color(0xFFD9CCE9)
//                            )
//                        )
//                    )
//                    .padding(dimens.spaceM),
//
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            )
//
//
//
//            {
//
//                Card(
//                    modifier = Modifier.weight(1f),
//                    shape = RoundedCornerShape(dimens.radiusM)
//                ) {
//
//                    Column(
//                        modifier = Modifier.padding(dimens.spaceM)
//                    ) {
//
//                        Text(
//                            text = "Candidate ID :"+CanddidateId,
//                            fontWeight = FontWeight.Bold
//                        )
//
//                        Spacer(
//                            modifier = Modifier.height(
//                                dimens.spaceXS
//                            )
//                        )
//
//                        Text(
//                            text = "Candidate Name :"+UserName
//                        )
//                    }
//                }
//
//                Spacer(
//                    modifier = Modifier.width(
//                        dimens.spaceM
//                    )
//                )
//
//                Box(
//                    contentAlignment = Alignment.Center
//                )
//                {
//                    val totalTime = 30 * 60 // 30 minutes
//
//                    var timeLeft by remember {
//                        mutableStateOf(totalTime)
//                    }
//
//                    LaunchedEffect(Unit) {
//
//                        while (timeLeft > 0) {
//
//                            delay(1000)
//
//                            timeLeft--
//                        }
//                    }
//
//                    val progress =
//                        timeLeft.toFloat() / totalTime.toFloat()
//
//                    CircularProgressIndicator(
//                        progress = {
//                            timeLeft.toFloat() / totalTime.toFloat()
//                        },
//                        modifier = Modifier.size(
//                            dimens.iconXL
//                        ),
//                        strokeWidth = dimens.space2XS,
//                        color = Color.White,
//                        trackColor = Color.White.copy(alpha = 0.25f)
//                    )
//
//                    Text(
//                        text = String.format(
//                            "%02d:%02d",
//                            timeLeft / 60,
//                            timeLeft % 60
//                        ),
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold)
//                }
//                IconButton(
//                    onClick = {}
//                ) {
//                    IconButton(
//                        onClick = {
//                            showQuestionPalette = true
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Edit,
//                            contentDescription = null,
//                            modifier = Modifier.size(dimens.iconM)
//                        )
//                    }
//                }
//            }
//
//            // ================= QUESTION =================
//
//            val scrollState = rememberScrollState()
//
//            currentQuestion?.let { question ->
//
//                Column(
//                    modifier = Modifier
//                        .weight(1f)
//                        .verticalScroll(scrollState)
//                        .padding(
//                            horizontal = dimens.screenPaddingHorizontal,
//                            vertical = dimens.spaceM
//                        )
//                ) {
//
//                    Text(
//                        text = "Question ${currentQuestionIndex + 1}/${questionList.size}",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    Spacer(
//                        modifier = Modifier.height(
//                            dimens.spaceM
//                        )
//                    )
//                    Text(
//                        text = question.questionTitle,
//                        fontSize = questionFontSize,
//                        fontWeight = FontWeight.SemiBold,
//                        lineHeight = (questionFontSize.value + 6).sp,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//
//                    Spacer(
//                        modifier = Modifier.height(
//                            dimens.spaceL
//                        )
//                    )
//
//                    question.options.forEach { option ->
//
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(
//                                    vertical = dimens.spaceXS
//                                )
//                                .clickable {
//
//                                    selectedAnswer =
//                                        option.option_Key
//                                },
//
//                            shape = RoundedCornerShape(
//                                dimens.radiusM
//                            ),
//
//                            colors = CardDefaults.cardColors(
//                                containerColor =
//                                    if (selectedAnswer == option.option_Key)
//                                        Color(0xFFE7D9F8)
//                                    else
//                                        Color.White
//                            )
//                        ) {
//
//                            Row(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(
//                                        dimens.spaceS
//                                    ),
//
//                                verticalAlignment = Alignment.Top
//                            ) {
//
//                                RadioButton(
//                                    selected =
//                                        selectedAnswer ==
//                                                option.option_Key,
//
//                                    onClick = {
//
//                                        selectedAnswer =
//                                            option.option_Key
//                                    }
//                                )
//
//                                Spacer(
//                                    modifier = Modifier.width(
//                                        dimens.spaceXS
//                                    )
//                                )
//
//                                Text(
//                                    text = option.option_value,
//                                    modifier = Modifier.weight(1f),
//                                    style = MaterialTheme.typography.bodyMedium
//                                )
//                            }
//                        }
//                    }
//                    Spacer(
//                        modifier = Modifier.height(
//                            dimens.spaceL
//                        )
//                    )
//                }
//            }
//            // ================= ACTION BUTTONS =================
//            val buttonList = listOf(
//                "Save & Next" to Color(0xFF4CAF50),
//                "Save & Review" to Color(0xFFFFC107),
//                "Mark" to Color(0xFF03A9F4),
//                "Clear" to Color(0xFF9E9E9E))
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(
//                        horizontal = dimens.screenPaddingHorizontal
//                    ),
//
//                horizontalArrangement = Arrangement.spacedBy(
//                    dimens.spaceXS
//                )
//            )
//            {
//
//                buttonList.forEach { (text, color) ->
//
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .height(48.dp)
//                            .clip(
//                                RoundedCornerShape(
//                                    dimens.radiusM
//                                )
//                            )
//                            .background(color)
//                            .clickable {
//
//                                when (text) {
//
//                                    "Clear" -> {
//                                        selectedAnswer = ""
//                                    }
//                                    "Save & Next" -> {
//
//                                        if (selectedAnswer.isNotEmpty()) {
//                                            answeredQuestions[currentQuestionIndex] =
//                                                selectedAnswer
//                                        }
//
//                                        if (currentQuestionIndex < questionList.lastIndex) {
//
//                                            currentQuestionIndex++
//                                            selectedAnswer = ""
//                                        }
//                                    }
//                                    "Save & Review" -> {
//
//                                        if (!reviewQuestions.contains(currentQuestionIndex)) {
//
//                                            reviewQuestions.add(
//                                                currentQuestionIndex
//                                            )
//                                        }
//
//                                        if (selectedAnswer.isNotEmpty()) {
//
//                                            answeredQuestions[currentQuestionIndex] =
//                                                selectedAnswer
//                                        }
//
//                                        if (currentQuestionIndex < questionList.lastIndex) {
//
//                                            currentQuestionIndex++
//                                            selectedAnswer = ""
//                                        }
//                                    }
//                                    "Mark" -> {
//
//                                        if (!markedQuestions.contains(currentQuestionIndex)) {
//
//                                            markedQuestions.add(currentQuestionIndex)
//
//
//
//                                        }
//
//                                    }
//
//
//                                }
//                            },
//
//                        contentAlignment = Alignment.Center
//                    ) {
//
//                        Text(
//                            text = text,
//
//                            color =
//                                if (text == "Save & Review")
//                                    Color.Black
//                                else
//                                    Color.White,
//
//                            fontSize = 10.sp,
//                            fontWeight = FontWeight.Bold,
//                            textAlign = TextAlign.Center,
//                            maxLines = 1
//                        )
//                    }
//                }
//            }
//
//            Spacer(
//                modifier = Modifier.height(
//                    dimens.spaceS
//                )
//            )
//
//
////            Spacer(modifier = Modifier.height(dimens.spaceS))
//            Spacer(modifier = Modifier.height(20.dp))
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(
//                        top = 8.dp,
//                        start = 16.dp,
//                        end = 16.dp
//                    ),
//
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//
//                OutlinedButton(
//                    modifier = Modifier
//                        .width(90.dp)
//                        .height(34.dp),
//
//                    shape = RoundedCornerShape(8.dp),
//
//                    onClick = {
//
//                        if (currentQuestionIndex > 0) {
//
//                            currentQuestionIndex--
//
//                            selectedAnswer =
//                                answeredQuestions[currentQuestionIndex]
//                                    ?: ""
//                        }
//                    }
//                ) {
//
//                    Text(
//                        text = "Previous",
//                        fontSize = 10.sp
//                    )
//                }
//
//                OutlinedButton(
//                    modifier = Modifier
//                        .width(90.dp)
//                        .height(34.dp),
//
//                    shape = RoundedCornerShape(8.dp),
//
//                    onClick = {
//
//                        if (currentQuestionIndex < questionList.lastIndex) {
//
//                            currentQuestionIndex++
//
//                            selectedAnswer =
//                                answeredQuestions[currentQuestionIndex]
//                                    ?: ""
//                        }
//                    }
//                ) {
//
//                    Text(
//                        text = "Next",
//                        fontSize = 10.sp
//                    )
//                }
//            }
//
//            Spacer(
//                modifier = Modifier.height(
//                    dimens.spaceM
//                )
//            )
//
//            Button(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(
//                        horizontal = dimens.screenPaddingHorizontal,
//                        vertical = dimens.spaceM
//                    )
//                    .height(dimens.buttonHeight),
//
//                shape = RoundedCornerShape(
//                    dimens.radiusL
//                ),
//
//                onClick = {
//
//                    if (answeredQuestions.size != questionList.size) {
//
//                        val remainingQuestions =
//                            questionList.size - answeredQuestions.size
//
//                        Toast.makeText(
//                            context,
//                            "Please attempt all questions. Remaining: $remainingQuestions",
//                            Toast.LENGTH_LONG
//                        ).show()
//
//                        return@Button
//                    }
//
//                    val submitList = answeredQuestions.map { entry ->
//
//                        SubmitAnswer(
//                            question_id =
//                                questionList[entry.key].questionId,
//
//                            answer_given =
//                                entry.value
//                        )
//                    }
//
//                    val request = SubmitExamRequest(
//
//                        courseType = 2,
//                        courseName = usertypedesc,
//                        certificateType = "Master",
//                        email = currentEmail,
//                        loginId = currentLoginId,
//                        answers = submitList
//                    )
//
//                    submitRequestJson = GsonBuilder()
//                        .setPrettyPrinting()
//                        .create()
//                        .toJson(request)
//                    println(submitRequestJson)
//
//                        showQuestionPalette = true
//
//                }
//                ,
//
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Transparent
//                ),
//
//                contentPadding = PaddingValues(0.dp)
//            ) {
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(
//                            brush = Brush.horizontalGradient(
//                                colors = listOf(
//                                    Color(0xFF2563EB),
//                                    Color(0xFFD9CCE9)
//                                )
//                            )
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//
//                    Text(
//                        text = "Submit Exam",
//                        color = Color.White,
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//        }
//    if (showQuestionPalette) {
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.White)
//                .zIndex(10f)
//        ) {
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)
//            ) {
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//
//                    Text(
//                        text = "Question Index",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 24.sp
//                    )
//
//                    IconButton(
//                        onClick = {
//                            showQuestionPalette = false
//                        }
//                    ) {
//
//                        Icon(
//                            imageVector = Icons.Default.Close,
//                            contentDescription = "Close"
//                        )
//                    }
//                }
//
//                Spacer(
//                    modifier = Modifier.height(20.dp)
//                )
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly
//                ) {
//
//                    LegendItem(
//                        Color(0xFF9E9E9E),
//                        "Not Answered"
//                    )
//
//                    LegendItem(
//                        Color(0xFF4CAF50),
//                        "Answered"
//                    )
//
//                    LegendItem(
//                        Color(0xFFFFC107),
//                        "Review"
//                    )
//
//                    LegendItem(
//                        Color(0xFF03A9F4),
//                        "Marked"
//                    )
//                }
//
//                Spacer(
//                    modifier = Modifier.height(20.dp)
//                )
//
//                LazyVerticalGrid(
//                    columns = GridCells.Fixed(5),
//                    modifier = Modifier.weight(1f)
//                ) {
//
//                    items(questionList.size) { index ->
//
//                        val bgColor = when {
//
//                            reviewQuestions.contains(index) ->
//                                Color(0xFFFFC107)
//
//                            markedQuestions.contains(index) ->
//                                Color(0xFF03A9F4)
//
//                            answeredQuestions.containsKey(index) ->
//                                Color(0xFF4CAF50)
//
//                            else ->
//                                Color(0xFF9E9E9E)
//                        }
//
//                        Box(
//                            modifier = Modifier
//                                .padding(6.dp)
//                                .size(60.dp)
//                                .background(
//                                    bgColor,
//                                    RoundedCornerShape(10.dp)
//                                )
//                                .clickable {
//
//                                    currentQuestionIndex = index
//                                    showQuestionPalette = false
//                                },
//
//                            contentAlignment = Alignment.Center
//                        ) {
//
//                            Text(
//                                text = "${index + 1}",
//                                color = Color.White,
//                                fontWeight = FontWeight.Bold
//                            )
//                        }
//                    }
//                }
//
//                Spacer(
//                    modifier = Modifier.height(12.dp)
//                )
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement =
//                        Arrangement.SpaceBetween
//                ) {
//
//                    OutlinedButton(
//                        modifier = Modifier
//                            .width(130.dp)
//                            .height(48.dp),
//
//                        onClick = {
//
//                            if (currentQuestionIndex > 0) {
//
//                                currentQuestionIndex--
//                                showQuestionPalette = false
//                            }
//                        }
//                    ) {
//
//                        Text("Previous")
//                    }
//
//                    OutlinedButton(
//                        modifier = Modifier
//                            .width(130.dp)
//                            .height(48.dp),
//
//                        onClick = {
//                            showQuestionPalette = false
//
//                            showReviewScreen = true
////                            if (currentQuestionIndex < questionList.lastIndex) {
////
////                                currentQuestionIndex++
////                                showQuestionPalette = false
////                            }
//                        }
//                    ) {
//
//                        Text("Next")
//                    }
//                }
//            }
//        }
//    }
//
//
//    if (showReviewScreen) {
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.White)
//                .zIndex(20f)
//        ) {
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)
//            ) {
//
//                Text(
//                    text = "Review Your Test",
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(
//                    modifier = Modifier.height(16.dp)
//                )
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement =
//                        Arrangement.SpaceEvenly
//                ) {
//
//                    SummaryCard(
//                        "Answered",
//                        answeredCount.toString(),
//                        Color(0xFF4CAF50)
//                    )
//
//                    SummaryCard(
//                        "Not Answered",
//                        notAnsweredCount.toString(),
//                        Color(0xFFF44336)
//                    )
//
//                    SummaryCard(
//                        "Marked",
//                        markedCount.toString(),
//                        Color(0xFFFFC107)
//                    )
//
//                    SummaryCard(
//                        "Total",
//                        questionList.size.toString(),
//                        Color(0xFF2196F3)
//                    )
//                }
//
//                Spacer(
//                    modifier = Modifier.height(20.dp)
//                )
//
//                LazyColumn(
//                    modifier = Modifier.weight(1f)
//                ) {
//
//                    items(questionList.size) { index ->
//
//                        val status = when {
//
//                            reviewQuestions.contains(index) ->
//                                "Marked for Review"
//
//                            markedQuestions.contains(index) ->
//                                "Marked"
//
//                            answeredQuestions.containsKey(index) ->
//                                "Answered"
//
//                            else ->
//                                "Not Answered"
//                        }
//
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(8.dp),
//
//                            horizontalArrangement =
//                                Arrangement.SpaceBetween
//                        ) {
//
//                            Text(
//                                text = "Q. ${index + 1}"
//                            )
//
//                            Text(
//                                text = status
//                            )
//                        }
//
//                        Divider()
//                    }
//                }
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement =
//                        Arrangement.spacedBy(12.dp)
//                ) {
//
//                    OutlinedButton(
//                        modifier = Modifier.weight(1f),
//                        onClick = {
//
//                            // Previous = dismiss
//                            showReviewScreen = false
//                        }
//                    ) {
//
//                        Text("Back to Test")
//                    }
//
//                    Button(
//                        modifier = Modifier.weight(1f),
//                        onClick = {
//
//                            val submitList =
//                                answeredQuestions.map { entry ->
//
//                                    SubmitAnswer(
//
//                                        question_id =
//                                            questionList[entry.key].questionId,
//
//                                        answer_given =
//                                            entry.value
//                                    )
//                                }
//
//                            val request =
//                                SubmitExamRequest(
//
//                                    courseType = 2,
//
//                                    courseName = "Operations",
//
//                                    certificateType = "Master",
//
//                                    loginId = currentLoginId,
//
//                                    email = currentEmail,
//
//                                    answers = submitList
//                                )
//
//                            insertViewModel.insertSubmit(request)
//                        }
//                    ) {
//
//                        Text("Submit Test")
//                    }
//                }
//            }
//        }
//    }
//    }
//
//
//    LaunchedEffect(questionState) {
//
//        when (questionState) {
//
//            is QuestionUiState.Success -> {
//
//                val response =
//                    (questionState as QuestionUiState.Success)
//                        .response
//
//                questionList =
//                    response.Questions
//
//                totalQuestions =
//                    response.summary.totalQuestions
//
//                easyCount =
//                    response.summary.easyCount
//
//                mediumCount =
//                    response.summary.mediumCount
//
//                hardCount =
//                    response.summary.hardCount
//
//                numberofAttempt =
//                    response.summary.numberofAttempt
//
//                easyPercentage =
//                    response.summary.easyPercentage
//
//                mediumPercentage =
//                    response.summary.mediumPercentage
//
//                hardPercentage =
//                    response.summary.hardPercentage
//            }
//
//            is QuestionUiState.Error -> {
//
//                Toast.makeText(
//                    context,
//                    (questionState as QuestionUiState.Error).message,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            else -> {}
//        }
//    }
//
//
//
//
//
//
//
//
//    ExamScreen(questionList = questionList)
//
//    val profileState by profileViewModel.profileState.collectAsState()
//    LaunchedEffect(profileState) {
//
//        when (val state = profileState) {
//
//            is Resource.Success -> {
//
//                val response = state.data
//
//                if (response.responseDesc == "OK") {
//
//                    response.wrappedList.forEach { item ->
//
//                        val validationFields = listOf(
//                            "Email" to item.email,
//                            "First Name" to item.firstname,
//                            "Last Name" to item.lastname,
//                            "Age" to item.age?.toString(),
//                            "Gender" to item.gender?.toString(),
//                            "Pincode" to item.pincode?.toString(),
//                            "City" to item.city,
//                            "Address" to item.address,
//                            "Mobile Number" to item.mobile,
//                            "Designation" to item.designation,
//                            "Process Group" to item.process_group,
//                            "Organization" to item.organization,
//                            "Functionary" to item.functionary,
//                            "User Department Type " to item.usertypedesc,
//                            "State" to item.state,
//                            "District" to item.district
//
//
//                        )
//                          UserName =item.firstname+item.lastname
//                        CanddidateId =item.loginId
//                        usertypedesc =item.usertypedesc
////                        Operation
//
//
//                        val missingField = validationFields.firstOrNull {
//                            it.second.isNullOrBlank() ||
//                                    it.second.equals("null", true)
//                        }
//
//                        if (missingField != null) {
//
//                            Toast.makeText(
//                                context,
//                                "Please complete Your Profile  ${missingField.first} then start exam",
//                                Toast.LENGTH_LONG
//                            ).show()
//
//                        } else
////                            questionViewModel.fetchQuestions(questionCode.toString())
//
//
//                            {
//                                val hasCameraPermission =
//                                    ContextCompat.checkSelfPermission(
//                                        context,
//                                        Manifest.permission.CAMERA
//                                    ) == PackageManager.PERMISSION_GRANTED
//
//                                if (hasCameraPermission) {
//
//
//
//
//                                        FaceVerificationUtils.startCameraWithAnalyzer(
//                                            context = context,
//                                            lifecycleOwner = lifecycleOwner,
//                                            previewView = previewView,
//                                            onReady = { capture ->
//                                                imageCapture = capture
//                                            },
//                                            onFace = { face ->
//                                                // face is FaceData forwarded from FaceVerificationUtils
//                                                val left = face.leftEyeOpenProbability
//                                                val right = face.rightEyeOpenProbability
//
//                                                // If ML Kit couldn't compute eye probabilities, ignore this detection
//                                                if (left == null || right == null) {
//                                                    face.trackingId?.let { id ->
//                                                        blinkStates[id]?.lastSeenMs = System.currentTimeMillis()
//                                                    }
//                                                } else {
//                                                    // maintain per-face state using trackingId if available
//                                                    val id = face.trackingId ?: face.hashCode()
//                                                    val state = blinkStates.getOrPut(id) { BlinkState() }
//                                                    state.lastSeenMs = System.currentTimeMillis()
//
//                                                    // Detect blink sequence: open -> closed -> open
//                                                    if (!state.eyesWereOpen) {
//                                                        if (left > 0.8f && right > 0.8f) {
//                                                            state.eyesWereOpen = true
//                                                            state.blinkDetected = false
//                                                            state.imageCaptured = false
//                                                        }
//                                                    } else {
//                                                        if (!state.blinkDetected && left < 0.3f && right < 0.3f) {
//                                                            state.blinkDetected = true
//                                                        }
//
//                                                        if (state.blinkDetected && left > 0.8f && right > 0.8f) {
//                                                            if (!state.imageCaptured) {
//                                                                state.imageCaptured = true
//
//                                                                // Capture image on blink (ensure imageCapture available)
//                                                                imageCapture?.let { cap ->
//                                                                    FaceVerificationUtils.captureImage(
//                                                                        imageCapture = cap,
//                                                                        cameraExecutor = cameraExecutor,
//                                                                        context = context
//                                                                    ) { bitmap ->
//                                                                        try {
//                                                                            firstEmbedding = FaceVerificationUtils.createEmbedding(bitmap)
//                                                                            showCameraPreview = false
//                                                                            Toast.makeText(context, "Blink Detected & Face Captured", Toast.LENGTH_SHORT).show()
//                                                                        } catch (e: Exception) {
//                                                                            e.printStackTrace()
//                                                                        }
//                                                                    }
//                                                                }
//                                                            }
//                                                        }
//                                                    }
//
//                                                    // cleanup stale states (not seen for >5s)
//                                                    val now = System.currentTimeMillis()
//                                                    val stale = blinkStates.filterValues { now - it.lastSeenMs > 5_000L }.keys
//                                                    stale.forEach { blinkStates.remove(it) }
//                                                }
//                                            }
//                                    )
//
//                                } else {
//                                    permissionLauncher.launch(
//                                        Manifest.permission.CAMERA
//                                    )
//                                }
//                            }
//                    }
//                }
//            }
//
//
//            is Resource.Error -> {
//                showLoading = false
//
//            }
//
//            is Resource.Loading -> {
//                showLoading = false
//                Log.d(
//                    "PROFILE",
//                    "Loading..."
//                )
//            }
//
//            else -> {
//                showLoading = false
//            }
//        }
//    }
//    if (showLoading) {
//        Dialog(onDismissRequest = { }) {
//            Box(
//                modifier = Modifier
//                    .size(90.dp)
//                    .background(Color.White, RoundedCornerShape(12.dp)),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator(
//                    color = Color.Blue
//                )
//            }
//        }
//    }
//
//
//
//
//    LaunchedEffect(firstEmbedding) {
//        while (firstEmbedding != null) {
//            delay(2000)
//
//            FaceVerificationUtils.captureImage(
//                imageCapture = imageCapture,
//                cameraExecutor = cameraExecutor,
//                context = context
//            ) { bitmap ->
//
//                val currentEmbedding =
//                    FaceVerificationUtils.createEmbedding(bitmap)
//
//                val savedEmbedding = firstEmbedding
//
//                if (savedEmbedding != null) {
//                    val distance =
//                        FaceVerificationUtils.compareEmbeddings(
//                            savedEmbedding,
//                            currentEmbedding
//                        )
//
//                    if (distance < 2000) {
//
////                        val request =
////                            QuestiontReq(category = "category",)
//                        Toast.makeText(
//                            context,
//                            DepartMentpedesc,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        questionViewModel.fetchQuestions(category = DepartMentpedesc)
//                        showDialog = false
//                        blurScreen = false
//                    }
//
//                    else {
////                        resultText = "Face Not Matched"
//                        blurScreen = true
//                        showDialog = true
//                    }
//                }
//            }
//        }
//    }
//
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .then(
//                    if (blurScreen) {
//                        Modifier.graphicsLayer {
//                            alpha = 0.3f
//                        }
//                    } else {
//                        Modifier
//                    }
//                ),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            if (showCameraPreview) {
//                AndroidView(
//                    factory = {
//                        previewView
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(10.dp))
//            if (showButton) {
//
//                Button(
//                    onClick = {
////                        showButton = false
//                        profileViewModel.getProfile(
//                            appVersion = currentVersion,
//                            loginId = currentLoginId,
//                            email = currentEmail
//                        )
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(10.dp)
//                ) {
//                    Text("Capture First Face")
//                }
//            }
//        }
//
//        if (showDialog) {
//            VibrateWhileDialogVisible(showDialog)
//
//            AlertDialog(
//                onDismissRequest = {},
//                title = {
//                    Text("Security Alert")
//                },
//                text = {
//                    Text("Face Not Matched or Object Detected")
//                },
//                confirmButton = {
//                    Button(
//                        onClick = {
//                            showDialog = false
//                            blurScreen = false
//                        }
//                    ) {
//                        Text("OK")
//                    }
//                }
//            )
//        }
//
//}
//
//}
