package com.example.esop.OrgnazationDropdown

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esop.network.RetrofitClient
import kotlinx.coroutines.launch


class RoleViewModel : ViewModel() {

    var roleList by mutableStateOf<List<RoleItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf("")

    fun fetchRoles(id: String) {

        viewModelScope.launch {

            isLoading = true
            error = ""

            try {
                val response = RetrofitClient.api.getRoles(id)

                // Success Response Print
                println("Success Response : $response")

                // Logcat mein print
                Log.d("API_SUCCESS", response.toString())

                roleList = response

            } catch (e: Exception) {

                error = e.message ?: "Error"

                // Error Print
                Log.e("API_ERROR", e.message.toString())
            }

            isLoading = false
        }
    }

    fun clearRoles() {
        roleList = emptyList()
    }
}
//class RoleViewModel : ViewModel() {
//
//    var roleList by mutableStateOf<List<RoleItem>>(emptyList())
//        private set
//
//    var isLoading by mutableStateOf(false)
//    var error by mutableStateOf("")
//
//    fun fetchRoles(id: String) {
//
//        viewModelScope.launch {
//
//            isLoading = true
//            error = ""
//
//            try {
//                val response = RetrofitClient.api.getRoles(id)
//
//                roleList = response
//
//            } catch (e: Exception) {
//                error = e.message ?: "Error"
//            }
//
//            isLoading = false
//        }
//    }
//}