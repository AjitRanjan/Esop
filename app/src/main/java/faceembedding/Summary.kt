package faceembedding

data class Summary(
    val totalQuestions: Int,
    val easyCount: Int,
    val mediumCount: Int,
    val hardCount: Int,
    val numberofAttempt: Int,
    val easyPercentage: Double,
    val mediumPercentage: Double,
    val hardPercentage: Double
)