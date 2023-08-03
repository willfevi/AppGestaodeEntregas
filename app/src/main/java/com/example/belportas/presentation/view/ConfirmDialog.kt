package com.example.belportas.presentation.view

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ConfirmDialog(
    question: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = "BelPortas alerta:") },
        text = { Text(text = question) },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("Confirmar", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text("Cancelar", color = Color.Red)
            }
        }
    )
}


@Composable
fun ConfirmDialogPermissions(
    question: String,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = "BelPortas alerta:") },
        text = { Text(text = question) },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("Reiniciar o App", color = Color.Red)
            }
        }
    )
}



