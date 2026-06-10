package faceembedding

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.esop.quetions_esop.Question

//@Composable
//fun ExamScreen(
//    navController: NavController,
//    questionList: List<Question>
//) {
//
//    var currentQuestionIndex by remember {
//        mutableIntStateOf(0)
//    }
//
//    var selectedAnswer by remember {
//        mutableStateOf("")
//    }
//
//    val currentQuestion =
//        questionList.getOrNull(currentQuestionIndex)
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF7F3FA))
//    ) {
//
//        // ================= HEADER =================
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(
//                    Brush.horizontalGradient(
//                        listOf(
//                            Color(0xFF8E6BC7),
//                            Color(0xFFD9CCE9)
//                        )
//                    )
//                )
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            Card(
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(12.dp)
//                ) {
//                    Text(
//                        text = "Candidate ID : 12345",
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    Text(
//                        text = "Candidate Name : Ajit"
//                    )
//                }
//            }
//
//            Box(
//                contentAlignment = Alignment.Center
//            ) {
//
//                CircularProgressIndicator(
//                    progress = 0.7f,
//                    modifier = Modifier.size(70.dp),
//                    strokeWidth = 5.dp,
//                    color = Color.White
//                )
//
//                Text(
//                    text = "29:19",
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//
//            Icon(
//                imageVector = Icons.Default.Menu,
//                contentDescription = null,
//                modifier = Modifier.size(30.dp)
//            )
//        }
//
//        // ================= QUESTION SECTION =================
//
//        currentQuestion?.let { question ->
//
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(16.dp)
//            ) {
//
//                Text(
//                    text = "Question ${currentQuestionIndex + 1} / ${questionList.size}",
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Text(
//                    text = question.questionTitle,
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.SemiBold
//                )
//
//                Spacer(modifier = Modifier.height(25.dp))
//
//                question.options.forEach { option ->
//
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 8.dp)
//                            .clickable {
//                                selectedAnswer = option.option_Key
//                            },
//                        colors = CardDefaults.cardColors(
//                            containerColor =
//                                if (selectedAnswer == option.option_Key)
//                                    Color(0xFFE7D9F8)
//                                else
//                                    Color.White
//                        ),
//                        elevation = CardDefaults.cardElevation(4.dp)
//                    ) {
//
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//
//                            RadioButton(
//                                selected = selectedAnswer == option.option_Key,
//                                onClick = {
//                                    selectedAnswer = option.option_Key
//                                }
//                            )
//
//                            Spacer(modifier = Modifier.width(12.dp))
//
//                            Text(
//                                text = option.option_value,
//                                fontSize = 16.sp
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//        // ================= ACTION BUTTONS =================
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 10.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//
//            Button(
//                modifier = Modifier.weight(1f),
//                onClick = { },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF4CAF50)
//                )
//            ) {
//                Text("Save & Next")
//            }
//
//            Button(
//                modifier = Modifier.weight(1f),
//                onClick = { },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFFFFC107)
//                )
//            ) {
//                Text("Save & Review")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 10.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//
//            Button(
//                modifier = Modifier.weight(1f),
//                onClick = { },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF03A9F4)
//                )
//            ) {
//                Text("Mark")
//            }
//
//            Button(
//                modifier = Modifier.weight(1f),
//                onClick = {
//                    selectedAnswer = ""
//                },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Gray
//                )
//            ) {
//                Text("Clear")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        // ================= PREVIOUS NEXT =================
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 10.dp),
//            horizontalArrangement = Arrangement.spacedBy(10.dp)
//        ) {
//
//            OutlinedButton(
//                modifier = Modifier.weight(1f),
//                onClick = {
//
//                    if (currentQuestionIndex > 0) {
//                        currentQuestionIndex--
//                    }
//                }
//            ) {
//                Text("Previous")
//            }
//
//            OutlinedButton(
//                modifier = Modifier.weight(1f),
//                onClick = {
//
//                    if (currentQuestionIndex < questionList.lastIndex) {
//                        currentQuestionIndex++
//                        selectedAnswer = ""
//                    }
//                }
//            ) {
//                Text("Next")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(15.dp))
//
//        // ================= SUBMIT =================
//
//        Button(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//                .height(55.dp),
//            onClick = {
//
//                // Submit API
//            },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFF6E4FB3)
//            )
//        ) {
//            Text(
//                text = "Submit Exam",
//                fontSize = 18.sp
//            )
//        }
//    }
//}