package com.example.esop

import com.example.esop.Result.WrappedResulttem

data class MyTestResponse(


    val responseCode: Int,

    val responseDesc: String,

    val wrappedList: List<MyTestItem>


)
