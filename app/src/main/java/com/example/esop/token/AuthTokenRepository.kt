package com.example.esop.token

import com.example.esop.login.LoginRequest
import com.example.esop.login.LoginResponse
import com.example.esop.network.Resource
import com.example.esop.network.RetrofitClient

class AuthTokenRepository {

    suspend fun getToken(request: TokenRequest): Resource<GetToken> {
        return try {
            val response = RetrofitClient.api.getToken(request)
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