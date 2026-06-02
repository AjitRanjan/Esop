package com.example.esop.token

data class GetToken(
    val authToken: String,
    val responseDesc: String?
)

//typealias AssignRoleResponse = List<GetToken>


//val login_id: String?,
//val role_cd: String?