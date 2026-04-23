package com.example.esop.validation

import android.content.Context
import com.example.esop.LoginType
import com.example.esop.R


fun validateLogin(
    context: Context,
    loginType: LoginType,
    mobile: String,
    password: String,
    aadhaar: String
): String {

    return if (loginType == LoginType.AADHAAR) {
        if (aadhaar.length != 12) {
            context.getString(R.string.aadhaar_must_be_12_digits)
        } else {
            context.getString(R.string.login_success_aadhaar)
        }
    } else {
        when {
            mobile.length != 10 ->
                context.getString(R.string.mobile_must_be_10_digits)

            password.isEmpty() ->
                context.getString(R.string.enter_password)

            else -> "Login Success (Password)"
        }
    }
}