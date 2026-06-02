package com.example.esop.util


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object Base64Utils {

    fun base64ToBitmap(base64String: String?): Bitmap? {

        return try {

            if (base64String.isNullOrEmpty()) {
                null
            } else {

                val imageBytes = Base64.decode(
                    base64String,
                    Base64.DEFAULT
                )

                BitmapFactory.decodeByteArray(
                    imageBytes,
                    0,
                    imageBytes.size
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}