package com.example.belportas.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onNavigateToFile: () -> Unit,
    deleteAllTasks: () -> Unit,
    taskViewModel: TaskViewModel
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
                    Box(modifier = Modifier.fillMaxWidth(0.40f)) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_belportas_topbar),
                            contentDescription = "App top bar logo "
                        )
                    }
                    Text(text = "   Menu")
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                actions = {
                        Icon(Icons.Filled.Home, contentDescription = "Settings")
                    }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
            ) {
                MenuItem(
                    text = "Pedidos Separados",
                    onClick = { taskViewModel.setSelectedStatus(DeliveryStatus.PEDIDO_SEPARADO) }
                )
                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color.Gray
                )
                MenuItem(
                    text = "Pedidos em Trânsito",
                    onClick = { taskViewModel.setSelectedStatus(DeliveryStatus.PEDIDO_EM_TRANSITO) }
                )
                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color.Gray
                )
                MenuItem(
                    text = "Pedidos entregues",
                    onClick = { taskViewModel.setSelectedStatus(DeliveryStatus.PEDIDO_ENTREGUE) }
                )
                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color.Gray
                )
                MenuItem(
                text = "Recarregar Lista",
                onClick = onRefresh
                )
                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color.Gray
                )
                MenuItem(
                    text = "Abrir Scanner",
                    onClick = onNavigateToBarcode
                )
                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color.Gray
                )
                MenuItem(
                    text = "Digitar Entrega",
                    onClick = onNavigateToAddTaskScreen
                )
                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color.Gray
                )
                MenuItem(
                    text = "Ir para arquivos",
                    onClick = {
                        try {
                            showFileError.value = false
                            filePickerLauncher.launch("text/xml")
                        } catch (e :Exception) {
                            println(e.message)
                        }
                    }
                )
                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color.Gray
                )
                MenuItem(
                    text = "Marcar todas como concluidas",
                    onClick = deleteAllTasks
                )
                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color.Gray
                )

            }
        }
    }
}

@Composable
fun MenuItem(
    text: String,
    onClick: () -> Unit,
) {
    Text(
        text = text,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onSurface
    )
}
