package com.example.esop.Result

import com.example.esop.AswersOptionSubmit.WrappedListaItem

data class ResultResponse(

    val responseCode: Int,

    val responseDesc: String,

    val wrappedList: List<WrappedResulttem>
)
