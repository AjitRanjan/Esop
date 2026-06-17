package com.example.esop.ResultScreen

import com.example.esop.mytest.MyTestItem

data class GetResultViewResponse(
    val responseCode: Int,

    val responseDesc: String,

    val wrappedList: List<GetResultItem>


)
