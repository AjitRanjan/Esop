package com.example.esop
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
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




import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel








// ─────────────────────────────────────────────
// Color palette
// ─────────────────────────────────────────────
private val BackgroundPage = Color(0xFFF5F5F5)
private val CardWhite      = Color(0xFFFFFFFF)
private val TextPrimary    = Color(0xFF1A1A2E)
private val TextMuted      = Color(0xFF888888)
private val DividerColor   = Color(0xFFF0F0F0)
private val GreenText      = Color(0xFF1A9650)
private val GreenBg        = Color(0xFFEAFAF1)
private val RedText        = Color(0xFFC0392B)
private val RedBg          = Color(0xFFFDECEA)
private val BlueLight      = Color(0xFFE8F4FD)
private val GreenLight     = Color(0xFFEAFAF1)

// ─────────────────────────────────────────────
// Screen
// ─────────────────────────────────────────────
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTest(
    navController: NavController,
    appPreferences: AppPreferences,
    resultViewModel: ResultViewModel = viewModel()
) {
    val getResult     = resultViewModel.state
    val userEmail by appPreferences.userEmail.collectAsState(initial = "")
    val loginId   by appPreferences.loginId.collectAsState(initial = "")

    var expanded            by remember { mutableStateOf(false) }
    var selectedDepartment  by remember { mutableStateOf("Finance") }
    var showEmptyDialog     by remember { mutableStateOf(false) }

    val departments = listOf("Finance", "Operations")

    LaunchedEffect(Unit) {
        resultViewModel.GetResult(
            ResultGetReq(
                loginId = loginId.toString(),
                emailId = userEmail.toString()
            )
        )
    }

    if (showEmptyDialog) {
        AlertDialog(
            onDismissRequest = {},
            title   = { Text("No Result Available") },
            text    = { Text("Please conduct the exam first.") },
            confirmButton = {
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text       = "My Test",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CardWhite
                )
            )
        },
        containerColor = BackgroundPage
    ) { padding ->

        when (val state = getResult) {

            is ResultExamState.Loading -> {
                Box(
                    modifier         = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ResultExamState.Success -> {

                val resultList = state.response.wrappedList ?: emptyList()

                LaunchedEffect(resultList) {
                    if (resultList.isEmpty()) showEmptyDialog = true
                }

                // ✅ Direct filter — no "All" check
                val filteredList = resultList.filter {
                    it.departmentCetegory?.equals(selectedDepartment, ignoreCase = true) == true
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {

                    // ── Dropdown ──────────────────────────
                    ExposedDropdownMenuBox(
                        expanded         = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value         = selectedDepartment,
                            onValueChange = {},
                            readOnly      = true,
                            colors        = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor   = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor        = Color.Black,
                                unfocusedTextColor      = Color.Black,
                                focusedBorderColor      = Color.Gray,
                                unfocusedBorderColor    = Color.LightGray
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
                            expanded         = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            departments.forEach { department ->
                                DropdownMenuItem(
                                    text    = { Text(text = department, color = Color.Black) },
                                    onClick = {
                                        selectedDepartment = department
                                        expanded           = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // ── Cards ─────────────────────────────
                    if (filteredList.isNotEmpty()) {
                        LazyColumn(
                            modifier       = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical   = 4.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(
                                items = filteredList,
                                key   = { filteredList.indexOf(it).toString() }
                            ) { item ->
                                MyTestCard(item)
                            }
                        }
                    } else {
                        // ✅ Agar us department ka koi data nahi
                        Box(
                            modifier         = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text      = "No Result Found",
                                    fontSize  = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color     = TextPrimary
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text     = "No data available for $selectedDepartment department",
                                    fontSize = 13.sp,
                                    color    = TextMuted
                                )
                            }
                        }
                    }
                }
            }
//            is ResultExamState.Success -> {
//
//                val resultList = state.response.wrappedList ?: emptyList()
//
//                LaunchedEffect(resultList) {
//                    if (resultList.isEmpty()) showEmptyDialog = true
//                }
//
//                // ✅ Dropdown se selected department ka item filter karo
//                val filteredItem = resultList.firstOrNull {
//                    it.departmentCetegory?.equals(selectedDepartment, ignoreCase = true) == true
//                }
//
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(padding)
//                ) {
//
//                    // ── Dropdown ──────────────────────────
//                    ExposedDropdownMenuBox(
//                        expanded        = expanded,
//                        onExpandedChange = { expanded = !expanded }
//                    ) {
//                        OutlinedTextField(
//                            value         = selectedDepartment,
//                            onValueChange = {},
//                            readOnly      = true,
//                            colors        = OutlinedTextFieldDefaults.colors(
//                                focusedContainerColor   = Color.White,
//                                unfocusedContainerColor = Color.White,
//                                focusedTextColor        = Color.Black,
//                                unfocusedTextColor      = Color.Black,
//                                focusedBorderColor      = Color.Gray,
//                                unfocusedBorderColor    = Color.LightGray
//                            ),
//                            label = {
//                                Text(text = "Select Department", color = Color.Black)
//                            },
//                            trailingIcon = {
//                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(start = 16.dp, end = 16.dp, top = 10.dp)
//                                .menuAnchor()
//                        )
//
//                        ExposedDropdownMenu(
//                            expanded        = expanded,
//                            onDismissRequest = { expanded = false }
//                        ) {
//                            departments.forEach { department ->
//                                DropdownMenuItem(
//                                    text    = { Text(text = department, color = Color.Black) },
//                                    onClick = {
//                                        selectedDepartment = department
//                                        expanded           = false
//                                    }
//                                )
//                            }
//                        }
//                    }
//
//                    Spacer(Modifier.height(12.dp))
//
//                    // ── Card ya No Data message ───────────
//                    if (filteredItem != null) {
//                        LazyColumn(
//                            modifier       = Modifier.fillMaxSize(),
//                            contentPadding = PaddingValues(
//                                horizontal = 16.dp,
//                                vertical   = 4.dp
//                            ),
//                            verticalArrangement = Arrangement.spacedBy(14.dp)
//                        ) {
//                            item {
//                                MyTestCard(filteredItem)
//                            }
//                        }
//                    } else {
//                        // ✅ Agar selected department ka data nahi hai
//                        Box(
//                            modifier         = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text  = "No data available for $selectedDepartment",
//                                color = TextMuted
//                            )
//                        }
//                    }
//                }
//            }

            is ResultExamState.Error -> {
                Box(
                    modifier         = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text  = state.message,
                        color = RedText
                    )
                }
            }

            else -> {}
        }
    }
}



// ─────────────────────────────────────────────
// Card
// ─────────────────────────────────────────────

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyTestCard(item: WrappedResulttem) {

    // ✅ Saari values null-safe
    val department = item.departmentCetegory ?: "Unknown"
    val resultDate = item.resultdate ?: ""
    val totalQ     = item.totalQuestion ?: "0"
    val correctA   = item.correctAns ?: "0"
    val wrongA     = item.wrongAns ?: "0"
    val scoredPer  = item.scoredPercentage ?: "0"
    val passingPer = item.passingPercentage ?: "0"
    val finalRes   = item.finalResult ?: ""

    val isPassed = finalRes.equals("1", ignoreCase = true)
            || finalRes.equals("pass", ignoreCase = true)

    val scoreInt = scoredPer.toIntOrNull() ?: 0
    val passInt  = passingPer.toIntOrNull() ?: 0

    // ✅ Null-safe department check
    val deptIconBg = if (department.equals("Finance", ignoreCase = true))
        BlueLight else GreenLight

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(18.dp),
        colors    = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            // ── Header ────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier          = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier         = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(deptIconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text     = if (department.equals("Finance", ignoreCase = true)) "💼" else "⚙️",
                        fontSize = 20.sp
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = department,  // ✅ null-safe
                        fontWeight = FontWeight.Bold,
                        fontSize   = 16.sp,
                        color      = TextPrimary
                    )
                    if (resultDate.isNotEmpty()) {  // ✅ null-safe
                        Text(
                            text     = formatDateTime(resultDate),
                            fontSize = 12.sp,
                            color    = TextMuted
                        )
                    }
                }

                // Pass/Fail badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isPassed) GreenBg else RedBg)
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                ) {
                    Text(
                        text       = if (isPassed) "PASS" else "FAIL",
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color      = if (isPassed) GreenText else RedText
                    )
                }
            }

            HorizontalDivider(
                modifier  = Modifier.padding(vertical = 14.dp),
                thickness = 1.dp,
                color     = DividerColor
            )

            // ── Data rows ─────────────────────────────
            ResultRow("Total Questions", totalQ)
            ResultRow("Correct Answers", correctA,   valueColor = GreenText)
            ResultRow("Wrong Answers",   wrongA,     valueColor = RedText)
            ResultRow("Passing %",       "$passingPer%")

            HorizontalDivider(
                modifier  = Modifier.padding(vertical = 10.dp),
                thickness = 1.dp,
                color     = DividerColor
            )

            // ── Score + Progress bar ──────────────────
            ResultRow(
                title      = "Score",
                value      = "$scoreInt%",
                valueColor = if (scoreInt >= passInt) GreenText else RedText
            )

            Spacer(Modifier.height(8.dp))

            LinearProgressIndicator(
                progress   = { scoreInt / 100f },
                modifier   = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color      = if (scoreInt >= passInt) GreenText else RedText,
                trackColor = DividerColor
            )
        }
    }
}
//fun MyTestCard(item: WrappedResulttem) {
//
//    val isPassed = item.finalResult.equals("1", ignoreCase = true)
//            || item.finalResult.equals("pass", ignoreCase = true)
//
//    val scoreInt = item.scoredPercentage.toIntOrNull() ?: 0
//    val passInt  = item.passingPercentage.toIntOrNull() ?: 0
//
//    val deptIconBg = if (item.departmentCetegory.equals("Finance", ignoreCase = true))
//        BlueLight else GreenLight
//
//    Card(
//        modifier  = Modifier.fillMaxWidth(),
//        shape     = RoundedCornerShape(18.dp),
//        colors    = CardDefaults.cardColors(containerColor = CardWhite),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Column(modifier = Modifier.padding(18.dp)) {
//
//            // ── Header ────────────────────────────────
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier          = Modifier.fillMaxWidth()
//            ) {
//                Box(
//                    modifier         = Modifier
//                        .size(44.dp)
//                        .clip(RoundedCornerShape(12.dp))
//                        .background(deptIconBg),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text     = if (item.departmentCetegory
//                                .equals("Finance", ignoreCase = true)) "💼" else "⚙️",
//                        fontSize = 20.sp
//                    )
//                }
//
//                Spacer(Modifier.width(12.dp))
//
//                Column(modifier = Modifier.weight(1f)) {
//                    Text(
//                        text       = item.departmentCetegory,
//                        fontWeight = FontWeight.Bold,
//                        fontSize   = 16.sp,
//                        color      = TextPrimary
//                    )
//                    if (item.resultdate.isNotEmpty()) {
//                        Text(
//                            text     = item.resultdate,
//                            fontSize = 12.sp,
//                            color    = TextMuted
//                        )
//                    }
//                }
//
//                // Pass/Fail badge
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(20.dp))
//                        .background(if (isPassed) GreenBg else RedBg)
//                        .padding(horizontal = 14.dp, vertical = 5.dp)
//                ) {
//                    Text(
//                        text       = if (isPassed) "PASS" else "FAIL",
//                        fontSize   = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        color      = if (isPassed) GreenText else RedText
//                    )
//                }
//            }
//
//            HorizontalDivider(
//                modifier  = Modifier.padding(vertical = 14.dp),
//                thickness = 1.dp,
//                color     = DividerColor
//            )
//
//            // ── Data rows ─────────────────────────────
//            ResultRow("Total Questions", item.totalQuestion)
//            ResultRow("Correct Answers", item.correctAns, valueColor = GreenText)
//            ResultRow("Wrong Answers",   item.wrongAns,   valueColor = RedText)
//            ResultRow("Passing %",       "${item.passingPercentage}%")
//
//            HorizontalDivider(
//                modifier  = Modifier.padding(vertical = 10.dp),
//                thickness = 1.dp,
//                color     = DividerColor
//            )
//
//            // ── Score + Progress bar ──────────────────
//            ResultRow(
//                title      = "Score",
//                value      = "$scoreInt%",
//                valueColor = if (scoreInt >= passInt) GreenText else RedText
//            )
//
//            Spacer(Modifier.height(8.dp))
//
//            LinearProgressIndicator(
//                progress   = { scoreInt / 100f },
//                modifier   = Modifier
//                    .fillMaxWidth()
//                    .height(8.dp)
//                    .clip(RoundedCornerShape(6.dp)),
//                color      = if (scoreInt >= passInt) GreenText else RedText,
//                trackColor = DividerColor
//            )
//        }
//    }
//}

