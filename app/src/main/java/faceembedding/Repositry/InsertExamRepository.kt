package faceembedding.Repositry

import com.example.esop.network.RetrofitClient
import faceembedding.SubmitExamRequest
import faceembedding.SubmitResponse
import signup.SignupResponse
import signup.request.SignupRequest

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