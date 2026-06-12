package com.example.esop.Result

import com.example.esop.network.RetrofitClient
import com.example.esop.AswersOptionSubmit.SubmitExamRequest
import com.example.esop.AswersOptionSubmit.SubmitResponse
import com.example.esop.fialAnsweredSubmitApi.ResultInsertReq
import retrofit2.http.Body

class ExamResultRepository {

    suspend fun GetResult(
        request: ResultGetReq
    ): Result<ResultResponse> {

        return try {

            val response =
                RetrofitClient.api.getresult(
                    request
                )

            Result.success(response)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}