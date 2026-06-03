package com.example.esop.state

data class StateResponse(

    val responseCode: Int,

    val responseDesc: String,

    val wrappedList: List<StateItem>
)
