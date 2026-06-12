package com.example.esop.AswersOptionSubmit.Repositry

import com.example.esop.AswersOptionSubmit.SubmitResponse

sealed class InsertExamState {

    object Idle : InsertExamState()

    object Loading : InsertExamState()

    data class Success(
        val response: SubmitResponse
    ) : InsertExamState()

    data class Error(
        val message: String
    ) : InsertExamState()
}