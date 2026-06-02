package com.example.esop.profile

import com.example.esop.state.StateItem

data class ProfileResponse(

    val responseCode: Int,

    val responseDesc: String,

    val wrappedList: List<ProfileItem>
)
