package faceembedding.Repositry

import faceembedding.SubmitResponse

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