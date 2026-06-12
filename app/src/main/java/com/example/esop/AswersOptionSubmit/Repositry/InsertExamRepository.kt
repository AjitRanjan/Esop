package com.example.esop.AswersOptionSubmit.Repositry

import com.example.esop.network.RetrofitClient
import com.example.esop.AswersOptionSubmit.SubmitExamRequest
import com.example.esop.AswersOptionSubmit.SubmitResponse

class InsertExamRepository {

    suspend fun insertSubmit(
        request: SubmitExamRequest
    ): Result<SubmitResponse> {

        return try {

            val response =
                RetrofitClient.api.insertsubmit(
                    request
                )

            Result.success(response)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}