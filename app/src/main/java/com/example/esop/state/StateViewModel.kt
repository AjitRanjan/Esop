package com.example.esop.state

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esop.OrgnazationDropdown.RoleItem
import com.example.esop.network.RetrofitClient
import kotlinx.coroutines.launch

class StateViewModel : ViewModel() {

    var stateList by mutableStateOf<List<StateItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf("")
        private set

    fun fetchState() {

        viewModelScope.launch {

            isLoading = true

            try {

                val response = RetrofitClient.api.getStateList()

                Log.d("STATE_RESPONSE", response.toString())

                // ✅ NOW WORKING
                stateList = response.wrappedList

            } catch (e: Exception) {

                error = e.message ?: "Unknown Error"

                Log.e("STATE_ERROR", e.toString())
            }

            isLoading = false
        }
    }
}
//class StateViewModel : ViewModel() {
//
//    var stateList by mutableStateOf<List<Item>>(emptyList())
//        private set
//
//    var isLoading by mutableStateOf(false)
//    var error by mutableStateOf("")
//
//    fun fetchState() {
//
//        viewModelScope.launch {
//
//            isLoading = true
//            error = ""
//
//            try {
//                val response = RetrofitClient.api.getStateList()
//
//                // Success Response Print
//                println("Success Response : $response")
//
//                // Logcat mein print
//                Log.d("API_SUCCESS", response.toString())
//
//                stateList = response
//
//            } catch (e: Exception) {
//
//                error = e.message ?: "Error"
//
//                // Error Print
//                Log.e("API_ERROR", e.message.toString())
//            }
//
//            isLoading = false
//        }
//    }
//
//}