package com.example.belportas.presentation.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.belportas.R
import com.example.belportas.data.DeliveryStatus
import com.example.belportas.data.Task
import com.example.belportas.model.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddTaskScreen(
    onNavigateBack: () -> Unit,
    taskViewModel: TaskViewModel
) {
    val noteNumberValue = remember { mutableStateOf("") }
    val phoneValue = remember { mutableStateOf("") }
    val valueValue = remember { mutableStateOf("") }
    val addressValue = remember { mutableStateOf("") }
    val cepValue = remember { mutableStateOf("") }
    val distanceValue = remember { mutableStateOf("↻") }
    val clientNameValue = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(0.50f)) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_belportas_topbar),
                            contentDescription = "App top bar logo "
                        )
                    }
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
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomOutlinedTextField(noteNumberValue, "Número da Nota", KeyboardType.Number)
                CustomOutlinedTextField(valueValue, "Valor", KeyboardType.Decimal)
                CustomOutlinedTextField(addressValue, "Endereço")
                CustomOutlinedTextField(cepValue, "CEP", KeyboardType.Number)
                CustomOutlinedTextField(clientNameValue, "Nome do Cliente")
                CustomOutlinedTextField(phoneValue, "Contato", KeyboardType.Phone)

                val context = LocalContext.current
                Button(
                    onClick = {
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
                        val currentDate = sdf.format(Date())

                        if (noteNumberValue.value.isNotEmpty()
                            && valueValue.value.isNotEmpty()
                            && addressValue.value.isNotEmpty()
                            && cepValue.value.isNotEmpty()
                            && clientNameValue.value.isNotEmpty()
                            && phoneValue.value.isNotEmpty()
                        ) {
                            val task = Task(
                                noteNumber = noteNumberValue.value,
                                value = valueValue.value,
                                address = addressValue.value,
                                cep = cepValue.value,
                                distance = distanceValue.value,
                                deliveryStatus = DeliveryStatus.PEDIDO_SEPARADO,
                                date = sdf.parse(currentDate),
                                clientName = clientNameValue.value,
                                phoneClient = phoneValue.value
                            )
                            taskViewModel.addTask(task)
                            Toast.makeText(
                                context,
                                "Tarefa inserida com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                            onNavigateBack()

                        } else {
                            Toast.makeText(
                                context,
                                "Preencha todos os campos da tarefa!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 16.dp)
                ) {
                    Text(text = "Adicionar Tarefa")
                }
            }
        }
    )
}