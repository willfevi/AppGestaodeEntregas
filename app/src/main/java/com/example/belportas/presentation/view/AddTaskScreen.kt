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
import com.example.belportas.model.ValidatingInputsFromTheAddTaskScreen
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
    val cityValue = remember { mutableStateOf("") }
    val ufValue = remember { mutableStateOf("") }
    val cepValue = remember { mutableStateOf("") }
    val distanceValue = remember { mutableStateOf("↻") }
    val clientNameValue = remember { mutableStateOf("") }
    val validator = remember { ValidatingInputsFromTheAddTaskScreen() }

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
                CustomOutlinedTextField(
                    state = noteNumberValue,
                    label = "Número da Nota",
                    keyboardType = KeyboardType.Number,
                    validator = validator::isValidNoteNumber,
                    errorMessage = "Não pode conter mais de 5 dígitos!",
                    maxLength = 5
                )

                CustomOutlinedTextField(
                    state = valueValue,
                    label = "Valor",
                    keyboardType = KeyboardType.Decimal,
                    validator = validator::isValidValue,
                    errorMessage = "Valor inválido!",
                    maxLength =5
                )

                CustomOutlinedTextField(addressValue, "Endereço")
                CustomOutlinedTextField(cityValue, "Cidade")
                CustomOutlinedTextField(
                    state = ufValue,
                    label = "Estado",
                    validator = validator::isValidUF,
                    errorMessage = "UF inválido! Use o formato 'SP', 'RJ', etc.",
                    maxLength = 2
                )
                CustomOutlinedTextField(
                    state = cepValue,
                    label = "CEP",
                    keyboardType = KeyboardType.Number,
                    validator = validator::isValidCEP,
                    errorMessage = "CEP inválido! Digite o CEP no formato 00.000-000",
                    maxLength = 10
                )
                CustomOutlinedTextField(clientNameValue, "Nome do Cliente")
                CustomOutlinedTextField(
                    state = phoneValue,
                    label = "Contato",
                    validator = validator::isValidPhoneNumber,
                    errorMessage = "Telefone inválido! Use o formato (xx)xxxxx-xxxx",
                    maxLength = 14
                )


                val context = LocalContext.current
                Button(
                    onClick = {
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
                        val currentDate = sdf.format(Date())

                        if (noteNumberValue.value.isNotEmpty()
                            && valueValue.value.isNotEmpty()
                            && addressValue.value.isNotEmpty()
                            && cityValue.value.isNotEmpty()
                            && ufValue.value.isNotEmpty()
                            && cepValue.value.isNotEmpty()
                            && clientNameValue.value.isNotEmpty()
                            && phoneValue.value.isNotEmpty()
                        ) {
                            val task = Task(
                                noteNumber = noteNumberValue.value,
                                value = valueValue.value + ",00",
                                address =" "+addressValue.value+" ,"+cityValue.value+" ,"+ufValue.value,
                                cep = cepValue.value,
                                distance =distanceValue.value,
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
