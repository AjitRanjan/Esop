package com.example.esop.ProcessGroup

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esop.network.RetrofitClient
import kotlinx.coroutines.launch


//class ProcessGroupDropdown : ViewModel() {
//
//    var processGroupList by mutableStateOf<List<ProcessGroupItem>>(emptyList())
//        private set
//
//    var isLoading by mutableStateOf(false)
//    var error by mutableStateOf("")
//
//    fun fetchProcessGroups() {
//
//        viewModelScope.launch {
//
//            isLoading = true
//            error = ""
//
//            try {
//                val response = RetrofitClient.api.getProcessGroupList()
//
//                // ✅ Allowed names list
//                val allowedNames = listOf(
//                    "Mord", "CTSA", "TSA", "SRLM", "PIA", "Others"
//                )
//
//                // ✅ FILTER WITH IGNORE CASE
//                processGroupList = response.filter { item ->
//                    allowedNames.any { allowed ->
//                        item.level_short_name.equals(allowed, ignoreCase = true)
//                    }
//                }
//
//            } catch (e: Exception) {
//                error = e.message ?: "Error"
//            }
//
//            isLoading = false
//        }
//    }
//}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcessGroupDropdown(
    list: List<ProcessGroupItem>,
    selectedText: String,
    onItemSelected: (ProcessGroupItem) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Process Group") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()   // 🔥 IMPORTANT FIX
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            if (list.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No Data") },
                    onClick = { expanded = false }
                )
            }

            list.forEach { item ->

                DropdownMenuItem(
                    text = {
                        Text(item.level_short_name ?: "")
                    },
                    onClick = {
                        expanded = false

                        Toast.makeText(
                            context,
                            item.level_admin_cd ?: "",
                            Toast.LENGTH_SHORT
                        ).show()

                        onItemSelected(item)
                    }
                )
            }
        }
    }
}


