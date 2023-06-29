package com.example.belportas.presentation.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.belportas.model.TaskViewModel
import com.example.belportas.model.data.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

fun openMap(context: android.content.Context, address: String) {
    val encodedAddress = Uri.encode(address)

    val googleMapsIntentUri = Uri.parse("google.navigation:q=$encodedAddress")
    val googleMapsIntent = Intent(Intent.ACTION_VIEW, googleMapsIntentUri)

    val wazeIntentUri = Uri.parse("waze://?q=$encodedAddress")
    val wazeIntent = Intent(Intent.ACTION_VIEW, wazeIntentUri)

    val chooserIntent = Intent.createChooser(googleMapsIntent, "Compartilhar localização via").apply {
        val intentsArray = arrayOf(wazeIntent)
        putExtra(Intent.EXTRA_INITIAL_INTENTS, intentsArray)
    }

    try {
        context.startActivity(chooserIntent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Erro ao abrir o App!", Toast.LENGTH_SHORT).show()
    }
}

fun openPhone(context: android.content.Context, phone: String) {
    try {
        val intentUri = Uri.parse("tel:${Uri.encode(phone)}")
        val phoneIntent = Intent(Intent.ACTION_DIAL, intentUri)
        context.startActivity(phoneIntent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Aplicativo de telefone não encontrado!", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun TaskCard(
    task: Task,
    isDetailsVisible: MutableState<Boolean>,
    taskViewModel: TaskViewModel,
) {
    val context = LocalContext.current
    val userNote = remember { mutableStateOf("") }
    val isNoteFieldVisible = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateString = dateFormat.format(task.date)

    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.25.dp, Color.Gray),
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .shadow(16.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false),
                onClick = { isDetailsVisible.value = !isDetailsVisible.value }
            )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.List,
                        contentDescription = "Nota",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = "Número da Nota: ${task.noteNumber}",
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = { isDetailsVisible.value = !isDetailsVisible.value }
                ) {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = "Toggle details visibility"
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Person, contentDescription = "Client Name")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = task.clientName, fontSize = 20.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { openPhone(context, task.phoneClient) }
                ) {
                    Icon(Icons.Filled.Phone, contentDescription = "phone")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Celular: ${task.phoneClient}",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "    Distância: ${task.distance} km",
                    fontWeight = FontWeight.Bold
                )
            }
            if (isDetailsVisible.value) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = task.address,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                    IconButton(
                        onClick = { openMap(context, task.address) }
                    ) {
                        Icon(Icons.Filled.LocationOn, contentDescription = "Open Location")
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.List, contentDescription = "ID entrega")
                    Text(text = "${task.id}", modifier = Modifier.padding(start = 8.dp))
                    Text(
                        text = "R$ ${task.value}",
                        modifier = Modifier.padding(start = 8.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Data: $dateString")
                }
                Text(text = "Status de Entrega: ${task.deliveryStatus}")
                Button(
                    onClick = { isNoteFieldVisible.value = true }
                ) {
                    Text("Adicionar nota de observação")
                }

                if (isNoteFieldVisible.value) {
                    Row {
                        TextField(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            value = userNote.value,
                            onValueChange = { newValue -> userNote.value = newValue },
                            label = { Text("Digite sua observação") }
                        )
                        Button(
                            onClick = { isNoteFieldVisible.value = false }
                        ) {
                            Text("✔️")
                        }
                    }
                }
            }
        }
    }
}
