package com.example.esop.fialAnsweredSubmitApi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esop.AswersOptionSubmit.Repositry.InsertExamRepository
import com.example.esop.AswersOptionSubmit.Repositry.InsertExamState
import com.example.esop.AswersOptionSubmit.SubmitExamRequest
import kotlinx.coroutines.launch

class FinalInsertViewModel : ViewModel() {

    private val repo =
        FinalInsertExamRepository()

    var state by mutableStateOf<FinalInsertExamState>(
        FinalInsertExamState.Idle
    )
        private set

    fun FinalinsertSubmit(
        request: ResultInsertReq
    ) {

        viewModelScope.launch {

            state =
                FinalInsertExamState.Loading

            val result =
                repo.FialinsertSubmit(request)

            state = result.fold(

                onSuccess = {

                    FinalInsertExamState.Success(it)
                },

                onFailure = {

                    FinalInsertExamState.Error(
                        it.message ?: "Error"
                    )
                }
            )
        }
    }
}