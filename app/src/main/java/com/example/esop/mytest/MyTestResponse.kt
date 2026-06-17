package com.example.esop.mytest

data class MyTestResponse(


    val responseCode: Int,

    val responseDesc: String,

    val wrappedList: List<MyTestItem>


)
