package com.example.belportas.presentation.view

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.belportas.R
import com.example.belportas.data.DeliveryStatus
import com.example.belportas.data.Task
import com.example.belportas.model.ConfirmImage
import com.example.belportas.model.OpenExternalApps
import com.example.belportas.model.TaskViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskCard(
    task: Task,
    isDetailsVisible: MutableState<Boolean>,
    taskViewModel: TaskViewModel
) {
    val context = LocalContext.current

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateString = task.date?.let { dateFormat.format(it) }
    val openExternalApps = OpenExternalApps()

    val showDialog = remember { mutableStateOf(false) }

    val swipeableState = rememberSwipeableState(initialValue = 0f)
    val iconOpacity = animateFloatAsState(
        targetValue = if (swipeableState.offset.value < -100) 1f else 0f,
        animationSpec = tween(500)
    ).value

    val anchors = mapOf(-250f to -1f, 0f to 0f)

    val confirmImage = ConfirmImage()
    val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val currentPhotoPath = remember { mutableStateOf<String?>(null) }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .background(MaterialTheme.colors.background)
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(0.25.dp, Color.Gray),
            elevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background, RoundedCornerShape(8.dp))
                .offset(x = swipeableState.offset.value.dp)
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
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
                            text = "Número da Nota:${task.noteNumber}",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "        ID: ${task.id} ",
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
                    Text(
                        text = task.clientName,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                    )
                    Spacer(Modifier.weight(1f))
                    if (showDialog.value) {
                        ConfirmDialog(
                            question = "Deseja mesmo marcar essa entrega como concluida?",
                            onConfirm = {
                                task.deliveryStatus = DeliveryStatus.PEDIDO_ENTREGUE
                                task.date = Date()
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
                        Icon(
                            Icons.Filled.Phone,
                            contentDescription = "phone",
                            tint = Color(0xFF2196F3)
                        )
                    }
                    IconButton(onClick = {
                        if (task.phoneClient.length > 9) {
                            openExternalApps.openWpp(
                                context,
                                task.phoneClient,
                                task.noteNumber,
                                task.address
                            )
                        } else {
                            openExternalApps.openWppIncorrectPhones(
                                context,
                                task.phoneClient,
                                task.noteNumber,
                                task.address
                            )
                        }
                    })
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_open_wpp),
                            contentDescription = "Whatsapp",
                            tint = Color(0xFF2C7A30)
                        )

                    }
                    Text(
                        text = "    Tel:    ${task.phoneClient}",
                        fontWeight = FontWeight.Light,
                        style = MaterialTheme.typography.subtitle1
                    )

                    Spacer(
                        modifier = Modifier.padding(16.dp)
                    )
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
                            text = task.address + " " + task.cep,
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .fillMaxSize(0.5f),
                            fontWeight = FontWeight.Light,
                            style = MaterialTheme.typography.subtitle2
                        )

                        IconButton(
                            onClick = { openExternalApps.openMap(context, task.address) }
                        ) {
                            Icon(
                                Icons.Filled.LocationOn,
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
                            style = MaterialTheme.typography.subtitle2
                        )
                        Text(
                            text = "       Data: $dateString",
                            style = MaterialTheme.typography.subtitle2,
                            fontWeight = FontWeight.Light
                        )
                    }
                    val indicator = task.deliveryStatus
                    DefineProgressBar(indicator)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterEnd)
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            AnimatedVisibility(visible = iconOpacity > 0) {
                IconButton(onClick = {
                    Log.d("CameraDebug", "IconButton was clicked!")
                        confirmImage.simpleCameraIntent(context as Activity)
                }) {
                    Icon(
                        Icons.Filled.Done,
                        contentDescription = "Entregue!",
                        modifier = Modifier.size(24.dp).alpha(iconOpacity),
                        tint = Color(0xFF2C7A30)
                    )
                }
            }
            Spacer(modifier = Modifier.width(32.dp))

            AnimatedVisibility(visible = iconOpacity > 0) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {}
                            .alpha(iconOpacity)
                    )
                }
            }
            Spacer(modifier = Modifier.width(32.dp))

            AnimatedVisibility(visible = iconOpacity > 0) {
                IconButton(onClick = {taskViewModel.deleteAllTasks()}) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {}
                            .alpha(iconOpacity),
                        tint = Color(0xFF888888)
                    )
                }
            }
        }
    }
    ImagePreviewDialog(bitmap = imageBitmap.value, onConfirm = {
        showDialog.value = true
    }, onCancel = {
        currentPhotoPath.value?.let {
            File(it).delete()
        }
    })

}
@Composable
fun ImagePreviewDialog(bitmap: Bitmap?, onConfirm: () -> Unit, onCancel: () -> Unit) {
    if (bitmap != null) {
        AlertDialog(
            onDismissRequest = onCancel,
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(onClick = onCancel) {
                    Text("Cancelar")
                }
            },
            title = { Text(text = "Pré-visualização da Foto") },
            text = {
                Image(bitmap = bitmap.asImageBitmap(), contentDescription = null)
            }
        )
    }
}
