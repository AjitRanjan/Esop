package com.example.esop.ResultScreen.UiSate

import com.example.esop.AswersOptionSubmit.SubmitResponse
import com.example.esop.ResultScreen.GetResultViewResponse
import com.example.esop.fialAnsweredSubmitApi.FinalInsertExamState

sealed class GetResultState  {
object Idle : GetResultState()

object Loading : GetResultState()

data class Success(
    val response: GetResultViewResponse
) : GetResultState()

data class Error(
    val message: String
) : GetResultState()
}