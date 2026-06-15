package com.example.esop.aadharField

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.esop.util.AadhaarMaskTransformation
import com.example.esop.R

@Composable
fun AadhaarField(value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= 12 && it.all { ch -> ch.isDigit() }) {
                onChange(it)
            }
        },
        label = { Text(stringResource(R.string.aadhaar_number)) },
        visualTransformation = AadhaarMaskTransformation(),
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}