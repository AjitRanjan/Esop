package com.example.esop.login

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: UserData?
)

data class UserData(
    val id: Int,
    val fullName: String,
    val email: String,
    val mobile: String,
    val designation: String,
    val organization: String
)
