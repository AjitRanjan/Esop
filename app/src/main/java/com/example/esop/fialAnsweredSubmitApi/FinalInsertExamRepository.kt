package com.example.esop.fialAnsweredSubmitApi

import com.example.esop.network.RetrofitClient
import com.example.esop.AswersOptionSubmit.SubmitExamRequest
import com.example.esop.AswersOptionSubmit.SubmitResponse

class FinalInsertExamRepository {

    suspend fun FialinsertSubmit(
        request: ResultInsertReq
    ): Result<SubmitResponse> {

        return try {

            val response =
                RetrofitClient.api.insertresultsubmit(
                    request
                )

            Result.success(response)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}