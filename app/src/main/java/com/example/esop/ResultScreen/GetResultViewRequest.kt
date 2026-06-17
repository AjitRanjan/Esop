package com.example.esop.ResultScreen

import com.example.esop.mytest.MyTestItem

data class GetResultViewRequest(
    val loginId: String,
    val numberofAttempt: String,
    val certificateType: String,
    val departmentCetegory: String
)
