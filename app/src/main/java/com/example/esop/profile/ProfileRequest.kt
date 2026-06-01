package com.example.esop.profile

data class ProfileRequest(
    val email: String,
    val firstName: String,
    val lastName: String,

    val alternateEmail: String?,

    val aadhaarId: String,
    val panNo: String,
    val drivingLicense: String,

    val age: Int,
    val gender: String,

    val address: String,
    val mobile: String,
    val telephone: String,

    val processGroup: String,
    val organization: String,
    val functionary: String,

    val country: String,
    val state: String,
    val district: String,
    val city: String,

    val designation: String
)
