package com.example.esop.CertificateScreen


import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.util.Log
import android.view.View
import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.esop.network.AppPreferences
import com.example.esop.network.Resource
import com.example.esop.profile.ProfileViewModel
import java.io.File
import java.io.FileOutputStream
import kotlin.math.cos
import kotlin.math.sin



import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore


import android.graphics.Canvas
import android.os.Build
import androidx.annotation.RequiresApi





import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider

import android.graphics.Paint
import android.graphics.RectF
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ESOPCertificateScreen(
    navController: NavController,
    appPreferences: AppPreferences,
    profileViewModel: ProfileViewModel = viewModel(),
)

{
    val context = LocalContext.current

    val versionName = remember {
        context.packageManager
            .getPackageInfo(context.packageName, 0)
            .versionName
    }
    var showLoading by remember { mutableStateOf(false) }
    val profileState by profileViewModel.profileState.collectAsState()
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }


    val totalQuestions by appPreferences.totalQuestions.collectAsState(initial = 0)
    val correctAns by appPreferences.correctAns.collectAsState(initial = 0)
    val resultValue by appPreferences.result.collectAsState(initial = 0)


    val userEmail by appPreferences.userEmail.collectAsState(initial = null)
    val userMobile by appPreferences.mobile.collectAsState(initial = null)
    val userloginId by appPreferences.loginId.collectAsState(initial = null)
    val userusertype by appPreferences.usertype.collectAsState(initial = null)





    val currentLoginId = userloginId.orEmpty()
    val currentEmail = userEmail.orEmpty()
    val currentVersion = versionName

    val view = LocalView.current
    LaunchedEffect(
        currentLoginId,
        currentEmail
    ) {

        if (
            currentLoginId.isNotBlank() &&
            currentEmail.isNotBlank()
        ) {

            Log.d(
                "PROFILE_API",
                "Calling API : $currentLoginId"
            )

            profileViewModel.getProfile(
                loginId = currentLoginId,
                email = currentEmail,
                appVersion = currentVersion.toString()
            )
        }
    }
    LaunchedEffect(profileState) {

        when (val state = profileState) {

            is Resource.Loading -> {

                showLoading = true

                Log.d(
                    "PROFILE",
                    "Loading..."
                )
            }

            is Resource.Success -> {

                showLoading = false

                val response = state.data

                Log.d(
                    "PROFILE_RESPONSE",
                    response.toString()
                )

                response.wrappedList.firstOrNull()?.let { item ->

                    firstName = item.firstname.orEmpty()
                    lastName = item.lastname.orEmpty()

                    Log.d(
                        "PROFILE_NAME",
                        "$firstName $lastName"
                    )
                }
            }
            is Resource.Error -> {

                showLoading = false

                Log.e(
                    "PROFILE",
                    state.message ?: "Unknown Error"
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

    fun sharePdf(
        context: Context,
        pdfFiles: File
    ) {

        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            pdfFiles
        )

        val intent = Intent(Intent.ACTION_SEND)

        intent.type = "application/pdf"

        intent.putExtra(
            Intent.EXTRA_STREAM,
            uri
        )

        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        context.startActivity(
            Intent.createChooser(
                intent,
                "Share Certificate"
            )
        )
    }




    @RequiresApi(Build.VERSION_CODES.Q)



    fun saveCertificatePdf(
        context: Context,
        candidateName: String,
        score: String,
        result: String,
        date: String
    ) {

        val pdfDocument = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(
            595,
            842,
            1
        ).create()

        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        val titlePaint = Paint().apply {
            textSize = 24f
            isFakeBoldText = true
        }

        val textPaint = Paint().apply {
            textSize = 18f
        }

        canvas.drawText(
            "CERTIFICATE",
            180f,
            80f,
            titlePaint
        )

        canvas.drawText(
            "Candidate Name: $candidateName",
            50f,
            180f,
            textPaint
        )

        canvas.drawText(
            "Score: $score",
            50f,
            240f,
            textPaint
        )

        canvas.drawText(
            "Result: $result",
            50f,
            300f,
            textPaint
        )

        canvas.drawText(
            "Date: $date",
            50f,
            360f,
            textPaint
        )

        pdfDocument.finishPage(page)

        val fileName = "Certificate_${System.currentTimeMillis()}.pdf"

        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            put(
                MediaStore.Downloads.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS
            )
        }

        val uri = context.contentResolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            values
        )

        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { output ->
                pdfDocument.writeTo(output)
            }

            Toast.makeText(
                context,
                "Certificate saved in Downloads",
                Toast.LENGTH_LONG
            ).show()
        }

        pdfDocument.close()
    }
