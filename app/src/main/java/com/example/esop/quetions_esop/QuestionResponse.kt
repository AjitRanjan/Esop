package com.example.esop.quetions_esop

data class QuestionResponse(
    val status: String,
    val message: String,
    val Course_Type: Int,
    val title: String,
    val Questions: List<Question>
)
