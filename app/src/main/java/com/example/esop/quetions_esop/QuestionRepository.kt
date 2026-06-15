package com.example.esop.quetions_esop

import com.example.esop.network.Resource
import com.example.esop.network.RetrofitClient
class QuestionRepository {

    suspend fun getQuestions(
        request: QuestiontReq
    ): Resource<QuestionResponse> {

        return try {

            val response =
                RetrofitClient.api.getQuestions(
                    request
                )

            Resource.Success(response)

        } catch (e: Exception) {

            Resource.Error(
                e.message ?: "Unknown Error"
            )
        }
    }
}





//class QuestionRepository {
//
//    suspend fun getQuestions(
//        request: QuestiontReq
//    ): Resource<QuestionResponse> {
//
//        return try {
//            val response =
//                RetrofitClient.api.getQuestions(
//                    request
//                )
//            Resource.Success(response)
//
//        } catch (e: Exception) {
//
//            Resource.Error(
//                e.message ?: "Unknown Error"
//            )
//        }
//    }
//}