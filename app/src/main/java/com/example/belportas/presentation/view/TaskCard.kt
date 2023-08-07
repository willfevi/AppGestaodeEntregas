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
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.belportas.R
import com.example.belportas.data.DeliveryStatus
import com.example.belportas.data.Task
import com.example.belportas.model.OpenExternalApps
import com.example.belportas.model.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TaskCard(
    task: Task,
    isDetailsVisible: MutableState<Boolean>,
    taskViewModel: TaskViewModel,
    onNavigateEditTaskScreen:()->Unit
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateString = dateFormat.format(task.date)
    val openExternalApps = OpenExternalApps()

    val showDialog = remember { mutableStateOf(false) }

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
                onClick = { /*onNavigateEditTaskScreen()*/ }

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
                    Text(
                        text = "        id:${task.id} ",
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
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                    .fillMaxWidth(0.8f))
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {showDialog.value = true}) {
                    Icon(Icons.Filled.Done,
                        contentDescription ="Entregue!",
                        tint= Color(0xFF2C7A30))
                }
                if (showDialog.value) {
                    ConfirmDialog(
                        question = "Deseja mesmo marcar essa entrega como concluida?",
                        onConfirm = {
                            task.deliveryStatus=DeliveryStatus.PEDIDO_ENTREGUE
                            taskViewModel.updateTask(task)
                            taskViewModel.getAllTasks()
                            showDialog.value = false
                        },
                        onDismissRequest = {
                            showDialog.value = false
                        }
                    )
                }
            }


            Row(verticalAlignment = Alignment.CenterVertically) {

                IconButton(
                    onClick = { openExternalApps.openPhone(context, task.phoneClient) }
                ) {
                    Icon(Icons.Filled.Phone,
                        contentDescription = "phone",
                        tint=Color(0xFF2196F3)
                    )
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
                        tint = Color(0xFF2C7A30)
                )

                }
                Text(
                    text ="    Tel:    ${task.phoneClient}",
                    fontWeight=FontWeight.Light,
                    style =MaterialTheme.typography.subtitle1)

                Spacer(
                    modifier = Modifier.padding(16.dp))
                Text(
                    text = "      (${task.distance} km)",
                    fontWeight = FontWeight.Bold
                )

            }
            if (isDetailsVisible.value) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = task.address+" "+task.cep,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .fillMaxSize(0.5f),
                        fontWeight=FontWeight.Light,
                        style =MaterialTheme.typography.subtitle2
                    )

                    IconButton(
                        onClick = { openExternalApps.openMap(context, task.address) }
                    ) {
                        Icon(Icons.Filled.LocationOn,
                            contentDescription = "Open Location",
                            modifier = Modifier.size(35.dp),
                            tint = Color(0xFFA2150A)
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "R$ ${task.value}",
                        modifier = Modifier.padding(start = 8.dp),
                        fontWeight = FontWeight.SemiBold,
                        style =MaterialTheme.typography.subtitle2
                    )
                    Text(
                        text = "       Data: $dateString",
                        style =MaterialTheme.typography.subtitle2,
                        fontWeight = FontWeight.Light
                    )
                }
                val indicator = task.deliveryStatus
                DefineProgressBar(indicator)
            }
        }
    }
}