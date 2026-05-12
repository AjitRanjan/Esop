package signup.Repositry

import com.example.esop.network.RetrofitClient
import signup.SignupResponse
import signup.request.SignupRequest

class SignupRepository {

    suspend fun signup(request: SignupRequest): Result<SignupResponse> {
        return try {
            val response = RetrofitClient.api.signupUser(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}