package com.example.esop.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation


//Dev_Ajitranjan 22.04.2026 create branch
class AadhaarMaskTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {

        val masked = buildString {
            text.text.forEachIndexed { index, c ->
                if (index < text.length - 4) append("*")
                else append(c)
            }
        }

        return TransformedText(
            AnnotatedString(masked),
            OffsetMapping.Identity
        )
    }
}