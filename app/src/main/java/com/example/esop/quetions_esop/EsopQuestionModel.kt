package com.example.esop.quetions_esop

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esop.network.Resource
import com.example.esop.network.RetrofitClient
import com.example.esop.profile.ProfileRepository
import com.example.esop.profile.ProfileRequest
import com.example.esop.profile.ProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



class QuestionViewModel : ViewModel() {


    var selectedPosition by mutableStateOf(0)

    fun setPosition(pos: Int) {
        selectedPosition = pos
    }
    private val repository = QuestionRepository()

    var uiState by mutableStateOf<QuestionUiState>(
        QuestionUiState.Idle
    )
        private set

    private var apiCalled = false


    fun fetchQuestions(
        category: String,
        certytype: String,
        paacategory: String


    ) {

        // API already call ho chuki hai
        if (apiCalled) return

        apiCalled = true

        viewModelScope.launch {

            uiState = QuestionUiState.Loading

            when (
                val result = repository.getQuestions(
                    QuestiontReq(
                        category = category,
                        certytype = certytype,
                        paacategory = paacategory
                    )
                )
            ) {


                is Resource.Success -> {

                    result.data?.let {

                        uiState =
                            QuestionUiState.Success(it)
                    }
                }

                is Resource.Error -> {

                    // Error aaye to future retry allow kar do
                    apiCalled = false

                    uiState =
                        QuestionUiState.Error(
                            result.message
                                ?: "Unknown Error"
                        )
                }

                else -> {
                    apiCalled = false
                }
            }
        }
    }
}
//class QuestionViewModel : ViewModel() {
//
//    private val repository =
//        QuestionRepository()
//
//    var uiState by mutableStateOf<QuestionUiState>(
//        QuestionUiState.Idle
//    )
//        private set
//
//    fun fetchQuestions(
//        category: String
//    ) {
//
//        viewModelScope.launch {
//
//            uiState =
//                QuestionUiState.Loading
//
//            when (
//
//                val result =
//                    repository.getQuestions(
//
//                        QuestiontReq(
//                            category = category
//                        )
//                    )
//
//            ) {
//
//                is Resource.Success -> {
//
//                    result.data?.let {
//
//                        uiState =
//                            QuestionUiState.Success(
//                                it
//                            )
//                    }
//                }
//
//                is Resource.Error -> {
//
//                    uiState =
//                        QuestionUiState.Error(
//                            result.message
//                                ?: "Unknown Error"
//                        )
//                }
//
//                else -> {}
//            }
//        }
//    }
//}
