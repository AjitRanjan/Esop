package faceembedding


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.esop.network.AppPreferences
import kotlin.math.cos
import kotlin.math.sin

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//
//fun ESOPCertificateScreen(
//    navController: NavController,
//    candidateName: String = "Ajit Ranjan",
//    score: String = "38 / 50",
//    result: String = "PASS",
//    date: String = "05 May 2026",
//    onDownloadPdfClick: () -> Unit = {},
//    onShareCertificateClick: () -> Unit = {}
//)
//{
//
//    var firstName by remember { mutableStateOf("") }
//    var lastName by remember { mutableStateOf("") }
//
//
//    Scaffold(
//        containerColor = Color(0xFFF4F7FB),
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Certificate",
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF111827)
//                    )
//                },
//                navigationIcon = {
//                    IconButton(
//                        onClick = {
//                            navController.popBackStack()
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack,
//                            contentDescription = "Back",
//                            tint = Color(0xFF111827)
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color.White
//                )
//            )
//        },
//        bottomBar = {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color.White)
//                    .padding(12.dp),
//                horizontalArrangement = Arrangement.spacedBy(14.dp)
//            ) {
//                Button(
//                    onClick = onDownloadPdfClick,
//                    modifier = Modifier
//                        .weight(1f)
//                        .height(46.dp),
//                    shape = RoundedCornerShape(6.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF075CE8)
//                    )
//                ) {
//                    Text(
//                        text = "Download PDF",
//                        color = Color.White,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//
//                OutlinedButton(
//                    onClick = onShareCertificateClick,
//                    modifier = Modifier
//                        .weight(1f)
//                        .height(46.dp),
//                    shape = RoundedCornerShape(6.dp),
//                    border = BorderStroke(1.dp, Color(0xFFB7C7E8)),
//                    colors = ButtonDefaults.outlinedButtonColors(
//                        contentColor = Color(0xFF075CE8)
//                    )
//                ) {
//                    Text(
//                        text = "Share Certificate",
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//        }
//    ) { paddingValues ->
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .verticalScroll(rememberScrollState())
//                .padding(horizontal = 10.dp, vertical = 16.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            CertificateCard(
//                candidateName = candidateName,
//                score = score,
//                result = result,
//                date = date
//            )
//        }
//    }
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ESOPCertificateScreen(
    navController: NavController,
    onDownloadPdfClick: () -> Unit = {},
    onShareCertificateClick: () -> Unit = {}
) {

    val context = LocalContext.current

    val appPreferences = remember {
        AppPreferences(context)
    }

    val userName by appPreferences.userName.collectAsState(initial = "")
    val totalQuestions by appPreferences.totalQuestions.collectAsState(initial = 0)
    val correctAns by appPreferences.correctAns.collectAsState(initial = 0)
    val resultValue by appPreferences.result.collectAsState(initial = 0)

    // Score from DataStore
    val score = "$correctAns / $totalQuestions"

    // Result from DataStore
    val result = if (resultValue == 0) {
        "FAIL"
    } else {
        "PASS"
    }

    // Current Date
    val currentDate = remember {
        java.text.SimpleDateFormat(
            "dd MMM yyyy",
            java.util.Locale.getDefault()
        ).format(java.util.Date())
    }

    Scaffold(
        containerColor = Color(0xFFF4F7FB),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Certificate",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF111827)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Button(
                    onClick = onDownloadPdfClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF075CE8)
                    )
                ) {
                    Text(
                        text = "Download PDF",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onShareCertificateClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(
                        1.dp,
                        Color(0xFFB7C7E8)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF075CE8)
                    )
                ) {
                    Text(
                        text = "Share Certificate",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 10.dp,
                    vertical = 16.dp
                ),
            contentAlignment = Alignment.Center
        ) {

            CertificateCard(
                candidateName = userName ?: "",
                score = score,
                result = result,
                date = currentDate
            )
        }
    }
}
@Composable
private fun CertificateCard(
    candidateName: String,
    score: String,
    result: String,
    date: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(315.dp)
                .border(
                    width = 1.dp,
                    color = Color(0xFFD9E1F2),
                    shape = RoundedCornerShape(14.dp)
                )
        ) {
            CertificateCorners()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "eSOP",
                    color = Color(0xFF075CE8),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Electronic Standard Operation Process",
                    color = Color(0xFF111827),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Certificate of Completion",
                    color = Color(0xFF153C8A),
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Cursive
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "This is to certify that",
                    color = Color(0xFF374151),
                    fontSize = 11.sp
                )

                Text(
                    text = candidateName,
                    color = Color(0xFF111827),
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "has successfully completed the eSOP Examination.",
                    color = Color(0xFF374151),
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .width(92.dp)
                        .height(54.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(Color(0xFF064EA6)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Score",
                            color = Color.White,
                            fontSize = 11.sp
                        )

                        Text(
                            text = score,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Result: ",
                        color = Color(0xFF111827),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = result,
                        color = Color(0xFF19A64A),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Date",
                            color = Color(0xFF111827),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = date,
                            color = Color(0xFF111827),
                            fontSize = 9.sp
                        )
                    }

                    MedalIcon()

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Signature",
                            color = Color(0xFF111827),
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Cursive
                        )

                        Box(
                            modifier = Modifier
                                .width(70.dp)
                                .height(1.dp)
                                .background(Color(0xFF111827))
                        )

                        Text(
                            text = "Authorized Signatory",
                            color = Color(0xFF111827),
                            fontSize = 7.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CertificateCorners() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val blue = Color(0xFF064EA6)
        val gold = Color(0xFFD6A12D)

        drawPath(
            path = Path().apply {
                moveTo(0f, 0f)
                lineTo(95f, 0f)
                lineTo(0f, 95f)
                close()
            },
            color = blue
        )

        drawPath(
            path = Path().apply {
                moveTo(85f, 0f)
                lineTo(105f, 0f)
                lineTo(0f, 105f)
                lineTo(0f, 85f)
                close()
            },
            color = gold
        )

        drawPath(
            path = Path().apply {
                moveTo(size.width, 0f)
                lineTo(size.width - 95f, 0f)
                lineTo(size.width, 95f)
                close()
            },
            color = blue
        )

        drawPath(
            path = Path().apply {
                moveTo(size.width - 85f, 0f)
                lineTo(size.width - 105f, 0f)
                lineTo(size.width, 105f)
                lineTo(size.width, 85f)
                close()
            },
            color = gold
        )

        drawPath(
            path = Path().apply {
                moveTo(0f, size.height)
                lineTo(95f, size.height)
                lineTo(0f, size.height - 95f)
                close()
            },
            color = blue
        )

        drawPath(
            path = Path().apply {
                moveTo(size.width, size.height)
                lineTo(size.width - 95f, size.height)
                lineTo(size.width, size.height - 95f)
                close()
            },
            color = blue
        )

        drawPath(
            path = Path().apply {
                moveTo(size.width - 85f, size.height)
                lineTo(size.width - 105f, size.height)
                lineTo(size.width, size.height - 105f)
                lineTo(size.width, size.height - 85f)
                close()
            },
            color = gold
        )
    }
}

