package com.example.esop.login

import com.example.esop.network.Resource
import com.example.esop.network.RetrofitClient

class AuthRepository {

    suspend fun login(request: LoginRequest): Resource<LoginResponse> {
        return try {
            val response = RetrofitClient.api.login(request)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Something went wrong")
        }
    }
}