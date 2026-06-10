package faceembedding


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun TestInstructionsScreen(
    navController: NavController,
    onStartTestClick: () -> Unit = {},
    onGoBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
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
            onClick = onStartTestClick,
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
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {






                    navController.navigate("TestScreen")
                }
            )
        }
    }
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