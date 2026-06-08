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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.esop.quetions_esop.QuestionViewModel
import com.example.esop.util.Base64Utils
import com.example.esop.vibrate.FaceVerificationUtils
import com.example.esop.vibrate.VibrateWhileDialogVisible
import kotlinx.coroutines.delay
import java.util.concurrent.Executors

@Composable
fun TestScreen(
    navController: NavController,
    viewModel: CompletePofileScreenViewModel = viewModel(),
    updateModel: UpdateProfileViewModel = viewModel(),
    questionViewModel: QuestionViewModel = viewModel()
) {
    val state = viewModel.state
    val UpdateUI = updateModel.Updatestate
    val questionState by questionViewModel.questionState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    lateinit var appPrefs: AppPreferences
    val previewView = remember {
        PreviewView(context)
    }
//    val questionViewModel: EsopQuestionModel = viewModel()
    var showLoading by remember { mutableStateOf(false) }
    val cameraExecutor = remember {
        Executors.newSingleThreadExecutor()
    }

    var imageCapture by remember {
        mutableStateOf<ImageCapture?>(null)
    }

    var firstEmbedding by remember {
        mutableStateOf<FloatArray?>(null)
    }

    var resultText by remember { mutableStateOf("No Face Captured") }
    var questionCode by remember { mutableStateOf(2) }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var blurScreen by remember {
        mutableStateOf(false)
    }

    var showCameraPreview by remember {
        mutableStateOf(true)
    }


    val versionName = remember {
        context.packageManager
            .getPackageInfo(context.packageName, 0)
            .versionName
    }
    var loginId by remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }


    val profileViewModel: ProfileViewModel = viewModel()
    appPrefs = AppPreferences(context)
    val userEmail by appPrefs.userEmail.collectAsState(initial = null)
    val userMobile by appPrefs.mobile.collectAsState(initial = null)
    val userloginId by appPrefs.loginId.collectAsState(initial = null)
    val userusertype by appPrefs.usertype.collectAsState(initial = null)
    loginId=userloginId.toString()
    val currentLoginId = loginId
    val currentEmail = userEmail?.toString().orEmpty()
    val currentVersion = versionName?.toString().orEmpty()
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



    LaunchedEffect(questionState) {

        when (val state = questionState) {

            is Resource.Success -> {

                val response = state.data

                Log.d(
                    "QUESTION_SUCCESS",
                    response.toString()
                )
                Toast.makeText(
                    context,
                    " exam quetions",
                    Toast.LENGTH_LONG
                ).show()
                Toast.makeText(
                    context,
                    "Questions Loaded Successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }

            is Resource.Error -> {

                Toast.makeText(
                    context,
                    state.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is Resource.Loading -> {

                Log.d(
                    "QUESTION_LOADING",
                    "Loading..."
                )
            }

            else -> {}
        }
    }

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
                            "State" to item.state,
                            "District" to item.district
                        )

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
//                        questionViewModel.fetchQuestions(questionCode.toString())



                            {
                                val hasCameraPermission =
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED

                                if (hasCameraPermission) {
                                    FaceVerificationUtils.startCamera(
                                        context = context,
                                        lifecycleOwner = lifecycleOwner,
                                        previewView = previewView
                                    ) { capture ->

                                        imageCapture = capture

                                        previewView.postDelayed({
                                            FaceVerificationUtils.captureImage(
                                                imageCapture = imageCapture,
                                                cameraExecutor = cameraExecutor,
                                                context = context
                                            ) { bitmap ->

                                                firstEmbedding =
                                                    FaceVerificationUtils.createEmbedding(bitmap)

                                                resultText = "First Face Saved"
                                                showCameraPreview = false

                                                Toast.makeText(
                                                    context,
                                                    "Face Captured Successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }, 1500)
                                    }
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
                Log.e(
                    "PROFILE",
                    state.message
                )
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




    LaunchedEffect(firstEmbedding) {
        while (firstEmbedding != null) {
            delay(2000)

            FaceVerificationUtils.captureImage(
                imageCapture = imageCapture,
                cameraExecutor = cameraExecutor,
                context = context
            ) { bitmap ->

                val currentEmbedding =
                    FaceVerificationUtils.createEmbedding(bitmap)

                val savedEmbedding = firstEmbedding

                if (savedEmbedding != null) {
                    val distance =
                        FaceVerificationUtils.compareEmbeddings(
                            savedEmbedding,
                            currentEmbedding
                        )

                    if (distance < 2000) {
                        resultText = "Genuine Person"
                        blurScreen = false
                        showDialog = false
                    } else {
                        resultText = "Face Not Matched"
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

            Button(
                onClick =

                    {


                        profileViewModel.getProfile(
                            appVersion = currentVersion,
                            loginId = currentLoginId,
                            email = currentEmail
                        )
                    },

//                    {
//                    val hasCameraPermission =
//                        ContextCompat.checkSelfPermission(
//                            context,
//                            Manifest.permission.CAMERA
//                        ) == PackageManager.PERMISSION_GRANTED
//
//                    if (hasCameraPermission) {
//                        FaceVerificationUtils.startCamera(
//                            context = context,
//                            lifecycleOwner = lifecycleOwner,
//                            previewView = previewView
//                        ) { capture ->
//
//                            imageCapture = capture
//
//                            previewView.postDelayed({
//                                FaceVerificationUtils.captureImage(
//                                    imageCapture = imageCapture,
//                                    cameraExecutor = cameraExecutor,
//                                    context = context
//                                ) { bitmap ->
//
//                                    firstEmbedding =
//                                        FaceVerificationUtils.createEmbedding(bitmap)
//
//                                    resultText = "First Face Saved"
//                                    showCameraPreview = false
//
//                                    Toast.makeText(
//                                        context,
//                                        "Face Captured Successfully",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//                            }, 1500)
//                        }
//                    } else {
//                        permissionLauncher.launch(
//                            Manifest.permission.CAMERA
//                        )
//                    }
//                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text("Capture First Face")
            }

            Text(
                text = resultText,
                modifier = Modifier.padding(16.dp)
            )
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
        }
    }
}






























































//import android.Manifest
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.ImageCaptureException
//import androidx.camera.core.ImageProxy
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.view.PreviewView
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.ContextCompat
//import kotlinx.coroutines.delay
//import java.nio.ByteBuffer
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//import kotlin.math.sqrt

//@Composable
//fun TestScreen() {
//
//    val context = LocalContext.current
//
//    val previewView = remember {
//        PreviewView(context)
//    }
//
//    val cameraExecutor = remember {
//        Executors.newSingleThreadExecutor()
//    }
//
//    var imageCapture by remember {
//        mutableStateOf<ImageCapture?>(null)
//    }
//
//    var firstEmbedding by remember {
//        mutableStateOf<FloatArray?>(null)
//    }
//
//    var resultText by remember {
//        mutableStateOf("No Face Captured")
//    }
//
//    var showDialog by remember {
//        mutableStateOf(false)
//    }
//
//    var blurScreen by remember {
//        mutableStateOf(false)
//    }
//
//    // CAMERA PREVIEW SHOW/HIDE
//    var showCameraPreview by remember {
//        mutableStateOf(true)
//    }
//
//    // CAMERA PERMISSION
//    val permissionLauncher =
//        rememberLauncherForActivityResult(
//            contract =
//                ActivityResultContracts.RequestPermission()
//        ) { granted ->
//
//            if (!granted) {
//
//                Toast.makeText(
//                    context,
//                    "Camera Permission Denied",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//    // AUTO VERIFY
//    LaunchedEffect(firstEmbedding) {
//
//        while (firstEmbedding != null) {
//
//            delay(2000)
//
//            captureImage(
//                imageCapture = imageCapture,
//                cameraExecutor = cameraExecutor,
//                context = context
//            ) { bitmap ->
//
//                val currentEmbedding =
//                    createEmbedding(bitmap)
//
//                val savedEmbedding =
//                    firstEmbedding
//
//                if (savedEmbedding != null) {
//
//                    val distance =
//                        compareEmbeddings(
//                            savedEmbedding,
//                            currentEmbedding
//                        )
//
//                    if (distance < 2000) {
//
//                        resultText =
//                            "✅ Genuine Person"
//
//                        blurScreen = false
//
//                        showDialog = false
//
//                    } else {
//
//                        resultText =
//                            "❌ Face Not Matched"
//
//                        blurScreen = true
//
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
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .then(
//                    if (blurScreen) {
//
//                        Modifier.graphicsLayer {
//                            alpha = 0.3f
//                        }
//
//                    } else {
//
//                        Modifier
//                    }
//                ),
//            horizontalAlignment =
//                Alignment.CenterHorizontally
//        ) {
//
//            // CAMERA PREVIEW
//            if (showCameraPreview) {
//
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
//            Spacer(
//                modifier = Modifier.height(10.dp)
//            )
//
//            // CAPTURE BUTTON
//            Button(
//                onClick = {
//
//                    when {
//
//                        ContextCompat.checkSelfPermission(
//                            context,
//                            Manifest.permission.CAMERA
//                        ) == PackageManager.PERMISSION_GRANTED -> {
//
//                            startCamera(
//                                context = context,
//                                previewView = previewView
//                            ) { capture ->
//
//                                imageCapture = capture
//
//                                previewView.postDelayed({
//
//                                    captureImage(
//                                        imageCapture = imageCapture,
//                                        cameraExecutor = cameraExecutor,
//                                        context = context
//                                    ) { bitmap ->
//
//                                        val embedding =
//                                            createEmbedding(bitmap)
//
//                                        firstEmbedding =
//                                            embedding
//
//                                        resultText =
//                                            "✅ First Face Saved"
//
//                                        // HIDE CAMERA
//                                        showCameraPreview = false
//
//                                        Toast.makeText(
//                                            context,
//                                            "Face Captured Successfully",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//
//                                }, 1500)
//                            }
//                        }
//
//                        else -> {
//
//                            permissionLauncher.launch(
//                                Manifest.permission.CAMERA
//                            )
//                        }
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(10.dp)
//            ) {
//
//                Text(
//                    text = "Capture First Face"
//                )
//            }
//
//            Text(
//                text = resultText,
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//
//        // ALERT DIALOG
//        if (showDialog) {
//
//            AlertDialog(
//
//                onDismissRequest = {},
//
//                title = {
//
//                    Text(
//                        text = "Security Alert"
//                    )
//                },
//
//                text = {
//
//                    Text(
//                        text =
//                            "Face Not Matched or Object Detected"
//                    )
//                },
//
//                confirmButton = {
//
//                    Button(
//                        onClick = {
//
//                            showDialog = false
//
//                            blurScreen = false
//                        }
//                    ) {
//
//                        Text("OK")
//                    }
//                }
//            )
//        }
//    }
//}
//
//// START CAMERA
//private fun startCamera(
//    context: android.content.Context,
//    previewView: PreviewView,
//    onReady: (ImageCapture) -> Unit
//) {
//
//    val cameraProviderFuture =
//        ProcessCameraProvider.getInstance(context)
//
//    cameraProviderFuture.addListener({
//
//        val cameraProvider =
//            cameraProviderFuture.get()
//
//        val preview =
//            Preview.Builder().build()
//
//        preview.setSurfaceProvider(
//            previewView.surfaceProvider
//        )
//
//        val imageCapture =
//            ImageCapture.Builder()
//                .setCaptureMode(
//                    ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
//                )
//                .build()
//
//        val cameraSelector =
//            CameraSelector.DEFAULT_FRONT_CAMERA
//
//        try {
//
//            cameraProvider.unbindAll()
//
//            cameraProvider.bindToLifecycle(
//                context as androidx.lifecycle.LifecycleOwner,
//                cameraSelector,
//                preview,
//                imageCapture
//            )
//
//            onReady(imageCapture)
//
//        } catch (e: Exception) {
//
//            e.printStackTrace()
//
//            Toast.makeText(
//                context,
//                "Camera Start Failed",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//
//    }, ContextCompat.getMainExecutor(context))
//}
//
//// CAPTURE IMAGE
//private fun captureImage(
//    imageCapture: ImageCapture?,
//    cameraExecutor: ExecutorService,
//    context: android.content.Context,
//    onBitmapReady: (Bitmap) -> Unit
//) {
//
//    val capture = imageCapture
//
//    if (capture == null) {
//
//        Toast.makeText(
//            context,
//            "Camera Not Ready",
//            Toast.LENGTH_SHORT
//        ).show()
//
//        return
//    }
//
//    capture.takePicture(
//
//        cameraExecutor,
//
//        object : ImageCapture.OnImageCapturedCallback() {
//
//            override fun onCaptureSuccess(
//                image: ImageProxy
//            ) {
//
//                try {
//
//                    val bitmap =
//                        imageProxyToBitmap(image)
//
//                    onBitmapReady(bitmap)
//
//                } catch (e: Exception) {
//
//                    e.printStackTrace()
//
//                } finally {
//
//                    image.close()
//                }
//            }
//
//            override fun onError(
//                exception: ImageCaptureException
//            ) {
//
//                exception.printStackTrace()
//
//                Toast.makeText(
//                    context,
//                    "Capture Failed",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    )
//}
//
//// IMAGE TO BITMAP
//private fun imageProxyToBitmap(
//    image: ImageProxy
//): Bitmap {
//
//    val buffer: ByteBuffer =
//        image.planes[0].buffer
//
//    val bytes =
//        ByteArray(buffer.remaining())
//
//    buffer.get(bytes)
//
//    return BitmapFactory.decodeByteArray(
//        bytes,
//        0,
//        bytes.size
//    )
//}
//
//// SIMPLE FACE EMBEDDING
//private fun createEmbedding(
//    bitmap: Bitmap
//): FloatArray {
//
//    val resized =
//        Bitmap.createScaledBitmap(
//            bitmap,
//            32,
//            32,
//            true
//        )
//
//    val embedding =
//        FloatArray(32 * 32)
//
//    var index = 0
//
//    for (x in 0 until 32) {
//
//        for (y in 0 until 32) {
//
//            val pixel =
//                resized.getPixel(x, y)
//
//            val r =
//                (pixel shr 16) and 0xff
//
//            val g =
//                (pixel shr 8) and 0xff
//
//            val b =
//                pixel and 0xff
//
//            val gray =
//                (r + g + b) / 3f
//
//            embedding[index] = gray
//
//            index++
//        }
//    }
//
//    return embedding
//}
//
//// COMPARE EMBEDDING
//private fun compareEmbeddings(
//    emb1: FloatArray,
//    emb2: FloatArray
//): Float {
//
//    var sum = 0f
//
//    for (i in emb1.indices) {
//
//        val diff =
//            emb1[i] - emb2[i]
//
//        sum += diff * diff
//    }
//
//    return sqrt(sum)
//}


















//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//
//@Composable
//fun TestScreen() {
//
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//
//        Text(text = "Test Screen")
//    }
//}