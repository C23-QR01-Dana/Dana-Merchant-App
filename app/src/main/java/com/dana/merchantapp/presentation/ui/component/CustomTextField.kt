package com.dana.merchantapp.presentation.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import com.dana.merchantapp.presentation.ui.theme.BluePrimary
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = BluePrimary) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.primary,
            unfocusedBorderColor = MaterialTheme.colors.primary
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),

        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    )
}
