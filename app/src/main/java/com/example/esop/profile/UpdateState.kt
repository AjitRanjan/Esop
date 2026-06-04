package com.example.esop.profile

import signup.SignupResponse

sealed class UpdateState {
    object Idle : UpdateState()
    object Loading : UpdateState()
    data class Success(val responseDesc: UpdateProfileResponse) : UpdateState()
    data class Error(val responseDesc: String) : UpdateState()
}
//sealed class SignupState {
//    object Idle : SignupState()
//    object Loading : SignupState()
//    data class Success(val data: SignupResponse) : SignupState()
//    data class Error(val message: String) : SignupState()
//}