@Composable
private fun MedalIcon() {
    Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val blue = Color(0xFF064EA6)

            drawPath(
                path = Path().apply {
                    moveTo(size.width * 0.35f, size.height * 0.55f)
                    lineTo(size.width * 0.25f, size.height)
                    lineTo(size.width * 0.45f, size.height * 0.82f)
                    lineTo(size.width * 0.5f, size.height * 0.6f)
                    close()
                },
                color = blue
            )

            drawPath(
                path = Path().apply {
                    moveTo(size.width * 0.65f, size.height * 0.55f)
                    lineTo(size.width * 0.75f, size.height)
                    lineTo(size.width * 0.55f, size.height * 0.82f)
                    lineTo(size.width * 0.5f, size.height * 0.6f)
                    close()
                },
                color = blue
            )

            drawCircle(
                color = Color(0xFFD6A12D),
                radius = size.minDimension * 0.32f,
                center = center
            )

            drawCircle(
                color = Color(0xFF111827),
                radius = size.minDimension * 0.25f,
                center = center,
                style = Stroke(width = 2f)
            )

            val starPath = Path()
            val cx = center.x
            val cy = center.y
            val outer = size.minDimension * 0.15f
            val inner = outer * 0.45f

            for (i in 0 until 10) {
                val angle = Math.toRadians((i * 36 - 90).toDouble())
                val radius = if (i % 2 == 0) outer else inner
                val x = cx + cos(angle).toFloat() * radius
                val y = cy + sin(angle).toFloat() * radius

                if (i == 0) {
                    starPath.moveTo(x, y)
                } else {
                    starPath.lineTo(x, y)
                }
            }

            starPath.close()
            drawPath(starPath, color = Color(0xFF111827))
        }
    }
}