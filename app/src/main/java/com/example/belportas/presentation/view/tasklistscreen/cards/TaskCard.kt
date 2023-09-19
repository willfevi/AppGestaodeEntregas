package com.example.belportas.presentation.view.tasklistscreen.cards

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.belportas.R
import com.example.belportas.data.Task
import com.example.belportas.model.ConfirmImage
import com.example.belportas.model.OpenExternalApps
import com.example.belportas.model.TaskViewModel
import com.example.belportas.presentation.view.ConfirmDialogDelete
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskCard(
    task: Task,
    isDetailsVisible: MutableState<Boolean>,
    taskViewModel: TaskViewModel,
    editTaskScreen: ()-> Unit
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateString = task.date?.let { dateFormat.format(it) }
    val openExternalApps = OpenExternalApps()

    val swipeableState = rememberSwipeableState(initialValue = 0f)


    val scope = rememberCoroutineScope()
    val iconOpacity = animateFloatAsState(
        targetValue = if (swipeableState.offset.value < -100) 1f else 0f,
        animationSpec = tween(500)
    ).value

    val anchors = mapOf(-250f to -1f, 0f to 0f)

    val confirmImage = ConfirmImage()

    val isImageConfirmed = remember { mutableStateOf(false) }

    val onImageConfirmed: (Bitmap?) -> Unit = { bitmap ->
        if (bitmap != null) {
            Log.d("CameraDebug", "Image confirmed!")
            confirmImage.confirmAndSaveImage(context, task.noteNumber)?.let {
                Log.d("CameraDebug", "Image saved successfully!")
                taskViewModel.markTaskAsDelivered(task.id)
                scope.launch {
                    swipeableState.snapTo(0f)
                }
                isImageConfirmed.value = true
            } ?: run {
                Log.d("CameraDebug", "Failed to save image.")
            }
        } else {
            Log.d("CameraDebug", "Bitmap is null.")
        }
    }
    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            try {
                Log.d("CameraDebug", "Attempting to decode bitmap from: ${task.imagePath}")
                val imageBitmap = BitmapFactory.decodeFile(task.imagePath)
                onImageConfirmed(imageBitmap)
            } catch (e: Exception) {
                e.printStackTrace()
                onImageConfirmed(null)
            }
        } else {
            Log.d("CameraDebug", "Picture was not taken successfully.")
        }
    }
    val showDialogDelete = remember { mutableStateOf(false) }

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
                        text = "${task.distance}km",
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
                Icon(
                    Icons.Filled.Done,
                    contentDescription = "Entregue!",
                    modifier = Modifier
                        .size(24.dp)
                        .alpha(iconOpacity)
                        .clickable {
                        activity?.let {
                            confirmImage.startCameraIntent(it, takePictureLauncher, task)
                        }
                        scope.launch {
                                swipeableState.snapTo(0f)
                        }
                    },
                    tint = Color(0xFF2C7A30)
                )
            }
            Spacer(modifier = Modifier.width(64.dp))

            AnimatedVisibility(visible = iconOpacity > 0) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            editTaskScreen()
                            scope.launch {
                                swipeableState.snapTo(0f)
                            }
                        }
                        .alpha(iconOpacity)
                )
            }
            Spacer(modifier = Modifier.width(64.dp))

            AnimatedVisibility(visible = iconOpacity > 0) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            showDialogDelete.value = true
                            scope.launch {
                                swipeableState.snapTo(0f)
                            }
                        }
                        .alpha(iconOpacity),
                    tint = Color(0xFF888888)
                )
            }
        }
        if (showDialogDelete.value) {
            val textFieldState = remember { mutableStateOf(task.observation ?: "") }
            ConfirmDialogDelete(
                question = "Deseja excluir essa entrega?",
                task = task,
                onConfirm = {
                    taskViewModel.updateTaskObservation(task.id, textFieldState.value)
                    showDialogDelete.value = false
                },
                onDismissRequest = { showDialogDelete.value = false },
                taskViewModel=taskViewModel
            )
        }
    }
}
fun Context.findActivity(): Activity? {
    val context = this
    while (context is ContextWrapper) {
        if (context is Activity)
            return context
    }
    return null
}