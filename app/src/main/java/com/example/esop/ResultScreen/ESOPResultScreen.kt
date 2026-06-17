package faceembedding
import android.os.Build
import android.security.identity.ResultData
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.esop.AswersOptionSubmit.Repositry.InsertExamState
import com.example.esop.Result.ResultExamState
import com.example.esop.Result.ResultGetReq
import com.example.esop.Result.ResultViewModel
import com.example.esop.fialAnsweredSubmitApi.FinalInsertViewModel
import com.example.esop.fialAnsweredSubmitApi.ResultInsertReq
import com.example.esop.network.AppPreferences


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.esop.Result.WrappedResulttem
import com.example.esop.login.UserDataStore
import com.example.esop.util.formatDateTime
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ESOPResultScreen(
    navController: NavController,
    appPreferences: AppPreferences,
    onViewCertificateClick: () -> Unit = {},
    onBackToHomeClick: () -> Unit = {},
    resultViewModel: ResultViewModel = viewModel()
) {
    val context = LocalContext.current
    val getResult = resultViewModel.state
    val userEmail by appPreferences.userEmail.collectAsState(initial = "")
    val loginId by appPreferences.loginId.collectAsState(initial = "")

    // ---------- State ----------
    var expanded by remember { mutableStateOf(false) }
    var resultList by remember { mutableStateOf<List<WrappedResulttem>>(emptyList()) }
    var selectedDepartment by remember { mutableStateOf("Finance") }

    val departments = listOf("Finance", "Operations")

    // Derived: filter list by selected department
    val filteredResult: WrappedResulttem? = remember(resultList, selectedDepartment) {
        resultList.firstOrNull {
            it.departmentCetegory.equals(selectedDepartment, ignoreCase = true)
        }
    }
    var examNotAttempted by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    // Extract values from filtered result (or default to 0 / empty)
    val resultdate = filteredResult?.resultdate ?: ""
//    val departmentCetegory = filteredResult?.departmentCetegory ?: ""
    val totalQuestions = filteredResult?.totalQuestion?.toIntOrNull() ?: 0
    val correctAns = filteredResult?.correctAns?.toIntOrNull() ?: 0
    val wrongAns = filteredResult?.wrongAns?.toIntOrNull() ?: 0
    val percentage = filteredResult?.scoredPercentage?.toIntOrNull() ?: 0
    val result = filteredResult?.finalResult?.toIntOrNull() ?: 0
    var showDialogExam by remember { mutableStateOf(false) }
    // ---------- Fetch on launch ----------
    LaunchedEffect(Unit) {
        val request = ResultGetReq(
            loginId = loginId.toString(),
            emailId = userEmail.toString()
        )
        resultViewModel.GetResult(request)
    }

    if (showDialogExam) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text("No Result is Available")
            },
            text = {
                Text(
                    "Please Conduct Exam"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        navController.popBackStack()
//                        showDialogExam = false
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
    // ---------- Handle API response ----------
    LaunchedEffect(getResult) {
        when (getResult) {
            is ResultExamState.Loading -> {
                Log.d("SUBMIT_LOADING", "Loading...")
            }

            is ResultExamState.Success -> {

                val response = getResult.response

                if (response.wrappedList.isNotEmpty()) {

                    // Finance ka last record
                    val financeLast =
                        response.wrappedList
                            .filter {
                                it.departmentCetegory.equals(
                                    "Finance",
                                    ignoreCase = true
                                )
                            }
                            .lastOrNull()

                    // Operation ka last record
                    val operationLast =
                        response.wrappedList
                            .filter {
                                it.departmentCetegory.equals(
                                    "Operations",
                                    ignoreCase = true
                                )
                            }
                            .lastOrNull()

                    // Sirf latest Finance + latest Operation
                    resultList = listOfNotNull(
                        financeLast,
                        operationLast
                    )

                    val availableDepts =
                        resultList
                            .map {
                                it.departmentCetegory
                            }
                            .distinct()

                    if (
                        availableDepts.isNotEmpty() &&
                        availableDepts.none {
                            it.equals(
                                selectedDepartment,
                                ignoreCase = true
                            )
                        }
                    ) {

                        selectedDepartment =
                            availableDepts.first()
                    }
                }

                else{
                    examNotAttempted = true
                    showDialogExam = true

                }
            }

            is ResultExamState.Error -> {
                Toast.makeText(context, getResult.message, Toast.LENGTH_LONG).show()
                Log.d("SUBMIT_ERROR", getResult.message)
            }

            else -> {}
        }
    }

    // ---------- Derived display values ----------
    val score = "$correctAns / $totalQuestions"
//    val resultText = if (result == 0) "Failed" else "Passed"
    val resultText = when {
        examNotAttempted -> "Not Attempted"
        result == 0 -> "Failed"
        else -> "Passed"
    }
    val resultColor = if (result == 0) Color.Red else Color(0xFF95DD31)

    // ---------- UI ----------
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Result",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 26.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (resultText == "Passed") {
//                if (resultText == "Failed") {
                    Button(
                        onClick = {
                            scope.launch {

                                appPreferences.saveResult(
                                    totalQuestions = totalQuestions,
                                    wrongAns = wrongAns,
                                    notAttempted = 0,
                                    percentage = percentage,
                                    correctAns = correctAns,
                                    result = result
                                )
                            }

                            navController.navigate("ESOPCertificateScreen") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF075CE8))
                    )
                    {
                        Text(
                            text = "View Certificate",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {

            // ---------- Dropdown ----------
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            )
            {
                OutlinedTextField(
                    value = selectedDepartment,
                    onValueChange = {},
                    readOnly = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    label = {
                        Text(text = "Select Department", color = Color.Black)
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 10.dp)
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    departments.forEach { department ->
                        DropdownMenuItem(
                            text = { Text(text = department, color = Color.Black) },
                            onClick = {
                                selectedDepartment = department
                                expanded = false
                                // No need to call API again — just filter existing list
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            // ---------- Result Content ----------
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(430.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF062C63), Color(0xFF031A3F))
                            )
                        )
                ) {
                    Text(
                        text = "Your Result",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 20.dp)
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(bottom = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ResultProgress(
                            percentage = percentage,
                            score = score,
                            modifier = Modifier.size(160.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (result == 0) "Sorry! You " else "Congratulations! You ",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = resultText,
                                color = resultColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (result != 0) {
                                Text(
                                    text = " 🎉",
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (result == 0)
                                "Please try again to improve your score."
                            else
                                "Well done! You have successfully",
                            color = Color.White,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(formatDateTime(resultdate),
//                        Text("Time and Date"+ formatDateTime("2026-06-13T15:28:44.360+00:00"),
                            color = Color.White, fontSize = 13.sp

                        )

                        if (result != 0) {
                            Text(
                                text = "cleared the test.",
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                ResultStatsCard(

                    correct = correctAns.toString(),

                    incorrect = wrongAns.toString(),

                    score = score,

                    rank = when {
                        examNotAttempted -> "NA"
                        result == 0 -> "Fail"
                        else -> "Pass"
                    },

                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(
                            start = 25.dp,
                            end = 25.dp,
                            bottom = 10.dp
                        )
                )

//                ResultStatsCard(
//                    correct = correctAns.toString(),
//                    incorrect = wrongAns.toString(),
//                    score = score,
//                    rank = if (result == 0) "Fail" else "Pass",
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .padding(start = 25.dp, end = 25.dp, bottom = 10.dp)
//                )
            }
        }
    }
}


@Composable
private fun ResultProgress(
    percentage: Int,
    score: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 14.dp.toPx()
            val startAngle = 140f
            val sweepAngle = 260f

            drawArc(
                color = Color(0xFF1BB2AA),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                color = Color(0xFF95DD31),
                startAngle = startAngle,
                sweepAngle = sweepAngle * (percentage / 100f),
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$percentage%",
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = score,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
private fun ResultStatsCard(
    correct: String,
    incorrect: String,
    score: String,
    rank: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FB)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ResultStatItem(title = "Correct", value = correct, valueColor = Color(0xFF19A64A))
            ResultStatItem(title = "Incorrect", value = incorrect, valueColor = Color(0xFFE53935))
            ResultStatItem(title = "Score", value = score, valueColor = Color(0xFF1D9BF0))
            ResultStatItem(title = "Result", value = rank, valueColor = Color(0xFF111827))
        }
    }
}


@Composable
private fun ResultStatItem(
    title: String,
    value: String,
    valueColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            color = Color(0xFF4B5563),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = valueColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}





