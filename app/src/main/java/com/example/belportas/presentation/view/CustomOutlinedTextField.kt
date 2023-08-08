package com.example.belportas.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
@Composable
fun CustomOutlinedTextField(
    state: MutableState<String>,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    validator: (String) -> Boolean = { true },
    errorMessage: String = "",
    maxLength: Int = MAX_VALUE
) {
    val isError = remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = state.value,
            onValueChange = {
                if (it.length <= maxLength) {
                    if (it.isNotEmpty()) {
                        isError.value = !validator(it.trim())
                    } else {
                        isError.value = false
                    }
                    state.value = it
                }
            },
            label = { Text(text = label) },
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.onSurface,
                focusedLabelColor = MaterialTheme.colors.primary,
                unfocusedLabelColor = MaterialTheme.colors.onSurface
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            isError = isError.value
        )
        if (isError.value) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.error),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}
const val MAX_VALUE:Int =50