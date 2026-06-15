package com.example.esop.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(dateTime: String): String {

    return try {

        val parsedDate =
            OffsetDateTime.parse(dateTime)

        val indiaTime =
            parsedDate.atZoneSameInstant(
                ZoneId.of("Asia/Kolkata")
            )

        val formatter =
            DateTimeFormatter.ofPattern(
                "dd MMM yyyy, hh:mm a"
            )

        indiaTime.format(formatter)

    } catch (e: Exception) {
        ""
    }
}