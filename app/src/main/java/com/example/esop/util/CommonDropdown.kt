package com.example.esop.util

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CommonDropdown(
    list: List<T>,
    selectedText: String,
    label: String,
    itemText: (T) -> String,
    onItemSelected: (T) -> Unit
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
            label = { Text(label) },   // ✅ dynamic label
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor()
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
                        Text(itemText(item))   // ✅ dynamic text
                    },
                    onClick = {
                        expanded = false

                        Toast.makeText(
                            context,
                            itemText(item),
                            Toast.LENGTH_SHORT
                        ).show()

                        onItemSelected(item)
                    }
                )
            }
        }
    }
}