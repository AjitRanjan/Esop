package com.example.esop.FunctionaryDropdown

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esop.network.RetrofitClient
import kotlinx.coroutines.launch

class FunctionaryViewModel : ViewModel() {

    var functionaryList by mutableStateOf<List<AssignRoleItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf("")

    fun fetchFunctionaries(orgId: String) {

        // ❗ guard (bahut important)
        if (orgId.isEmpty()) return

        viewModelScope.launch {

            isLoading = true
            error = ""

            try {
                val response = RetrofitClient.api.getFunctionaries(orgId)

                functionaryList = response

            } catch (e: Exception) {
                error = e.message ?: "Error"
            }

            isLoading = false
        }
    }
}