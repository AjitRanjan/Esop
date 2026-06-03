package com.example.esop.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat

object ImeiUtils {

    fun getIMEI(context: Context): String {

        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return "Permission Not Granted"
        }

        return try {

            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE)
                        as TelephonyManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                telephonyManager.imei ?: "IMEI Not Available"
            } else {
                @Suppress("DEPRECATION")
                telephonyManager.deviceId ?: "IMEI Not Available"
            }

        } catch (e: Exception) {
            "IMEI Not Available"
        }
    }

    fun getAndroidId(context: Context): String {

        return android.provider.Settings.Secure.getString(
            context.contentResolver,
            android.provider.Settings.Secure.ANDROID_ID
        ) ?: ""
    }
}