package faceembedding

import com.google.gson.annotations.SerializedName

data class SubmitExamRequest(

    @SerializedName("Course_Type")
    val courseType: Int,

    @SerializedName("courseName")
    val courseName: String,

    @SerializedName("certificateType")
    val certificateType: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("loginId")
    val loginId: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("answers")
    val answers: List<SubmitAnswer>
)