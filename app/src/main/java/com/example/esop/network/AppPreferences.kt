package com.example.esop.network

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.esop.login.UserDataStore
import com.example.esop.token.GetToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// ✅ Ye class ke bahar hona chahiye
private val Context.dataStore by preferencesDataStore(name = "app_prefs")
class AppPreferences(
    private val context: Context
) {

    private object Keys {
        val AUTHTOKEN = stringPreferencesKey("authToken")
        val USER_ID = stringPreferencesKey("user_id")
        val NAME = stringPreferencesKey("name")
        val EMAIL = stringPreferencesKey("email")
        val MOBILE = stringPreferencesKey("mobile")
        val DESIGNATION = stringPreferencesKey("designation")
        val PROCESSGROUP = stringPreferencesKey("processGroup")
        val LOGINID = stringPreferencesKey("loginId")
        val USERTYPE = stringPreferencesKey("usertype")
        val ORGANIZATION = stringPreferencesKey("organization")
        val LOGGED_IN = booleanPreferencesKey("logged_in")
    }

    // ✅ 👉 YAHI ADD KARNA HAI
    suspend fun saveUser(user: UserDataStore) {
        context.dataStore.edit { prefs ->
            prefs[Keys.USER_ID] = user.id
            prefs[Keys.NAME] = user.name
            prefs[Keys.EMAIL] = user.email
            prefs[Keys.MOBILE] = user.mobile
            prefs[Keys.PROCESSGROUP] = user.processGroup
            prefs[Keys.LOGINID] = user.loginId
            prefs[Keys.USERTYPE] = user.usertype
            prefs[Keys.DESIGNATION] = user.designation
            prefs[Keys.ORGANIZATION] = user.organization
            prefs[Keys.LOGGED_IN] = true
        }
    }



    suspend fun saveToke(user: GetToken) {
        context.dataStore.edit { prefs ->

            prefs[Keys.AUTHTOKEN] = user.authToken.toString()

            prefs[Keys.LOGGED_IN] = true
        }
    }


    // =========================
    // LOGOUT / CLEAR DATA
    // =========================

    suspend fun clearUser() {

        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
    // =========================
    // GET NAME
    // =========================


    val authToken: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.AUTHTOKEN]
        }
    val userName: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.NAME]
        }

    // =========================
    // GET EMAIL
    // =========================

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



}