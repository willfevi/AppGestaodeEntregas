package com.example.belportas.presentation.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
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
import com.example.belportas.model.data.Task

fun openMap(context: android.content.Context, address: String, city:String) {
    val intentUri = Uri.parse("geo:0,0?q=${Uri.encode(address+city)}")
    val mapIntent = Intent(Intent.ACTION_VIEW, intentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    context.startActivity(mapIntent)
}

@Composable
fun TaskCard(task: Task) {
    val context = LocalContext.current
    val userNote = remember { mutableStateOf("") }
    val isNoteFieldVisible = remember { mutableStateOf(false) }
    val isDetailsVisible = remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(1.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, Color.Black),
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.List, contentDescription = "Nota")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Número da Nota: ${task.noteNumber}",
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { isDetailsVisible.value = !isDetailsVisible.value }) {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Toggle details visibility")
                }
            }

            if (isDetailsVisible.value) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Person, contentDescription = "Client Name")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = task.clientName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = task.address,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier
                            .fillMaxWidth(0.8f))
                    IconButton(
                        onClick = { openMap(context, task.address,task.city) },
                    ) {
                        Icon(Icons.Filled.LocationOn, contentDescription = "Open Location")
                    }
                }
                Text(text ="Cidade :${task.city}",
                    fontStyle = FontStyle.Italic)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.List, contentDescription = "ID entrega")
                    Text(text = "${task.id}", modifier = Modifier.padding(start = 8.dp))
                    Text(text = "R$ ${task.value}   ", modifier = Modifier.padding(start = 8.dp),fontWeight = FontWeight.Bold)
                    Text(text = "Data: ${task.date}")
                }

                Text(text = "Status de Entrega: ${task.deliveryStatus}")

                Button(onClick = { isNoteFieldVisible.value = true }) {
                    Text("Adicionar nota de observação")
                }

                if (isNoteFieldVisible.value) {
                    Row {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth(0.8f),
                            value = userNote.value,
                            onValueChange = { newValue -> userNote.value = newValue },
                            label = { Text("Digite sua observação") }
                        )
                        Button(onClick = {
                            isNoteFieldVisible.value = false
                        }) {
                            Text("✔️")
                        }
                    }
                }
            }
        }
    }
}
