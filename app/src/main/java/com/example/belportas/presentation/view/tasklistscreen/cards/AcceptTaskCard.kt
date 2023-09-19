package com.example.belportas.presentation.view.tasklistscreen.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.belportas.data.DeliveryStatus
import com.example.belportas.data.Task
import com.example.belportas.model.TaskViewModel
import com.example.belportas.presentation.view.ConfirmDialog

@Composable
fun AcceptTaskCard(
    task: Task,
    taskViewModel: TaskViewModel
) {
    val showDialog = remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.25.dp, Color.Gray),
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .shadow(16.dp)
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
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = task.clientName,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = { showDialog.value = true }
                ) {
                    Icon(
                        Icons.Filled.AddCircle,
                        contentDescription = "Nota",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }

            if (showDialog.value) {
                ConfirmDialog(
                    question = "Deseja mesmo adicionar essa entrega na rota atual?",
                    onConfirm = {
                        task.deliveryStatus = DeliveryStatus.PEDIDO_EM_TRANSITO
                        taskViewModel.updateTask(task)
                        taskViewModel.getAllTasks()
                        showDialog.value = false
                    },
                    onDismissRequest = {
                        showDialog.value = false
                    }
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Tel: ${task.phoneClient}",
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            val indicator = task.deliveryStatus
            DefineProgressBar(indicator)
        }
    }
}
