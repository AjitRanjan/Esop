package faceembedding




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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.esop.network.AppPreferences

//@Composable
//fun ESOPResultScreen(
//    navController: NavController,
//    percentage: Int = 76,
//    correct: String = "40",
//    incorrect: String = "10",
//    score: String = "38 / 50",
//    rank: String = "---",
//    onViewCertificateClick: () -> Unit = {},
//    onBackToHomeClick: () -> Unit = {}
//) {
//    Scaffold(
//        containerColor = Color.White,
//        bottomBar = {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color.White)
//                    .padding(horizontal = 26.dp, vertical = 16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Button(
//                    onClick = onViewCertificateClick,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(48.dp),
//                    shape = RoundedCornerShape(6.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF075CE8)
//                    )
//                ) {
//
//                    Text(
//                        text = "View Certificate",
//                        color = Color.White,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.clickable {
//                            navController.navigate("ESOPCertificateScreen")
//                        }
//                    )
////                    Text(
////                        text = "View Certificate",
////                        color = Color.White,
////                        fontSize = 16.sp,
////                        fontWeight = FontWeight.Bold
////                    )
//                }
//
//                Spacer(modifier = Modifier.height(18.dp))
//
//                Text(
//                    text = "Back to Home",
//                    color = Color(0xFF2563EB),
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.clickable {
//                        onBackToHomeClick()
//                    }
//                )
//            }
//        }
//    ) { paddingValues ->
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues),
//            contentAlignment = Alignment.Center
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(360.dp)
//                    .background(
//                        brush = Brush.verticalGradient(
//                            colors = listOf(
//                                Color(0xFF062C63),
//                                Color(0xFF031A3F)
//                            )
//                        )
//                    )
//                    .padding(horizontal = 20.dp, vertical = 20.dp)
//            ) {
//                Text(
//                    text = "Your Result",
//                    color = Color.White,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.align(Alignment.TopCenter)
//                )
//
//                Column(
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .padding(top = 12.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    ResultProgress(
//                        percentage = percentage,
//                        score = score,
//                        modifier = Modifier.size(150.dp)
//                    )
//
//                    Spacer(modifier = Modifier.height(3.dp))
//
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Text(
//                            text = "Congratulations! You ",
//                            color = Color.White,
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//
//                        Text(
//                            text = "Passed",
//                            color = Color(0xFF95DD31),
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//
//                        Text(
//                            text = " 🎉",
//                            color = Color.White,
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(5.dp))
//
//                    Text(
//                        text = "Well done! You have successfully",
//                        color = Color.White.copy(alpha = 0.9f),
//                        fontSize = 13.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//
//                    Text(
//                        text = "cleared the test.",
//                        color = Color.White.copy(alpha = 0.9f),
//                        fontSize = 13.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//                ResultStatsCard(
//                    correct = correct,
//                    incorrect = incorrect,
//                    score = score,
//                    rank = rank,
//                    modifier = Modifier.align(Alignment.BottomCenter)
//                )
//            }
//        }
//    }
//}



@Composable
fun ESOPResultScreen(
    navController: NavController,
    appPreferences: AppPreferences,
    onViewCertificateClick: () -> Unit = {},
    onBackToHomeClick: () -> Unit = {}
) {

    val totalQuestions by appPreferences.totalQuestions.collectAsState(initial = 0)
    val wrongAns by appPreferences.wrongAns.collectAsState(initial = 0)
    val percentage by appPreferences.percentage.collectAsState(initial = 0)
    val correctAns by appPreferences.correctAns.collectAsState(initial = 0)
    val result by appPreferences.result.collectAsState(initial = 0)

    val score = "$correctAns / $totalQuestions"

    val resultText = if (result == 0) "Failed" else "Passed"

    val resultColor =
        if (result == 0) Color.Red
        else Color(0xFF95DD31)

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 26.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    onClick = {
                        navController.navigate("ESOPCertificateScreen")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF075CE8)
                    )
                ) {
                    Text(
                        text = "View Certificate",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Back to Home",
                    color = Color(0xFF2563EB),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onBackToHomeClick()
                    }
                )
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF062C63),
                                Color(0xFF031A3F)
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {

                Text(
                    text = "Your Result",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.TopCenter)
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    ResultProgress(
                        percentage = percentage,
                        score = score,
                        modifier = Modifier.size(150.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = if (result == 0)
                                "Sorry! You "
                            else
                                "Congratulations! You ",
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

                        Text(
                            text = if (result == 0) " 😔" else " 🎉",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = if (result == 0)
                            "Please try again to improve your score."
                        else
                            "Well done! You have successfully",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    if (result != 0) {
                        Text(
                            text = "cleared the test.",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                ResultStatsCard(
                    correct = correctAns.toString(),
                    incorrect = wrongAns.toString(),
                    score = score,
                    rank = if (result == 0) "Fail" else "Pass",
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
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
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )

            drawArc(
                color = Color(0xFF95DD31),
                startAngle = startAngle,
                sweepAngle = sweepAngle * (percentage / 100f),
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
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
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7F7FB)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ResultStatItem(
                title = "Correct",
                value = correct,
                valueColor = Color(0xFF19A64A)
            )

            ResultStatItem(
                title = "Incorrect",
                value = incorrect,
                valueColor = Color(0xFFE53935)
            )

            ResultStatItem(
                title = "Score",
                value = score,
                valueColor = Color(0xFF1D9BF0)
            )

            ResultStatItem(
                title = "Rank",
                value = rank,
                valueColor = Color(0xFF111827)
            )
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

//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.StrokeCap
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//
//
//@Composable
//fun ESOPResultScreen(
//    navController: NavController,
//    percentage: Int = 76,
//    correct: String = "40",
//    incorrect: String = "10",
//    score: String = "38 / 50",
//    rank: String = "---",
//    onViewCertificateClick: () -> Unit = {},
//    onBackToHomeClick: () -> Unit = {}
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(360.dp)
//                .background(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(
//                            Color(0xFF062C63),
//                            Color(0xFF031A3F)
//                        )
//                    )
//                )
//                .padding(horizontal = 20.dp, vertical = 20.dp)
//        ) {
//            Text(
//                text = "Your Result",
//                color = Color.White,
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.align(Alignment.TopCenter)
//            )
//
//            Column(
//                modifier = Modifier
//                    .align(Alignment.Center)
//                    .padding(top = 12.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                ResultProgress(
//                    percentage = percentage,
//                    modifier = Modifier.size(150.dp)
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text(
//                        text = "Congratulations! You ",
//                        color = Color.White,
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Text(
//                        text = "Passed",
//                        color = Color(0xFF95DD31),
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Text(
//                        text = " 🎉",
//                        color = Color.White,
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text(
//                    text = "Well done! You have successfully",
//                    color = Color.White.copy(alpha = 0.9f),
//                    fontSize = 13.sp,
//                    fontWeight = FontWeight.Medium
//                )
//                Text(
//                    text = "cleared the test.",
//                    color = Color.White.copy(alpha = 0.9f),
//                    fontSize = 13.sp,
//                    fontWeight = FontWeight.Medium
//                )
//            }
//
//            ResultStatsCard(
//                correct = correct,
//                incorrect = incorrect,
//                score = score,
//                rank = rank,
//                modifier = Modifier.align(Alignment.BottomCenter)
//            )
//        }
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 26.dp, vertical = 16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Button(
//                onClick = onViewCertificateClick,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp),
//                shape = RoundedCornerShape(6.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF075CE8)
//                )
//            ) {
//                Text(
//                    text = "View Certificate",
//                    color = Color.White,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//
//            Spacer(modifier = Modifier.height(18.dp))
//
//            Text(
//                text = "Back to Home",
//                color = Color(0xFF2563EB),
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.clickable {
//                    onBackToHomeClick()
//                }
//            )
//        }
//    }
//}
//
//@Composable
//private fun ResultProgress(
//    percentage: Int,
//    modifier: Modifier = Modifier
//) {
//    Box(
//        modifier = modifier,
//        contentAlignment = Alignment.Center
//    ) {
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            val strokeWidth = 14.dp.toPx()
//            val startAngle = 140f
//            val sweepAngle = 260f
//
//            drawArc(
//                color = Color(0xFF1BB2AA),
//                startAngle = startAngle,
//                sweepAngle = sweepAngle,
//                useCenter = false,
//                style = Stroke(
//                    width = strokeWidth,
//                    cap = StrokeCap.Round
//                )
//            )
//
//            drawArc(
//                color = Color(0xFF95DD31),
//                startAngle = startAngle,
//                sweepAngle = sweepAngle * (percentage / 100f),
//                useCenter = false,
//                style = Stroke(
//                    width = strokeWidth,
//                    cap = StrokeCap.Round
//                )
//            )
//        }
//
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Text(
//                text = "$percentage%",
//                color = Color.White,
//                fontSize = 42.sp,
//                fontWeight = FontWeight.Bold
//            )
//
//            Text(
//                text = "38 / 50",
//                color = Color.White,
//                fontSize = 17.sp,
//                fontWeight = FontWeight.Bold
//            )
//        }
//    }
//}
//
//@Composable
//private fun ResultStatsCard(
//    correct: String,
//    incorrect: String,
//    score: String,
//    rank: String,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(10.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color(0xFFF7F7FB)
//        ),
//        elevation = CardDefaults.cardElevation(0.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 14.dp),
//            horizontalArrangement = Arrangement.SpaceAround,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            ResultStatItem(
//                title = "Correct",
//                value = correct,
//                valueColor = Color(0xFF19A64A)
//            )
//
//            ResultStatItem(
//                title = "Incorrect",
//                value = incorrect,
//                valueColor = Color(0xFFE53935)
//            )
//
//            ResultStatItem(
//                title = "Score",
//                value = score,
//                valueColor = Color(0xFF1D9BF0)
//            )
//
//            ResultStatItem(
//                title = "Rank",
//                value = rank,
//                valueColor = Color(0xFF111827)
//            )
//        }
//    }
//}
//
//@Composable
//private fun ResultStatItem(
//    title: String,
//    value: String,
//    valueColor: Color
//) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(
//            text = title,
//            color = Color(0xFF4B5563),
//            fontSize = 12.sp,
//            fontWeight = FontWeight.SemiBold
//        )
//
//        Spacer(modifier = Modifier.height(4.dp))
//
//        Text(
//            text = value,
//            color = valueColor,
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Bold
//        )
//    }
//}