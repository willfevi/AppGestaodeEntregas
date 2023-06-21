package com.example.belportas.view
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.belportas.model.data.Task
import com.example.belportas.R
import com.example.belportas.model.TaskViewModel
import com.example.belportas.presentation.view.widthInFraction


@Composable
fun AddTaskScreen(
    onNavigateBack: () -> Unit,
    taskViewModel: TaskViewModel
) {
    val idValue = remember { mutableStateOf("") }
    val noteNumberValue = remember { mutableStateOf("") }
    val valueValue = remember { mutableStateOf("") }
    val addressValue = remember { mutableStateOf("") }
    val cityValue = remember { mutableStateOf("") }
    val deliveryStatusValue = remember { mutableStateOf("") }
    val dateValue = remember { mutableStateOf("") }
    val clientNameValue = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val text = buildAnnotatedString {
                        val customGreen = Color(0xFF4CAF50)
                        withStyle(style = SpanStyle(color = Color.White)) {
                            append("App Bel")
                        }
                        withStyle(style = SpanStyle(color = customGreen)) {
                            append("portas")
                        }
                    }
                    Text(text = text)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = idValue.value,
                    onValueChange = { idValue.value = it },
                    label = { Text(text = "ID") },
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
                        keyboardType = KeyboardType.Number
                    )
                )
                OutlinedTextField(
                    value = noteNumberValue.value,
                    onValueChange = { noteNumberValue.value = it },
                    label = { Text(text = "Número da Nota") },
                    modifier = Modifier
                        .widthInFraction(0.95f)
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface,
                        focusedLabelColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = MaterialTheme.colors.onSurface
                    )
                )
                OutlinedTextField(
                    value = valueValue.value,
                    onValueChange = { valueValue.value = it },
                    label = { Text(text = "Valor") },
                    modifier = Modifier
                        .widthInFraction(0.95f)
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface,
                        focusedLabelColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = MaterialTheme.colors.onSurface
                    )
                )
                OutlinedTextField(
                    value = addressValue.value,
                    onValueChange = { addressValue.value = it },
                    label = { Text(text = "Endereço") },
                    modifier = Modifier
                        .widthInFraction(0.95f)
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface,
                        focusedLabelColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = MaterialTheme.colors.onSurface
                    )
                )
                OutlinedTextField(
                    value = deliveryStatusValue.value,
                    onValueChange = { deliveryStatusValue.value = it },
                    label = { Text(text = "Status de Entrega") },
                    modifier = Modifier
                        .widthInFraction(0.95f)
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface,
                        focusedLabelColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = MaterialTheme.colors.onSurface
                    )
                )
                OutlinedTextField(
                    value = dateValue.value,
                    onValueChange = { dateValue.value = it },
                    label = { Text(text = "Data") },
                    modifier = Modifier
                        .widthInFraction(0.95f)
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface,
                        focusedLabelColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = MaterialTheme.colors.onSurface
                    )
                )
                OutlinedTextField(
                    value = clientNameValue.value,
                    onValueChange = { clientNameValue.value = it },
                    label = { Text(text = "Nome do Cliente") },
                    modifier = Modifier
                        .widthInFraction(0.95f)
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface,
                        focusedLabelColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = MaterialTheme.colors.onSurface
                    )
                )
                Button(
                    onClick = {
                        val task = Task(
                            id = idValue.value.toLongOrNull() ?: 0,
                            noteNumber = noteNumberValue.value,
                            value = valueValue.value,
                            address = addressValue.value,
                            city=cityValue.value,
                            deliveryStatus = deliveryStatusValue.value,
                            date = dateValue.value,
                            clientName = clientNameValue.value
                        )
                        taskViewModel.addTask(task)
                        onNavigateBack()

                    },
                    modifier = Modifier
                        .widthInFraction(0.9f)
                        .padding(top = 16.dp)
                ) {
                    Text(text = "Adicionar Tarefa")
                }
            }
        }
    )
}