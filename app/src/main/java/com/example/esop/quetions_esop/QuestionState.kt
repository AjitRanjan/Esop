package com.example.esop.quetions_esop




sealed class QuestionUiState {

    object Idle : QuestionUiState()

    object Loading : QuestionUiState()

    data class Success(
        val response: QuestionResponse
    ) : QuestionUiState()

    data class Error(
        val message: String
    ) : QuestionUiState()
}

//sealed class QuestionState {
//
//    object Idle : QuestionState()
//
//    object Loading : QuestionState()
//
//    data class Success(
//        val response: QuestionResponse
//    ) : QuestionState()
//
//    data class Error(
//        val message: String
//    ) : QuestionState()
//}