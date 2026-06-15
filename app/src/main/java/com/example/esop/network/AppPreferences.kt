package com.example.esop.network

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.esop.login.UserDataStore
import com.example.esop.profile.UserType
import com.example.esop.token.GetToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_prefs")

class AppPreferences(
    private val context: Context
) {

    private object Keys {

        val DEPARTMENT = stringPreferencesKey("department")

        val AUTHTOKEN = stringPreferencesKey("authToken")
        val USER_ID = stringPreferencesKey("user_id")
        val NAME = stringPreferencesKey("name")
        val EMAIL = stringPreferencesKey("email")
        val MOBILE = stringPreferencesKey("mobile")
        val DESIGNATION = stringPreferencesKey("designation")
        val PROCESSGROUP = stringPreferencesKey("processGroup")
        val LOGINID = stringPreferencesKey("loginId")
        val USERTYPE = stringPreferencesKey("usertype")
        val PROCESSGROUPID = stringPreferencesKey("processGroupId")
        val ORGANIZATIONID = stringPreferencesKey("organizationId")
        val ORGANIZATION = stringPreferencesKey("organization")


        // Result Card Fields
        val TOTAL_QUESTIONS = intPreferencesKey("totalQuestions")
        val WRONG_ANS = intPreferencesKey("wrongAns")
        val NOT_ATTEMPTED = intPreferencesKey("notAttempted")
        val PERCENTAGE = intPreferencesKey("percentage")
        val CORRECT_ANS = intPreferencesKey("correctAns")
        val RESULT = intPreferencesKey("result")

        val LOGGED_IN = booleanPreferencesKey("logged_in")
    }

    // =========================
    // SAVE USER
    // =========================

    suspend fun saveUser(user: UserDataStore) {
        context.dataStore.edit { prefs ->

            prefs[Keys.USER_ID] = user.id
            prefs[Keys.NAME] = user.name
            prefs[Keys.EMAIL] = user.email
            prefs[Keys.MOBILE] = user.mobile
            prefs[Keys.PROCESSGROUP] = user.processGroup
            prefs[Keys.PROCESSGROUPID] = user.processGroupId.toString()
            prefs[Keys.LOGINID] = user.loginId
            prefs[Keys.USERTYPE] = user.usertype
            prefs[Keys.DESIGNATION] = user.designation
            prefs[Keys.ORGANIZATIONID] = user.organizationId.toString()
            prefs[Keys.ORGANIZATION] = user.organization

            prefs[Keys.LOGGED_IN] = true
        }
    }

    // =========================
    // SAVE TOKEN
    // =========================







    suspend fun saveToke(user: GetToken) {
        context.dataStore.edit { prefs ->
            prefs[Keys.AUTHTOKEN] = user.authToken.toString()
            prefs[Keys.LOGGED_IN] = true
        }
    }

    // =========================
    // SAVE RESULT
    // =========================

    suspend fun saveResult(
        totalQuestions: Int,
        wrongAns: Int,
        notAttempted: Int,
        percentage: Int,
        correctAns: Int,
        result: Int
    ) {
        context.dataStore.edit { prefs ->

            prefs[Keys.TOTAL_QUESTIONS] = totalQuestions
            prefs[Keys.WRONG_ANS] = wrongAns
            prefs[Keys.NOT_ATTEMPTED] = notAttempted
            prefs[Keys.PERCENTAGE] = percentage
            prefs[Keys.CORRECT_ANS] = correctAns
            prefs[Keys.RESULT] = result
        }
    }


    suspend fun saveDepartment(department: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DEPARTMENT] = department
        }
    }

    val department: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.DEPARTMENT]
        }
    // =========================
    // CLEAR DATA
    // =========================

    suspend fun clearUser() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    // =========================
    // USER DATA FLOWS
    // =========================

    val authToken: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.AUTHTOKEN]
        }

    val userName: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.NAME]
        }

    val userEmail: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.EMAIL]
        }

    val mobile: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.MOBILE]
        }

    val processGroup: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.PROCESSGROUP]
        }

    val organization: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.ORGANIZATION]
        }

    val loginId: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.LOGINID]
        }

    val usertype: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.USERTYPE]
        }

    val processGroupId: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.PROCESSGROUPID]
        }

    val organizationId: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.ORGANIZATIONID]
        }



    // =========================
    // RESULT FLOWS
    // =========================

    val totalQuestions: Flow<Int> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.TOTAL_QUESTIONS] ?: 0
        }

    val wrongAns: Flow<Int> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.WRONG_ANS] ?: 0
        }

    val notAttempted: Flow<Int> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.NOT_ATTEMPTED] ?: 0
        }

    val percentage: Flow<Int> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.PERCENTAGE] ?: 0
        }

    val correctAns: Flow<Int> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.CORRECT_ANS] ?: 0
        }

    val result: Flow<Int> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.RESULT] ?: 0
        }


}
