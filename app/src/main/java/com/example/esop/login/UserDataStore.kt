package com.example.esop.login

data class UserDataStore(
    val id: String,
    val name: String,
    val email: String,
    val mobile: String,
    val processGroup: String,
    val loginId: String,
    val usertype: String,
    val designation: String,
    val organization: String,
    val isLoggedIn: Boolean
)