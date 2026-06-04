package com.example.esop.profile.Repositry

import com.example.esop.network.RetrofitClient
import com.example.esop.profile.request.UpadteProfileRequest
import signup.SignupResponse
import signup.request.SignupRequest

class UpdateProfileRepository {

    suspend fun UpdateProfile(request: UpadteProfileRequest): Result<SignupResponse> {
        return try {
            val response = RetrofitClient.api.UpdateUser(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}