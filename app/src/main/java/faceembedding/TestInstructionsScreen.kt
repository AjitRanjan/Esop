package faceembedding


import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.esop.network.AppPreferences
import com.example.esop.network.Resource
import com.example.esop.quetions_esop.Question
import com.example.esop.quetions_esop.QuestionUiState
import com.example.esop.quetions_esop.QuestionViewModel
import com.example.esop.vibrate.FaceVerificationUtils
import java.util.concurrent.Executors


@Composable
fun TestInstructionsScreen(
    navController: NavController,
    appPreferences: AppPreferences,
    onStartTestClick: () -> Unit = {},
    onGoBackClick: () -> Unit = {},
    questionViewModel: QuestionViewModel = viewModel()
) {

    val context = LocalContext.current
    val typedesc by appPreferences.usertypedesc.collectAsState(initial = "")
    val questionState = questionViewModel.uiState
    var totalQuestions by remember { mutableIntStateOf(0) }








    LaunchedEffect(questionState) {

        when (questionState) {

            is QuestionUiState.Success -> {

                val response =
                    (questionState as QuestionUiState.Success)
                        .response



                totalQuestions =
                    response?.summary?.totalQuestions?: 0


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




//    LaunchedEffect(questionState) {
//
//        when (val state = questionState) {
//
//            is Resource.Success -> {
//
//                val response = state.data
//
//                questionList = response?.Questions ?: emptyList()
//                questionList =
//                    state.data?.Questions ?: emptyList()
//                questionList = state.data?.Questions ?: emptyList()
//
//
//
//
////                Log.d("QUESTION_COUNT", questionList.size.toString())
//
//
//                // Summary Data
//                totalQuestions = response?.summary?.totalQuestions ?: 0
//                easyCount = response?.summary?.easyCount ?: 0
//                mediumCount = response?.summary?.mediumCount ?: 0
//                hardCount = response?.summary?.hardCount ?: 0
//                numberofAttempt = response?.summary?.numberofAttempt ?: 0
//
//                easyPercentage = response?.summary?.easyPercentage ?: 0.0
//                mediumPercentage = response?.summary?.mediumPercentage ?: 0.0
//                hardPercentage = response?.summary?.hardPercentage ?: 0.0
//
//                Log.d("QUESTION_COUNT", questionList.size.toString())
//
//                Log.d("TOTAL_QUESTIONS", totalQuestions.toString())
//                Log.d("EASY_COUNT", easyCount.toString())
//                Log.d("MEDIUM_COUNT", mediumCount.toString())
//                Log.d("HARD_COUNT", hardCount.toString())
//
//                Log.d("EASY_PERCENTAGE", easyPercentage.toString())
//                Log.d("MEDIUM_PERCENTAGE", mediumPercentage.toString())
//                Log.d("HARD_PERCENTAGE", hardPercentage.toString())
//
//                questionList.forEach {
//                    Log.d("QUESTION_DATA", it.toString())
//                }
//
//            }
//
//            is Resource.Error -> {
//                Toast.makeText(
//                    context,
//                    state.message,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            is Resource.Loading -> {
//                Log.d("QUESTION_LOADING", "Loading...")
//            }
//
//            else -> {}
//        }
//    }










    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    )


    {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Test Instructions",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        Spacer(modifier = Modifier.height(28.dp))

        InstructionItem(
            icon = Icons.Default.Assignment,
            title = "Total Questions",
            value = totalQuestions.toString()
        )

//        InstructionItem(
//            icon = Icons.Default.EmojiEvents,
//            title = "Total Marks",
//            value = "50"
//        )

        InstructionItem(
            icon = Icons.Default.AccessTime,
            title = "Time Duration",
            value = "30 Minutes"
        )

//        InstructionItem(
//            icon = Icons.Default.IndeterminateCheckBox,
//            title = "Negative Marking",
//            value = "No"
//        )

        InstructionItem(
            icon = Icons.Default.EditNote,
            title = "Passing Marks",
            value = "70%"
        )


        InstructionItem(
            icon = Icons.Default.ForkRight,
            title = "Hard Questions",
            value = "30%",
//            showCard = false
        )

        InstructionItem(
            icon = Icons.Default.ForkRight,
            title = "Medium Questions",
            value = "40%",
//            showCard = false
        )
        InstructionItem(
            icon = Icons.Default.ForkRight,
            title = "Low Questions",
            value = "30%",
//            showCard = false
        )

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                navController.navigate(
                    "TestScreen"
                )
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),

            shape = RoundedCornerShape(8.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0B5EF7)
            )
        ) {

            Text(
                text = "Start New Test",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

        questionViewModel.fetchQuestions(category = typedesc)


}
@Composable
private fun InstructionItem(
    icon: ImageVector,
    title: String,
    value: String,
    showCard: Boolean = true
) {
    val content: @Composable () -> Unit = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF2563EB),
                modifier = Modifier.size(34.dp)
            )

            Spacer(modifier = Modifier.width(22.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color(0xFF374151),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = value,
                    fontSize = 15.sp,
                    color = Color(0xFF111827),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showCard) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF8F9FD)
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            content()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
        ) {
            content()
        }
    }
}