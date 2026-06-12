package com.example.esop.Result

import com.google.gson.annotations.SerializedName

data class ResultGetReq(

    @SerializedName("loginId")
    val loginId: String,
    @SerializedName("emailId")
    val emailId: String,

)