// ─────────────────────────────────────────────
// Reusable row
// ─────────────────────────────────────────────
@Composable
fun ResultRow(
    title      : String,
    value      : String,
    valueColor : Color = TextPrimary
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text     = title,
            fontSize = 13.sp,
            color    = TextMuted
        )
        Text(
            text       = value,
            fontSize   = 13.sp,
            fontWeight = FontWeight.Bold,
            color      = valueColor
        )
    }
}
// ─────────────────────────────────────────────
// Color palette
// ─────────────────────────────────────────────
//private val BackgroundPage   = Color(0xFFF5F5F5)
//private val CardWhite        = Color(0xFFFFFFFF)
//private val TextPrimary      = Color(0xFF1A1A2E)
//private val TextMuted        = Color(0xFF888888)
//private val Divider          = Color(0xFFF0F0F0)
//private val GreenText        = Color(0xFF1A9650)
//private val GreenBg          = Color(0xFFEAFAF1)
//private val RedText          = Color(0xFFC0392B)
//private val RedBg            = Color(0xFFFDECEA)
//private val BlueLight        = Color(0xFFE8F4FD)
//private val GreenLight       = Color(0xFFEAFAF1)
//
//// ─────────────────────────────────────────────
//// Screen
//// ─────────────────────────────────────────────
//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MyTest(
//    navController: NavController,
//    appPreferences: AppPreferences,
//    resultViewModel: ResultViewModel = viewModel()
//) {
//    val getResult             = resultViewModel.state
//    val userEmail by appPreferences.userEmail.collectAsState(initial = "")
//    val loginId   by appPreferences.loginId.collectAsState(initial = "")
//
//    var resultList by remember { mutableStateOf<List<WrappedResulttem>>(emptyList()) }
//    var showEmptyDialog by remember { mutableStateOf(false) }
//
//    // Fetch on launch
//    LaunchedEffect(Unit) {
//        resultViewModel.GetResult(
//            ResultGetReq(
//                loginId = loginId.toString(),
//                emailId = userEmail.toString()
//            )
//        )
//    }
//
//    // Empty-result dialog
//    if (showEmptyDialog) {
//        AlertDialog(
//            onDismissRequest = {},
//            title   = { Text("No Result Available") },
//            text    = { Text("Please conduct the exam first.") },
//            confirmButton = {
//                TextButton(onClick = { navController.popBackStack() }) {
//                    Text("OK")
//                }
//            }
//        )
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text       = "My Test",
//                        fontWeight = FontWeight.Bold,
//                        fontSize   = 20.sp
//                    )
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = CardWhite
//                )
//            )
//        },
//        containerColor = BackgroundPage
//    ) { padding ->
//
//        when (val state = getResult) {
//
//            // ── Loading ───────────────────────────────
//            is ResultExamState.Loading -> {
//                Box(
//                    modifier          = Modifier.fillMaxSize(),
//                    contentAlignment  = Alignment.Center
//                ) {
//                    CircularProgressIndicator()
//                }
//            }
//
//            // ── Success ───────────────────────────────
//            is ResultExamState.Success -> {
//
//                // ✅ Apne ViewModel ke field name se match karo
//                // jaise state.data ya state.resultList
//                resultList = state.data
//
//                LaunchedEffect(resultList) {
//                    if (resultList.isEmpty()) showEmptyDialog = true
//                }
//
//                LazyColumn(
//                    modifier        = Modifier
//                        .fillMaxSize()
//                        .padding(padding),
//                    contentPadding  = PaddingValues(
//                        horizontal = 16.dp,
//                        vertical   = 12.dp
//                    ),
//                    verticalArrangement = Arrangement.spacedBy(14.dp)
//                ) {
//                    items(
//                        items = resultList,
//                        key   = { it.departmentCetegory }   // unique key for performance
//                    ) { item ->
//                        MyTestCard(item)
//                    }
//                }
//            }
//
//            // ── Error ─────────────────────────────────
//            is ResultExamState.Error -> {
//                Box(
//                    modifier         = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text  = state.message,
//                        color = RedText
//                    )
//                }
//            }
//
//            else -> {}
//        }
//    }
//}
//
//// ─────────────────────────────────────────────
//// Card
//// ─────────────────────────────────────────────
//@Composable
//fun MyTestCard(item: WrappedResulttem) {
//
//    val isPassed   = item.finalResult.equals("1", ignoreCase = true)
//            || item.finalResult.equals("pass", ignoreCase = true)
//
//    val scoreInt   = item.scoredPercentage.toIntOrNull() ?: 0
//    val passInt    = item.passingPercentage.toIntOrNull() ?: 0
//
//    val deptIconBg = if (item.departmentCetegory.equals("Finance", ignoreCase = true))
//        BlueLight else GreenLight
//
//    Card(
//        modifier  = Modifier.fillMaxWidth(),
//        shape     = RoundedCornerShape(18.dp),
//        colors    = CardDefaults.cardColors(containerColor = CardWhite),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Column(modifier = Modifier.padding(18.dp)) {
//
//            // ── Header row ────────────────────────────
//            Row(
//                verticalAlignment    = Alignment.CenterVertically,
//                modifier             = Modifier.fillMaxWidth()
//            ) {
//                // Department icon box
//                Box(
//                    modifier         = Modifier
//                        .size(44.dp)
//                        .clip(RoundedCornerShape(12.dp))
//                        .background(deptIconBg),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text     = if (item.departmentCetegory
//                                .equals("Finance", ignoreCase = true)) "💼" else "⚙️",
//                        fontSize = 20.sp
//                    )
//                }
//
//                Spacer(Modifier.width(12.dp))
//
//                Column(modifier = Modifier.weight(1f)) {
//                    Text(
//                        text       = item.departmentCetegory,
//                        fontWeight = FontWeight.Bold,
//                        fontSize   = 16.sp,
//                        color      = TextPrimary
//                    )
//                    if (item.resultdate.isNotEmpty()) {
//                        Text(
//                            text     = item.resultdate,
//                            fontSize = 12.sp,
//                            color    = TextMuted
//                        )
//                    }
//                }
//
//                // Pass / Fail badge
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(20.dp))
//                        .background(if (isPassed) GreenBg else RedBg)
//                        .padding(horizontal = 14.dp, vertical = 5.dp)
//                ) {
//                    Text(
//                        text       = if (isPassed) "PASS" else "FAIL",
//                        fontSize   = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        color      = if (isPassed) GreenText else RedText
//                    )
//                }
//            }
//
//            // ── Divider ───────────────────────────────
//            HorizontalDivider(
//                modifier  = Modifier.padding(vertical = 14.dp),
//                thickness = 1.dp,
//                color     = Divider
//            )
//
//            // ── Data rows ─────────────────────────────
//            ResultRow("Total Questions", item.totalQuestion)
//            ResultRow("Correct Answers", item.correctAns,  valueColor = GreenText)
//            ResultRow("Wrong Answers",   item.wrongAns,    valueColor = RedText)
//            ResultRow("Passing %",       "${item.passingPercentage}%")
//
//            HorizontalDivider(
//                modifier  = Modifier.padding(vertical = 10.dp),
//                thickness = 1.dp,
//                color     = Divider
//            )
//
//            // ── Score % with progress bar ─────────────
//            ResultRow(
//                title      = "Score",
//                value      = "$scoreInt%",
//                valueColor = if (scoreInt >= passInt) GreenText else RedText
//            )
//
//            Spacer(Modifier.height(8.dp))
//
//            LinearProgressIndicator(
//                progress            = { scoreInt / 100f },
//                modifier            = Modifier
//                    .fillMaxWidth()
//                    .height(8.dp)
//                    .clip(RoundedCornerShape(6.dp)),
//                color               = if (scoreInt >= passInt) GreenText else RedText,
//                trackColor          = Divider
//            )
//        }
//    }
//}
//
//// ─────────────────────────────────────────────
//// Reusable row
//// ─────────────────────────────────────────────
//@Composable
//fun ResultRow(
//    title      : String,
//    value      : String,
//    valueColor : Color = TextPrimary
//) {
//    Row(
//        modifier             = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 5.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment     = Alignment.CenterVertically
//    ) {
//        Text(
//            text     = title,
//            fontSize = 13.sp,
//            color    = TextMuted
//        )
//        Text(
//            text       = value,
//            fontSize   = 13.sp,
//            fontWeight = FontWeight.Bold,
//            color      = valueColor
//        )
//    }
//}
