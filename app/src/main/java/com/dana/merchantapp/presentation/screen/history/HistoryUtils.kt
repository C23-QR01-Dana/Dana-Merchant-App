package com.dana.merchantapp.presentation.screen.history

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.util.*

class ThousandSeparatorTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = formatNumber(text.text)
        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var transformedOffset = 0
                var originalOffset = 0

                formatted.forEach { char ->
                    if (originalOffset == offset) return transformedOffset
                    if (char != ',') originalOffset++
                    transformedOffset++
                }
                return transformedOffset
            }
            override fun transformedToOriginal(offset: Int): Int {
                var transformedOffset = 0
                var originalOffset = 0

                formatted.forEach { char ->
                    if (transformedOffset == offset) return originalOffset
                    if (char != ',') originalOffset++
                    transformedOffset++
                }
                return originalOffset
            }
        }
        return TransformedText(AnnotatedString(formatted), offsetTranslator)
    }

    private fun formatNumber(input: String): String {
        val number = input.toLongOrNull() ?: return input
        val amountFormatter = java.text.NumberFormat.getNumberInstance(Locale.US)
        return amountFormatter.format(number)
    }
}