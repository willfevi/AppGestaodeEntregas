package com.example.belportas.presentation.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.belportas.R
import com.example.belportas.model.OpenExternalApps
import com.example.belportas.model.data.Task
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TaskCard(
    task: Task,
    isDetailsVisible: MutableState<Boolean>
) {
    val context = LocalContext.current
    val userNote = remember { mutableStateOf("") }
    val isNoteFieldVisible = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateString = dateFormat.format(task.date)
    val openExternalApps = OpenExternalApps()

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
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = task.clientName,
                    fontWeight = FontWeight.Bold)
                }
            Row(verticalAlignment = Alignment.CenterVertically) {

                IconButton(
                    onClick = { openExternalApps.openPhone(context, task.phoneClient) }
                ) {
                    Icon(Icons.Filled.Phone,
                        contentDescription = "phone",
                        tint = colorResource(id = R.color.blue_phone_icon))
                }

                IconButton(onClick = {
                    if (task.phoneClient.length > 9) {
                        openExternalApps.openWpp(context, task.phoneClient, task.noteNumber, task.address)
                    } else {
                        openExternalApps.openWppIncorrectPhones(context, task.phoneClient, task.noteNumber, task.address)
                    }
                })
                { Icon(
                        painter = painterResource(id = R.drawable.ic_open_wpp),
                        contentDescription = "Whatsapp",
                        tint = colorResource(id = R.color.green_icon_wpp)
                    )
                }
                Text(
                    text ="    Tel:    ${task.phoneClient}",
                    fontWeight=FontWeight.Light)

                Spacer(
                    modifier = Modifier.padding(16.dp))
                Text(
                    text = "        (${task.distance} km)",
                    fontWeight = FontWeight.Light
                )

            }
            if (isDetailsVisible.value) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = task.address+task.cep,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .fillMaxSize(0.5f),
                        fontWeight=FontWeight.Light
                    )

                    IconButton(
                        onClick = { openExternalApps.openMap(context, task.cep) }
                    ) {
                        Icon(Icons.Filled.LocationOn,
                            contentDescription = "Open Location",
                            tint = colorResource(id = R.color.red),
                            modifier = Modifier.size(35.dp))
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.List, contentDescription = "ID entrega")
                    Text(text = "${task.id}", modifier = Modifier.padding(start = 8.dp))
                    Text(
                        text = "    R$ ${task.value}",
                        modifier = Modifier.padding(start = 8.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "       Data: $dateString",
                        fontWeight = FontWeight.Light
                    )
                }
                DefineProgress(indicator = task.deliveryStatus)
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