package com.example.esop.ProcessGroup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esop.network.RetrofitClient
import kotlinx.coroutines.launch

class ProcessGroupViewModel : ViewModel() {

    var processGroupList by mutableStateOf<List<ProcessGroupItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf("")

    fun fetchProcessGroups() {

        viewModelScope.launch {

            isLoading = true
            error = ""

            try {
                val response = RetrofitClient.api.getProcessGroupList()

                // ✅ Direct assign (NO FILTER)
                processGroupList = response

            } catch (e: Exception) {
                error = e.message ?: "Error"
            }

            isLoading = false
        }
    }
}