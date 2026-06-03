package com.example.esop.profile

import com.example.esop.network.Resource
import com.example.esop.network.RetrofitClient

class ProfileRepository {

    suspend fun getProfile(
        token: String,
        request: ProfileRequest
    ): Resource<ProfileResponse> {

        return try {

            val response = RetrofitClient.api.getProfile(
                token = "Bearer $token",
                request = request
            )

            if (response.isSuccessful && response.body() != null) {

                Resource.Success(response.body()!!)

            } else {

                Resource.Error(
                    response.message()
                )
            }

        } catch (e: Exception) {

            Resource.Error(
                e.message ?: "Something went wrong"
            )
        }
    }
}