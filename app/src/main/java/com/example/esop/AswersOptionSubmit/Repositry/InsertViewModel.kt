package com.example.esop.AswersOptionSubmit.Repositry

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esop.AswersOptionSubmit.SubmitExamRequest
import kotlinx.coroutines.launch

class InsertViewModel : ViewModel() {

    private val repo =
        InsertExamRepository()

    var state by mutableStateOf<InsertExamState>(
        InsertExamState.Idle
    )
        private set

    fun insertSubmit(
        request: SubmitExamRequest
    ) {

        viewModelScope.launch {

            state =
                InsertExamState.Loading

            val result =
                repo.insertSubmit(request)

            state = result.fold(

                onSuccess = {

                    InsertExamState.Success(it)
                },

                onFailure = {

                    InsertExamState.Error(
                        it.message ?: "Error"
                    )
                }
            )
        }
    }
}