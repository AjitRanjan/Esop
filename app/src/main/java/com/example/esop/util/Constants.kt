package com.example.esop.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {

    const val APP_PREFERENCES = "app_preferences"
    const val FULLName = "fullName"
//    const val EMAIL = "email"
//    const val MOBILE = "mobile"
//    const val DESIGNATION = "designation"
//    const val ORGANIZATION = "organization"
//    const val TOKEN = "token"
//    const val LOGGED_IN = "is_logged_in"
//    const val LANGUAGE_KEY = "app_language"

    const val DDUGKY = "https://kaushal.dord.gov.in/demobackend/"
//    const val DDUGKY = "https://kaushal.dord.gov.in/demobackend/esop/"
//    const val RSETI = "https://kaushal.dord.gov.in/demobackend/rsetiofflineapp/"

//    const val DDUGKY = "https://kaushal.rural.gov.in/backend/ddugkyofflineapp/"
//    const val RSETI = "https://kaushal.rural.gov.in/backend/rsetiofflineapp/"




    // ✅ NEW USER DATA
    val NAME = stringPreferencesKey("name")
    val EMAIL = stringPreferencesKey("email")
    val MOBILE = stringPreferencesKey("mobile")
    val DESIGNATION = stringPreferencesKey("designation")
    val ORGANIZATION = stringPreferencesKey("organization")


}