//    @RequiresApi(Build.VERSION_CODES.Q)
//    fun savePdfToDownloads(
//        context: Context,
//        bitmap: Bitmap
//    ) {
//
//        val pdfDocument = PdfDocument()
//
//        val pageInfo = PdfDocument.PageInfo.Builder(
//            595,
//            842,
//            1
//        ).create()
//
//        val page = pdfDocument.startPage(pageInfo)
//        val leftMargin = 30f
//        val topMargin = 20f
//        val rightMargin = 30f
//        val bottomMargin = 30f
//        page.canvas.drawBitmap(
//            bitmap,
//            null,
//            android.graphics.RectF(
//                leftMargin,
//                topMargin,
//                595f - rightMargin,
//                842f - bottomMargin
//            ),
//            null
//        )
//
//        pdfDocument.finishPage(page)
//
//        val fileName = "Certificate_${System.currentTimeMillis()}.pdf"
//
//        val values = ContentValues().apply {
//            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
//            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
//            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
//        }
//
//        val uri = context.contentResolver.insert(
//            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
//            values
//        )
//
//        uri?.let {
//
//            context.contentResolver.openOutputStream(it)?.use { outputStream ->
//                pdfDocument.writeTo(outputStream)
//            }
//
//            Toast.makeText(
//                context,
//                "PDF saved in Download folder",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//
//        pdfDocument.close()
//    }






    @RequiresApi(Build.VERSION_CODES.Q)
    fun savePdfToDownloads(
        context: Context,
        bitmap: Bitmap
    ) {

        val pdfDocument = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(
            595,
            842,
            1
        ).create()

        val page = pdfDocument.startPage(pageInfo)

        page.canvas.drawBitmap(
            bitmap,
            null,
            RectF(
                30f,   // left
                20f,   // top
                565f,  // right
                812f   // bottom
            ),
            null
        )

        pdfDocument.finishPage(page)

        val fileName = "Certificate_${System.currentTimeMillis()}.pdf"

        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            put(
                MediaStore.Downloads.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS
            )
        }

        val uri = context.contentResolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            values
        )

        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { output ->
                pdfDocument.writeTo(output)
            }

            Toast.makeText(
                context,
                "PDF Saved In Downloads Folder",
                Toast.LENGTH_LONG
            ).show()
        }

        pdfDocument.close()
    }
    fun createPdf(
        context: Context,
        bitmap: Bitmap
    ) {

        val pdfDocument = PdfDocument()

        val pageWidth = 595
        val pageHeight = 842

        val pageInfo = PdfDocument.PageInfo.Builder(
            pageWidth,
            pageHeight,
            1
        ).create()

        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        val leftMargin = 30f
        val topMargin = 30f
        val bottomMargin = 30f

        val availableWidth = pageWidth - leftMargin
        val availableHeight = pageHeight - topMargin - bottomMargin

        val scale = minOf(
            availableWidth / bitmap.width.toFloat(),
            availableHeight / bitmap.height.toFloat()
        )

        val scaledWidth = bitmap.width * scale
        val scaledHeight = bitmap.height * scale

        canvas.drawBitmap(
            bitmap,
            null,
            RectF(
                leftMargin,
                topMargin,
                leftMargin + scaledWidth,
                topMargin + scaledHeight
            ),
            null
        )

        pdfDocument.finishPage(page)

        val file = File(
            context.getExternalFilesDir(null),
            "Certificate.pdf"
        )

        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()
    }


    fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return bitmap
    }







    val score = "$correctAns / $totalQuestions"

    // Result from DataStore
    val result = if (resultValue == 0) {
        "FAIL"
    } else {
        "PASS"
    }

    // Current Date
    val currentDate = remember {
        SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        ).format(Date())
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
                    onClick =
                        {
                            val originalBitmap = getBitmapFromView(view)

                            // Top aur Bottom ka extra area remove
                            val cropTop = 300
                            val cropBottom = 300

                            val croppedBitmap = Bitmap.createBitmap(
                                originalBitmap,
                                0,
                                cropTop,
                                originalBitmap.width,
                                originalBitmap.height - cropTop - cropBottom
                            )

                            savePdfToDownloads(
                                context = context,
                                bitmap = croppedBitmap
                            )

//                            val bitmap = getBitmapFromView(view)
//
//                            savePdfToDownloads(
//                                context = context,
//                                bitmap = bitmap
//                            )
                        },
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
                    onClick = {

                        val bitmap = getBitmapFromView(view)

                        val pdfFiles = createPdf(
                            context,
                            bitmap
                        )

//                        sharePdf(
//                            context,
//                            pdfFiles
//                        )

                    }
                ) {
                    Text("Share Certificate")
                }
//                OutlinedButton(
//                    onClick = onShareCertificateClick,
//                    modifier = Modifier
//                        .weight(1f)
//                        .height(46.dp),
//                    shape = RoundedCornerShape(6.dp),
//                    border = BorderStroke(
//                        1.dp,
//                        Color(0xFFB7C7E8)
//                    ),
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
            }
        }
    )

    { paddingValues ->

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

            Column {

                CertificateCard(
                    candidateName = (firstName ?: "") + (lastName ?: ""),
                    score = score,
                    result = result,
                    date = currentDate
                )
            }
        }
    }


//    { paddingValues ->
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .verticalScroll(rememberScrollState())
//                .padding(
//                    horizontal = 10.dp,
//                    vertical = 16.dp
//                ),
//            contentAlignment = Alignment.Center
//        )
//        {
//
//            CertificateCard(
//                candidateName = firstName+lastName ?: "",
//                score = score,
//                result = result,
//                date = currentDate
//            )
//        }
//    }

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
