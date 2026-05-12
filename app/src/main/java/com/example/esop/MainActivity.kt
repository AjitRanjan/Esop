package com.example.esop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.esop.login.LoginScreen
import com.example.esop.network.AppPreferences
import faceembedding.TestScreen
import signup.SignupScreen




//import android.Manifest
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.widget.Toast
//import androidx.activity.compose.setContent
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
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
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
//
//class MainActivity : ComponentActivity() {
//
//    // CAMERA CAPTURE
//    private var imageCapture: ImageCapture? = null
//
//    private lateinit var cameraExecutor: ExecutorService
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        cameraExecutor = Executors.newSingleThreadExecutor()
//
//        setContent {
//
//            MaterialTheme {
//
//                FaceVerificationScreen()
//            }
//        }
//    }
//
//    // CAMERA PERMISSION
//    private val permissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { granted ->
//
//            if (!granted) {
//
//                Toast.makeText(
//                    this,
//                    "Camera Permission Denied",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//    @Composable
//    fun FaceVerificationScreen() {
//
//        val context = LocalContext.current
//
//        val previewView = remember {
//            PreviewView(context)
//        }
//
//        var firstEmbedding by remember {
//            mutableStateOf<FloatArray?>(null)
//        }
//
//        var resultText by remember {
//            mutableStateOf("No Face Captured")
//        }
//
//        var showDialog by remember {
//            mutableStateOf(false)
//        }
//
//        var blurScreen by remember {
//            mutableStateOf(false)
//        }
//
//        // CAMERA PREVIEW SHOW/HIDE
//        var showCameraPreview by remember {
//            mutableStateOf(true)
//        }
//
//        // AUTO VERIFY
//        LaunchedEffect(firstEmbedding) {
//
//            while (firstEmbedding != null) {
//
//                delay(2000)
//
//                captureImage { bitmap ->
//
//                    val currentEmbedding =
//                        createEmbedding(bitmap)
//
//                    val savedEmbedding =
//                        firstEmbedding
//
//                    if (savedEmbedding != null) {
//
//                        val distance =
//                            compareEmbeddings(
//                                savedEmbedding,
//                                currentEmbedding
//                            )
//
//                        runOnUiThread {
//
//                            if (distance < 2000) {
//
//                                resultText =
//                                    "✅ Genuine Person"
//
//                                blurScreen = false
//
//                                showDialog = false
//
//                            } else {
//
//                                resultText =
//                                    "❌ Face Not Matched"
//
//                                blurScreen = true
//
//                                showDialog = true
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        Box(
//            modifier = Modifier.fillMaxSize()
//        ) {
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .then(
//                        if (blurScreen) {
//
//                            Modifier.graphicsLayer {
//                                alpha = 0.3f
//                            }
//
//                        } else {
//
//                            Modifier
//                        }
//                    )
//            ) {
//
//                // CAMERA PREVIEW
//                if (showCameraPreview) {
//
//                    AndroidView(
//                        factory = {
//                            previewView
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .weight(1f)
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(10.dp))
//
//                // CAPTURE BUTTON
//                Button(
//                    onClick = {
//
//                        checkPermissionAndStartCamera(
//                            previewView
//                        ) {
//
//                            captureImage { bitmap ->
//
//                                val embedding =
//                                    createEmbedding(bitmap)
//
//                                firstEmbedding = embedding
//
//                                resultText =
//                                    "✅ First Face Saved"
//
//                                // HIDE CAMERA PREVIEW
//                                showCameraPreview = false
//
//                                Toast.makeText(
//                                    context,
//                                    "Face Captured Successfully",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(10.dp)
//                ) {
//
//                    Text("Capture First Face")
//                }
//
//                // RESULT
//                Text(
//                    text = resultText,
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//
//            // ALERT DIALOG
//            if (showDialog) {
//
//                AlertDialog(
//
//                    onDismissRequest = {},
//
//                    title = {
//
//                        Text(
//                            text = "Security Alert"
//                        )
//                    },
//
//                    text = {
//
//                        Text(
//                            text =
//                                "Face Not Matched or Object Detected"
//                        )
//                    },
//
//                    confirmButton = {
//
//                        Button(
//                            onClick = {
//
//                                showDialog = false
//
//                                blurScreen = false
//                            }
//                        ) {
//
//                            Text("OK")
//                        }
//                    }
//                )
//            }
//        }
//    }
//
//    // CHECK CAMERA PERMISSION
//    private fun checkPermissionAndStartCamera(
//        previewView: PreviewView,
//        onGranted: () -> Unit
//    ) {
//
//        when {
//
//            ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_GRANTED -> {
//
//                startCamera(previewView)
//
//                // WAIT CAMERA INIT
//                previewView.postDelayed({
//
//                    onGranted()
//
//                }, 1500)
//            }
//
//            else -> {
//
//                permissionLauncher.launch(
//                    Manifest.permission.CAMERA
//                )
//            }
//        }
//    }
//
//    // START CAMERA
//    private fun startCamera(
//        previewView: PreviewView
//    ) {
//
//        val cameraProviderFuture =
//            ProcessCameraProvider.getInstance(this)
//
//        cameraProviderFuture.addListener({
//
//            val cameraProvider =
//                cameraProviderFuture.get()
//
//            val preview =
//                Preview.Builder().build()
//
//            preview.setSurfaceProvider(
//                previewView.surfaceProvider
//            )
//
//            imageCapture =
//                ImageCapture.Builder()
//                    .setCaptureMode(
//                        ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
//                    )
//                    .build()
//
//            val cameraSelector =
//                CameraSelector.DEFAULT_FRONT_CAMERA
//
//            try {
//
//                cameraProvider.unbindAll()
//
//                cameraProvider.bindToLifecycle(
//                    this,
//                    cameraSelector,
//                    preview,
//                    imageCapture
//                )
//
//            } catch (e: Exception) {
//
//                e.printStackTrace()
//
//                Toast.makeText(
//                    this,
//                    "Camera Start Failed",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//        }, ContextCompat.getMainExecutor(this))
//    }
//
//    // CAPTURE IMAGE
//    private fun captureImage(
//        onBitmapReady: (Bitmap) -> Unit
//    ) {
//
//        val capture = imageCapture
//
//        if (capture == null) {
//
//            Toast.makeText(
//                this,
//                "Camera Not Ready",
//                Toast.LENGTH_SHORT
//            ).show()
//
//            return
//        }
//
//        capture.takePicture(
//
//            cameraExecutor,
//
//            object : ImageCapture.OnImageCapturedCallback() {
//
//                override fun onCaptureSuccess(
//                    image: ImageProxy
//                ) {
//
//                    try {
//
//                        val bitmap =
//                            imageProxyToBitmap(image)
//
//                        onBitmapReady(bitmap)
//
//                    } catch (e: Exception) {
//
//                        e.printStackTrace()
//
//                    } finally {
//
//                        image.close()
//                    }
//                }
//
//                override fun onError(
//                    exception: ImageCaptureException
//                ) {
//
//                    exception.printStackTrace()
//
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Capture Failed",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        )
//    }
//
//    // IMAGE TO BITMAP
//    private fun imageProxyToBitmap(
//        image: ImageProxy
//    ): Bitmap {
//
//        val buffer: ByteBuffer =
//            image.planes[0].buffer
//
//        val bytes =
//            ByteArray(buffer.remaining())
//
//        buffer.get(bytes)
//
//        return BitmapFactory.decodeByteArray(
//            bytes,
//            0,
//            bytes.size
//        )
//    }
//
//    // SIMPLE FACE EMBEDDING
//    private fun createEmbedding(
//        bitmap: Bitmap
//    ): FloatArray {
//
//        val resized =
//            Bitmap.createScaledBitmap(
//                bitmap,
//                32,
//                32,
//                true
//            )
//
//        val embedding =
//            FloatArray(32 * 32)
//
//        var index = 0
//
//        for (x in 0 until 32) {
//
//            for (y in 0 until 32) {
//
//                val pixel =
//                    resized.getPixel(x, y)
//
//                val r =
//                    (pixel shr 16) and 0xff
//
//                val g =
//                    (pixel shr 8) and 0xff
//
//                val b =
//                    pixel and 0xff
//
//                val gray =
//                    (r + g + b) / 3f
//
//                embedding[index] = gray
//
//                index++
//            }
//        }
//
//        return embedding
//    }
//
//    // COMPARE EMBEDDING
//    private fun compareEmbeddings(
//        emb1: FloatArray,
//        emb2: FloatArray
//    ): Float {
//
//        var sum = 0f
//
//        for (i in emb1.indices) {
//
//            val diff =
//                emb1[i] - emb2[i]
//
//            sum += diff * diff
//        }
//
//        return sqrt(sum)
//    }
//
//    override fun onDestroy() {
//
//        super.onDestroy()
//
//        cameraExecutor.shutdown()
//    }
//}Working fine background and alert dialog added





//Fcae Detection and Verification with CameraX and Jetpack Compose
//  08/05/2026

//import android.Manifest
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.widget.Toast
//import androidx.activity.compose.setContent
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.ImageCaptureException
//import androidx.camera.core.ImageProxy
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.view.PreviewView
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.ContextCompat
//import java.nio.ByteBuffer
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//import kotlin.math.sqrt
//
//
//
//
//
//
//
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import kotlinx.coroutines.delay
//import kotlin.math.sqrt
//
//class MainActivity : ComponentActivity() {
//
//    // CAMERA CAPTURE
//    private var imageCapture: ImageCapture? = null
//
//    private lateinit var cameraExecutor: ExecutorService
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        cameraExecutor = Executors.newSingleThreadExecutor()
//
//        setContent {
//
//            MaterialTheme {
//
//                FaceVerificationScreen()
//            }
//        }
//    }
//
//    // CAMERA PERMISSION
//    private val permissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { granted ->
//
//            if (!granted) {
//
//                Toast.makeText(
//                    this,
//                    "Camera Permission Denied",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//    @Composable
//    fun FaceVerificationScreen() {
//
//        val context = LocalContext.current
//
//        val previewView = remember {
//            PreviewView(context)
//        }
//
//        var firstEmbedding by remember {
//            mutableStateOf<FloatArray?>(null)
//        }
//
//        var resultText by remember {
//            mutableStateOf("No Face Captured")
//        }
//
//        var showDialog by remember {
//            mutableStateOf(false)
//        }
//
//        var blurScreen by remember {
//            mutableStateOf(false)
//        }
//
//        // AUTO FACE VERIFY
//        LaunchedEffect(firstEmbedding) {
//
//            while (firstEmbedding != null) {
//
//                delay(3000)
//
//                captureImage { bitmap ->
//
//                    val currentEmbedding =
//                        createEmbedding(bitmap)
//
//                    val savedEmbedding =
//                        firstEmbedding
//
//                    if (savedEmbedding != null) {
//
//                        val distance =
//                            compareEmbeddings(
//                                savedEmbedding,
//                                currentEmbedding
//                            )
//
//                        runOnUiThread {
//
//                            if (distance < 2000) {
//
//                                resultText =
//                                    "✅ Genuine Person"
//
//                                blurScreen = false
//
//                                showDialog = false
//
//                            } else {
//
//                                resultText =
//                                    "❌ Face Not Matched"
//
//                                blurScreen = true
//
//                                showDialog = true
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        Box(
//            modifier = Modifier.fillMaxSize()
//        ) {
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .then(
//                        if (blurScreen) {
//
//                            Modifier.graphicsLayer {
//                                alpha = 0.3f
//                            }
//
//                        } else {
//
//                            Modifier
//                        }
//                    )
//            ) {
//
//                // CAMERA PREVIEW
//                AndroidView(
//                    factory = {
//                        previewView
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f)
//                )
//
//                Spacer(modifier = Modifier.height(10.dp))
//
//                // CAPTURE BUTTON
//                Button(
//                    onClick = {
//
//                        checkPermissionAndStartCamera(
//                            previewView
//                        ) {
//
//                            captureImage { bitmap ->
//
//                                val embedding =
//                                    createEmbedding(bitmap)
//
//                                firstEmbedding = embedding
//
//                                resultText =
//                                    "✅ First Face Saved"
//
//                                Toast.makeText(
//                                    context,
//                                    "Face Captured Successfully",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(10.dp)
//                ) {
//
//                    Text("Capture First Face")
//                }
//
//                // RESULT TEXT
//                Text(
//                    text = resultText,
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//
//            // ALERT DIALOG
//            if (showDialog) {
//
//                AlertDialog(
//
//                    onDismissRequest = {},
//
//                    title = {
//
//                        Text(
//                            text = "Security Alert"
//                        )
//                    },
//
//                    text = {
//
//                        Text(
//                            text =
//                                "Face Not Matched or Object Detected"
//                        )
//                    },
//
//                    confirmButton = {
//
//                        Button(
//                            onClick = {
//
//                                showDialog = false
//
//                                blurScreen = false
//                            }
//                        ) {
//
//                            Text("OK")
//                        }
//                    }
//                )
//            }
//        }
//    }
//
//    // CHECK CAMERA PERMISSION
//    private fun checkPermissionAndStartCamera(
//        previewView: PreviewView,
//        onGranted: () -> Unit
//    ) {
//
//        when {
//
//            ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_GRANTED -> {
//
//                startCamera(previewView)
//
//                // WAIT CAMERA INIT
//                previewView.postDelayed({
//
//                    onGranted()
//
//                }, 1500)
//            }
//
//            else -> {
//
//                permissionLauncher.launch(
//                    Manifest.permission.CAMERA
//                )
//            }
//        }
//    }
//
//    // START CAMERA
//    private fun startCamera(
//        previewView: PreviewView
//    ) {
//
//        val cameraProviderFuture =
//            ProcessCameraProvider.getInstance(this)
//
//        cameraProviderFuture.addListener({
//
//            val cameraProvider =
//                cameraProviderFuture.get()
//
//            val preview =
//                Preview.Builder().build()
//
//            preview.setSurfaceProvider(
//                previewView.surfaceProvider
//            )
//
//            imageCapture =
//                ImageCapture.Builder()
//                    .setCaptureMode(
//                        ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
//                    )
//                    .build()
//
//            val cameraSelector =
//                CameraSelector.DEFAULT_FRONT_CAMERA
//
//            try {
//
//                cameraProvider.unbindAll()
//
//                cameraProvider.bindToLifecycle(
//                    this,
//                    cameraSelector,
//                    preview,
//                    imageCapture
//                )
//
//            } catch (e: Exception) {
//
//                e.printStackTrace()
//
//                Toast.makeText(
//                    this,
//                    "Camera Start Failed",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//        }, ContextCompat.getMainExecutor(this))
//    }
//
//    // CAPTURE IMAGE
//    private fun captureImage(
//        onBitmapReady: (Bitmap) -> Unit
//    ) {
//
//        val capture = imageCapture
//
//        if (capture == null) {
//
//            Toast.makeText(
//                this,
//                "Camera Not Ready",
//                Toast.LENGTH_SHORT
//            ).show()
//
//            return
//        }
//
//        capture.takePicture(
//
//            cameraExecutor,
//
//            object : ImageCapture.OnImageCapturedCallback() {
//
//                override fun onCaptureSuccess(
//                    image: ImageProxy
//                ) {
//
//                    try {
//
//                        val bitmap =
//                            imageProxyToBitmap(image)
//
//                        onBitmapReady(bitmap)
//
//                    } catch (e: Exception) {
//
//                        e.printStackTrace()
//
//                    } finally {
//
//                        image.close()
//                    }
//                }
//
//                override fun onError(
//                    exception: ImageCaptureException
//                ) {
//
//                    exception.printStackTrace()
//
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Capture Failed",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        )
//    }
//
//    // IMAGE TO BITMAP
//    private fun imageProxyToBitmap(
//        image: ImageProxy
//    ): Bitmap {
//
//        val buffer: ByteBuffer =
//            image.planes[0].buffer
//
//        val bytes =
//            ByteArray(buffer.remaining())
//
//        buffer.get(bytes)
//
//        return BitmapFactory.decodeByteArray(
//            bytes,
//            0,
//            bytes.size
//        )
//    }
//
//    // SIMPLE FACE EMBEDDING
//    private fun createEmbedding(
//        bitmap: Bitmap
//    ): FloatArray {
//
//        val resized =
//            Bitmap.createScaledBitmap(
//                bitmap,
//                32,
//                32,
//                true
//            )
//
//        val embedding =
//            FloatArray(32 * 32)
//
//        var index = 0
//
//        for (x in 0 until 32) {
//
//            for (y in 0 until 32) {
//
//                val pixel =
//                    resized.getPixel(x, y)
//
//                val r =
//                    (pixel shr 16) and 0xff
//
//                val g =
//                    (pixel shr 8) and 0xff
//
//                val b =
//                    pixel and 0xff
//
//                val gray =
//                    (r + g + b) / 3f
//
//                embedding[index] = gray
//
//                index++
//            }
//        }
//
//        return embedding
//    }
//
//    // COMPARE EMBEDDING
//    private fun compareEmbeddings(
//        emb1: FloatArray,
//        emb2: FloatArray
//    ): Float {
//
//        var sum = 0f
//
//        for (i in emb1.indices) {
//
//            val diff =
//                emb1[i] - emb2[i]
//
//            sum += diff * diff
//        }
//
//        return sqrt(sum)
//    }
//
//    override fun onDestroy() {
//
//        super.onDestroy()
//
//        cameraExecutor.shutdown()
//    }
//}   Fiish
//Fcae Detection and Verification with CameraX and Jetpack Compose
//  08/05/2026














//class MainActivity : ComponentActivity() {
//
//    // SAFE NULLABLE
//    private var imageCapture: ImageCapture? = null
//
//    private lateinit var cameraExecutor: ExecutorService
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        cameraExecutor = Executors.newSingleThreadExecutor()
//
//        setContent {
//
//            MaterialTheme {
//
//                FaceVerificationScreen()
//            }
//        }
//    }
//
//    // CAMERA PERMISSION
//    private val permissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { granted ->
//
//            if (!granted) {
//
//                Toast.makeText(
//                    this,
//                    "Camera Permission Denied",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//    @Composable
//    fun FaceVerificationScreen() {
//
//        val context = LocalContext.current
//
//        val previewView = remember {
//            PreviewView(context)
//        }
//
//        var firstEmbedding by remember {
//            mutableStateOf<FloatArray?>(null)
//        }
//
//        var resultText by remember {
//            mutableStateOf("No Face Captured")
//        }
//
//        Column(
//            modifier = Modifier.fillMaxSize()
//        ) {
//
//            // CAMERA PREVIEW
//            AndroidView(
//                factory = {
//                    previewView
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//            )
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            // FIRST FACE BUTTON
//            Button(
//                onClick = {
//
//                    checkPermissionAndStartCamera(
//                        previewView
//                    ) {
//
//                        captureImage { bitmap ->
//
//                            val embedding =
//                                createEmbedding(bitmap)
//
//                            firstEmbedding = embedding
//
//                            resultText =
//                                "✅ First Face Saved"
//                        }
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(10.dp)
//            ) {
//
//                Text("Capture First Face")
//            }
//
//            // VERIFY BUTTON
//            Button(
//                onClick = {
//
//                    checkPermissionAndStartCamera(
//                        previewView
//                    ) {
//
//                        captureImage { bitmap ->
//
//                            val secondEmbedding =
//                                createEmbedding(bitmap)
//
//                            val saved =
//                                firstEmbedding
//
//                            if (saved == null) {
//
//                                resultText =
//                                    "Capture First Face First"
//
//                                return@captureImage
//                            }
//
//                            val distance =
//                                compareEmbeddings(
//                                    saved,
//                                    secondEmbedding
//                                )
//
//                            resultText =
//                                if (distance < 2000) {
//
//                                    "✅ Genuine Person"
//
//                                } else {
//
//                                    "❌ Different Person"
//                                }
//                        }
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(10.dp)
//            ) {
//
//                Text("Verify Face")
//            }
//
//            // RESULT
//            Text(
//                text = resultText,
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//    }
//
//    // CHECK PERMISSION
//    private fun checkPermissionAndStartCamera(
//        previewView: PreviewView,
//        onGranted: () -> Unit
//    ) {
//
//        when {
//
//            ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_GRANTED -> {
//
//                startCamera(previewView)
//
//                // WAIT FOR CAMERA INIT
//                previewView.postDelayed({
//
//                    onGranted()
//
//                }, 1500)
//            }
//
//            else -> {
//
//                permissionLauncher.launch(
//                    Manifest.permission.CAMERA
//                )
//            }
//        }
//    }
//
//    // START CAMERA
//    private fun startCamera(
//        previewView: PreviewView
//    ) {
//
//        val cameraProviderFuture =
//            ProcessCameraProvider.getInstance(this)
//
//        cameraProviderFuture.addListener({
//
//            val cameraProvider =
//                cameraProviderFuture.get()
//
//            val preview =
//                Preview.Builder().build()
//
//            preview.setSurfaceProvider(
//                previewView.surfaceProvider
//            )
//
//            imageCapture =
//                ImageCapture.Builder()
//                    .setCaptureMode(
//                        ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
//                    )
//                    .build()
//
//            val cameraSelector =
//                CameraSelector.DEFAULT_FRONT_CAMERA
//
//            try {
//
//                cameraProvider.unbindAll()
//
//                cameraProvider.bindToLifecycle(
//                    this,
//                    cameraSelector,
//                    preview,
//                    imageCapture
//                )
//
//            } catch (e: Exception) {
//
//                e.printStackTrace()
//
//                Toast.makeText(
//                    this,
//                    "Camera Start Failed",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//        }, ContextCompat.getMainExecutor(this))
//    }
//
//    // CAPTURE IMAGE
//    private fun captureImage(
//        onBitmapReady: (Bitmap) -> Unit
//    ) {
//
//        val capture = imageCapture
//
//        if (capture == null) {
//
//            Toast.makeText(
//                this,
//                "Camera Not Ready",
//                Toast.LENGTH_SHORT
//            ).show()
//
//            return
//        }
//
//        capture.takePicture(
//
//            cameraExecutor,
//
//            object : ImageCapture.OnImageCapturedCallback() {
//
//                override fun onCaptureSuccess(
//                    image: ImageProxy
//                ) {
//
//                    try {
//
//                        val bitmap =
//                            imageProxyToBitmap(image)
//
//                        onBitmapReady(bitmap)
//
//                    } catch (e: Exception) {
//
//                        e.printStackTrace()
//
//                    } finally {
//
//                        image.close()
//                    }
//                }
//
//                override fun onError(
//                    exception: ImageCaptureException
//                ) {
//
//                    exception.printStackTrace()
//
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Capture Failed",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        )
//    }
//
//    // IMAGE TO BITMAP
//    private fun imageProxyToBitmap(
//        image: ImageProxy
//    ): Bitmap {
//
//        val buffer: ByteBuffer =
//            image.planes[0].buffer
//
//        val bytes =
//            ByteArray(buffer.remaining())
//
//        buffer.get(bytes)
//
//        return BitmapFactory.decodeByteArray(
//            bytes,
//            0,
//            bytes.size
//        )
//    }
//
//    // CREATE SIMPLE EMBEDDING
//    private fun createEmbedding(
//        bitmap: Bitmap
//    ): FloatArray {
//
//        val resized =
//            Bitmap.createScaledBitmap(
//                bitmap,
//                32,
//                32,
//                true
//            )
//
//        val embedding =
//            FloatArray(32 * 32)
//
//        var index = 0
//
//        for (x in 0 until 32) {
//
//            for (y in 0 until 32) {
//
//                val pixel =
//                    resized.getPixel(x, y)
//
//                val r =
//                    (pixel shr 16) and 0xff
//
//                val g =
//                    (pixel shr 8) and 0xff
//
//                val b =
//                    pixel and 0xff
//
//                val gray =
//                    (r + g + b) / 3f
//
//                embedding[index] = gray
//
//                index++
//            }
//        }
//
//        return embedding
//    }
//
//    // COMPARE EMBEDDINGS
//    private fun compareEmbeddings(
//        emb1: FloatArray,
//        emb2: FloatArray
//    ): Float {
//
//        var sum = 0f
//
//        for (i in emb1.indices) {
//
//            val diff =
//                emb1[i] - emb2[i]
//
//            sum += diff * diff
//        }
//
//        return sqrt(sum)
//    }
//
//    override fun onDestroy() {
//
//        super.onDestroy()
//
//        cameraExecutor.shutdown()
//    }
//}









class MainActivity : ComponentActivity() {
    val context = LocalContext.current
    val appPrefs = AppPreferences(context)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "login"
            ) {

                composable("login") {
                    LoginScreen(navController)
                }

                composable("signup") {   // ✅ ADD THIS
                    SignupScreen(navController)
                }

                composable("welcome") {
                    WelcomeScreen(navController)
                }
                composable("TestScreen") {

                    TestScreen()
                }
            }
        }
    }}







































//        setContent {
//            LoginScreen()
//        }

//        setContent {
//
//            val navController = rememberNavController()
//
//            NavHost(
//                navController = navController,
//                startDestination = "login"
//            ) {
//
//                composable("login") {
//                    LoginScreen(navController)
//                }
//
//                composable("welcome") {
//                    WelcomeScreen(navController)
//                }
//            }
//        }
//    }
//}


