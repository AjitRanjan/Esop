package com.example.esop.quetions_esop

import com.example.esop.network.Resource
import com.example.esop.network.RetrofitClient

class QuestionRepository {

    suspend fun getQuestions(
        questionId: String
    ): Resource<QuestionResponse> {

        return try {

            val response =
                RetrofitClient.api.getQuestions(
                    questionId
                )

            Resource.Success(response)

        } catch (e: Exception) {

            Resource.Error(
                e.message ?: "Unknown Error"
            )
        }
    }
}