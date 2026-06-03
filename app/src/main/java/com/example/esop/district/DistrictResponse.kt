package com.example.esop.district

import com.example.esop.state.StateItem

data class DistrictResponse(

    val responseCode: Int,

    val responseDesc: String,

    val wrappedList: List<DistrictItem>
)
