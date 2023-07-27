package com.example.belportas.presentation.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Menu(onNavigateToBarcode: () -> Unit,
         onRefresh: () -> Unit ,
         onNavigateToAddTaskScreen: () -> Unit,
         onNavigateToFile: () -> Unit,
         deleteAllTasks:()->Unit
        ) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopAppBar(
                title = { Text("Menu") },
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
                    onClick = onNavigateToFile
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
