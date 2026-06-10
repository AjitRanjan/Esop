package faceembedding


import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavController
import com.example.esop.vibrate.FaceVerificationUtils
import java.util.concurrent.Executors


@Composable
fun TestInstructionsScreen(
    navController: NavController,
    onStartTestClick: () -> Unit = {},
    onGoBackClick: () -> Unit = {}
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraExecutor = remember {
        Executors.newSingleThreadExecutor()
    }

    var showCameraDialog by remember {
        mutableStateOf(false)
    }

    var imageCapture by remember {
        mutableStateOf<ImageCapture?>(null)
    }

    var firstEmbedding by remember {
        mutableStateOf<FloatArray?>(null)
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {
                showCameraDialog = true
            }
        }

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
            value = "50"
        )

        InstructionItem(
            icon = Icons.Default.EmojiEvents,
            title = "Total Marks",
            value = "50"
        )

        InstructionItem(
            icon = Icons.Default.AccessTime,
            title = "Time Duration",
            value = "60 Minutes"
        )

        InstructionItem(
            icon = Icons.Default.IndeterminateCheckBox,
            title = "Negative Marking",
            value = "No"
        )

        InstructionItem(
            icon = Icons.Default.EditNote,
            title = "Passing Marks",
            value = "60% (30 Marks)"
        )

        InstructionItem(
            icon = Icons.Default.ForkRight,
            title = "You can review & change",
            value = "answers before final submit.",
            showCard = false
        )

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                navController.navigate(
                    "TestScreen"
                )
//                val hasCameraPermission =
//                    ContextCompat.checkSelfPermission(
//                        context,
//                        Manifest.permission.CAMERA
//                    ) == PackageManager.PERMISSION_GRANTED
//
//                if (hasCameraPermission) {
//
//                    showCameraDialog = true
//
//                } else {
//
//                    permissionLauncher.launch(
//                        Manifest.permission.CAMERA
//                    )
//                }
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

//    if (showCameraDialog) {
//
//        Dialog(
//            onDismissRequest = {}
//        ) {
//
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(500.dp),
//
//                shape = RoundedCornerShape(16.dp)
//            ) {
//
//                Column {
//
//                    Text(
//                        text = "Please Look At Camera",
//                        modifier = Modifier.padding(16.dp),
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    AndroidView(
//
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .weight(1f),
//
//                        factory = { ctx ->
//
//                            PreviewView(ctx)
//                        },
//
//                        update = { previewView ->
//
//                            FaceVerificationUtils.startCamera(
//                                context = context,
//                                lifecycleOwner = lifecycleOwner,
//                                previewView = previewView
//                            ) { capture ->
//
//                                imageCapture = capture
//
//                                previewView.postDelayed({
//
//                                    FaceVerificationUtils.captureImage(
//                                        imageCapture = imageCapture,
//                                        cameraExecutor = cameraExecutor,
//                                        context = context
//                                    ) { bitmap ->
//
//                                        firstEmbedding =
//                                            FaceVerificationUtils
//                                                .createEmbedding(bitmap)
//
//                                        showCameraDialog = false
//
//                                        Toast.makeText(
//                                            context,
//                                            "Face Captured Successfully",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//
//                                        navController.navigate(
//                                            "TestScreen"
//                                        )
//                                    }
//
//                                }, 1500)
//
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    }
}

//        Button(
//            onClick = onStartTestClick,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(52.dp),
//            shape = RoundedCornerShape(8.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFF0B5EF7)
//            )
//        )
//        {
//
//            Text(
//                text = "Start New Test",
//                color = Color.White,
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.clickable {
//
//
//
//
//
//
//                    navController.navigate("TestScreen")
//                }
//            )
//        }
//    }
//}

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