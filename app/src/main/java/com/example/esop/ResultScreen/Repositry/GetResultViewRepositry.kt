package com.example.esop.ResultScreen.Repositry

import com.example.esop.AswersOptionSubmit.SubmitResponse
import com.example.esop.ResultScreen.GetResultViewRequest
import com.example.esop.ResultScreen.GetResultViewResponse
import com.example.esop.fialAnsweredSubmitApi.ResultInsertReq
import com.example.esop.network.RetrofitClient

class GetResultViewRepositry {
    suspend fun GetResultViewSubmit(
    request: GetResultViewRequest
): Result<GetResultViewResponse> {

    return try {

        val response =
            RetrofitClient.api.getResultView(
                request
            )

        Result.success(response)

    } catch (e: Exception) {

        Result.failure(e)
    }
}
}