package com.example.esop

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation


//code use in commit today
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