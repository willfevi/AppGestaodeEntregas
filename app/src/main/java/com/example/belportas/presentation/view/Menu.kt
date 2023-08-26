package com.example.belportas.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.belportas.R
import com.example.belportas.data.DeliveryStatus
import com.example.belportas.model.TaskViewModel
import com.example.belportas.model.getFilePickerLauncher

@Composable
fun Menu(
    onNavigateToBarcode: () -> Unit,
    onRefresh: () -> Unit,
    onNavigateToAddTaskScreen: () -> Unit,
    createRoute: () -> Unit,
    markAllTasks: () -> Unit,
    taskViewModel: TaskViewModel,
    closeDrawerState:() -> Unit
) {
    val context = LocalContext.current
    val showFileError = remember { mutableStateOf(false) }
    val filePickerLauncher = getFilePickerLauncher(context, taskViewModel, showFileError)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(0.60f)) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_belportas_topbar),
                            contentDescription = "App top bar logo "
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                actions = {
                    Icon(Icons.Filled.Home, contentDescription = "Menu app")
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                MenuItem(
                    text = "Pedidos Separados",
                    icon = Icons.Filled.Star,
                    onClick = {
                        taskViewModel.setSelectedStatus(DeliveryStatus.PEDIDO_SEPARADO)
                        closeDrawerState()
                    }
                )
                MenuItem(
                    text = "Pedidos em Trânsito",
                    icon = Icons.Filled.Send,
                    onClick = {
                        taskViewModel.setSelectedStatus(DeliveryStatus.PEDIDO_EM_TRANSITO)
                        closeDrawerState()
                    }
                )
                MenuItem(
                    text = "Pedidos entregues",
                    icon = Icons.Filled.CheckCircle,
                    onClick = {
                        taskViewModel.setSelectedStatus(DeliveryStatus.PEDIDO_ENTREGUE)
                        closeDrawerState()
                    }
                )
                MenuItem(
                        text = "Adicionar todos os pedidos na rota de entrega",
                icon = Icons.Filled.Add,
                onClick = {
                    createRoute()
                    closeDrawerState()
                }
                )
                MenuItem(
                    text = "Marcar todas como entregues",
                    icon = Icons.Filled.Done,
                    onClick = {
                        markAllTasks()
                        closeDrawerState()
                    }
                )
                MenuItem(
                    text = "Digitar Entrega",
                    icon = Icons.Filled.Edit,
                    onClick = {
                        onNavigateToAddTaskScreen()
                        closeDrawerState()
                    }
                )
                MenuItem(
                    text = "Ir para arquivos",
                    icon = Icons.Filled.Search,
                    onClick = {
                        try {
                            showFileError.value = false
                            filePickerLauncher.launch("text/xml")
                        } catch (e: Exception) {
                            println(e.message)
                        }
                        closeDrawerState()
                    }
                )
                MenuItem(
                    text = "Abrir Scanner",
                    icon = Icons.Filled.List,
                    onClick = {
                        onNavigateToBarcode()
                        closeDrawerState()
                    }
                )
                MenuItem(
                    text = "Reorganizar rota",
                    icon = Icons.Filled.Refresh,
                    onClick = {
                        onRefresh()
                        closeDrawerState()
                   }
                )
            }
        }
    }
}

@Composable
fun MenuItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(0.9f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .padding(end = 16.dp)
            )
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface
            )
        }

        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = "Navigate indicator"
        )
    }
}

