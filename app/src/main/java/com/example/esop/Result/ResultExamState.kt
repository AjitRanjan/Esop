package com.example.esop.Result

import com.example.esop.AswersOptionSubmit.SubmitResponse

sealed class ResultExamState {

    object Idle : ResultExamState()

    object Loading : ResultExamState()

    data class Success(
        val response: ResultResponse
    ) : ResultExamState()

    data class Error(
        val message: String
    ) : ResultExamState()
}