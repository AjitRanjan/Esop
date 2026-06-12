package com.example.esop.fialAnsweredSubmitApi

import com.example.esop.AswersOptionSubmit.SubmitResponse

sealed class FinalInsertExamState {

    object Idle : FinalInsertExamState()

    object Loading : FinalInsertExamState()

    data class Success(
        val response: SubmitResponse
    ) : FinalInsertExamState()

    data class Error(
        val message: String
    ) : FinalInsertExamState()
}