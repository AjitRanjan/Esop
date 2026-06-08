package com.example.esop.quetions_esop

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esop.network.Resource
import com.example.esop.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



class QuestionViewModel : ViewModel() {

    private val repository = QuestionRepository()

    private val _questionState =
        MutableStateFlow<Resource<QuestionResponse>?>(null)

    val questionState =
        _questionState.asStateFlow()

    fun fetchQuestions(
        questionId: String
    ) {

        viewModelScope.launch {

            _questionState.value =
                Resource.Loading()

            _questionState.value =
                repository.getQuestions(
                    questionId = questionId
                )
        }
    }
}