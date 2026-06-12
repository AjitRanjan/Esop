package com.example.esop.Result

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esop.AswersOptionSubmit.Repositry.InsertExamRepository
import com.example.esop.AswersOptionSubmit.Repositry.InsertExamState
import com.example.esop.AswersOptionSubmit.SubmitExamRequest
import com.example.esop.fialAnsweredSubmitApi.FinalInsertExamRepository
import com.example.esop.fialAnsweredSubmitApi.FinalInsertExamState
import com.example.esop.fialAnsweredSubmitApi.ResultInsertReq
import kotlinx.coroutines.launch

class ResultViewModel : ViewModel() {
    private val repo =
        ExamResultRepository()

    var state by mutableStateOf<ResultExamState>(
        ResultExamState.Idle
    )
        private set

    fun GetResult(
        request: ResultGetReq
    ) {

        viewModelScope.launch {

            state =
                ResultExamState.Loading

            val result =
                repo.GetResult(request)

            state = result.fold(

                onSuccess = {

                    ResultExamState.Success(it)
                },

                onFailure = {

                    ResultExamState.Error(
                        it.message ?: "Error"
                    )
                }
            )
        }
    }
}