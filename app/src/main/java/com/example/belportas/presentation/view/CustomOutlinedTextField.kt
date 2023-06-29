package com.example.belportas.presentation.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CustomOutlinedTextField(
    state: MutableState<String>,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = state.value,
        onValueChange = { state.value = it },
        label = { Text(text = label) },
        modifier = Modifier
            .widthInFraction(0.95f)
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
        )
    )
}

fun Modifier.widthInFraction(fraction: Float): Modifier = this.then(
    Modifier.layout { measurable, constraints ->
        val maxWidth = constraints.maxWidth
        val width = (maxWidth * fraction).toInt()
        val placeable = measurable.measure(
            constraints.copy(
                minWidth = width,
                maxWidth = width
            )
        )
        layout(width, placeable.height) {
            placeable.place(0, 0)
        }
    }
)