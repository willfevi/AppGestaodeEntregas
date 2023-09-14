package com.example.belportas.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.belportas.data.Task
import com.example.belportas.model.TaskViewModel

@Composable
fun ConfirmDialogDelete(
    question: String,
    task: Task,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    taskViewModel: TaskViewModel
) {
    var textFieldState by remember { mutableStateOf(TextFieldValue()) }
    var showError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = {
            textFieldState = TextFieldValue()
            showError = false
            onDismissRequest()
        },
        title = { Text(text = "BelPortas alerta:") },
        text = {
            Column {
                Text(text = question)
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = textFieldState,
                    onValueChange = { newTextFieldValue ->
                        textFieldState = newTextFieldValue
                        showError = newTextFieldValue.text.trim().isEmpty()
                    },
                    label = { Text("Digite o motivo da exclusão da entrega :") },
                    isError = showError,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp))
                        .padding(12.dp)
                )

                if (showError) {
                    Text("Este campo é obrigatório", color = Color.Red)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (textFieldState.text.trim().isNotEmpty()) {
                    taskViewModel.updateTaskObservation(task.id, textFieldState.text.trim())
                    onConfirm()
                    textFieldState = TextFieldValue()
                    showError = false
                    onDismissRequest()
                } else {
                    showError = true
                }
            }) {
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
                Text("Fechar o App", color = Color.Red)
            }
        }
    )
}



