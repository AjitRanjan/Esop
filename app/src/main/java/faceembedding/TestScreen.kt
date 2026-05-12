package faceembedding



import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.sqrt

@Composable
fun TestScreen() {

    val context = LocalContext.current

    val previewView = remember {
        PreviewView(context)
    }

    val cameraExecutor = remember {
        Executors.newSingleThreadExecutor()
    }

    var imageCapture by remember {
        mutableStateOf<ImageCapture?>(null)
    }

    var firstEmbedding by remember {
        mutableStateOf<FloatArray?>(null)
    }

    var resultText by remember {
        mutableStateOf("No Face Captured")
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var blurScreen by remember {
        mutableStateOf(false)
    }

    // CAMERA PREVIEW SHOW/HIDE
    var showCameraPreview by remember {
        mutableStateOf(true)
    }

    // CAMERA PERMISSION
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (!granted) {

                Toast.makeText(
                    context,
                    "Camera Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    // AUTO VERIFY
    LaunchedEffect(firstEmbedding) {

        while (firstEmbedding != null) {

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
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            // CAMERA PREVIEW
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

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // CAPTURE BUTTON
            Button(
                onClick = {

                    when {

                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED -> {

                            startCamera(
                                context = context,
                                previewView = previewView
                            ) { capture ->

                                imageCapture = capture

                                previewView.postDelayed({

                                    captureImage(
                                        imageCapture = imageCapture,
                                        cameraExecutor = cameraExecutor,
                                        context = context
                                    ) { bitmap ->

                                        val embedding =
                                            createEmbedding(bitmap)

                                        firstEmbedding =
                                            embedding

                                        resultText =
                                            "✅ First Face Saved"

                                        // HIDE CAMERA
                                        showCameraPreview = false

                                        Toast.makeText(
                                            context,
                                            "Face Captured Successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }, 1500)
                            }
                        }

                        else -> {

                            permissionLauncher.launch(
                                Manifest.permission.CAMERA
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {

                Text(
                    text = "Capture First Face"
                )
            }

            Text(
                text = resultText,
                modifier = Modifier.padding(16.dp)
            )
        }

        // ALERT DIALOG
        if (showDialog) {

            AlertDialog(

                onDismissRequest = {},

                title = {

                    Text(
                        text = "Security Alert"
                    )
                },

                text = {

                    Text(
                        text =
                            "Face Not Matched or Object Detected"
                    )
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