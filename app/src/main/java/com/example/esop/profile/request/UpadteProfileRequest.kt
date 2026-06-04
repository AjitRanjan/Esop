package com.example.esop.profile.request

data class UpadteProfileRequest(
    val appVersion: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val aadhaarId: String,
    val panNo: String,
    val drivingLicense: String,
    val age: Int,
    val gender: String,
    val address: String,
    val mobile: String,
    val processGroup: String,

    val organization: String,
    val functionary: String,

    val country: String,
    val state: String,
    val district: String,
    val city: String,
    val districtCode: String,
    val stateCode: String,
    val pincode: String,
    val usertypedesc: String,
    val usertype: String,
    val loginId: String,
    val designation: String,
    val processGroupId: String,
    val organizationId: String,
    val profileImage: String
)
//"usertype": "internal",
//    "usertypedesc":"
//pincode
//loginId
// "districtCode": "1710",
//    "district": "sonipat",
//    "stateCode": "17",
//    "state": "Haryana",