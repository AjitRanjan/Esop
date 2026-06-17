package com.example.esop.ResultScreen.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esop.ResultScreen.GetResultViewRequest
import com.example.esop.ResultScreen.Repositry.GetResultViewRepositry
import com.example.esop.ResultScreen.UiSate.GetResultState
import com.example.esop.fialAnsweredSubmitApi.FinalInsertExamRepository
import com.example.esop.fialAnsweredSubmitApi.FinalInsertExamState
import com.example.esop.fialAnsweredSubmitApi.ResultInsertReq
import kotlinx.coroutines.launch

class GetResultViewModel : ViewModel() {

    private val repo =
        GetResultViewRepositry()

    var state by mutableStateOf<GetResultState>(
        GetResultState.Idle
    )
        private set

    fun FinalinsertSubmit(
        request: GetResultViewRequest
    ) {

        viewModelScope.launch {

            state =
                GetResultState.Loading

            val result =
                repo.GetResultViewSubmit(request)

            state = result.fold(

                onSuccess = {

                    GetResultState.Success(it)
                },

                onFailure = {

                    GetResultState.Error(
                        it.message ?: "Error"
                    )
                }
            )
        }
    }
}