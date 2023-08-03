package com.example.belportas.presentation.view

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.belportas.R
import com.example.belportas.data.DeliveryStatus
import com.example.belportas.data.Task
import com.example.belportas.model.TaskViewModel
import java.util.Date

@Composable
fun AcceptRouteScreen(
    onNavigateBack: () -> Unit,
    taskViewModel:TaskViewModel
){
    val tasksStateFlow = taskViewModel.tasks.collectAsState()
    val tasks = tasksStateFlow.value
    val colorRed= Color(0xFFFC483B)
    Scaffold(


        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(0.50f)) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_belportas_topbar),
                            contentDescription = "App top bar logo"
                        )
                    }
                },
                backgroundColor = colorRed
            )
        },
        floatingActionButton = {
            val colorOrange= Color(0xFFFD7058)
            FloatingActionButton(
                onClick = {},
                content = {
                    Image(
                        painter = painterResource(R.drawable.ic_carro_de_entrega),
                        contentDescription = "Delivery Car",
                        modifier = Modifier.fillMaxSize()
                    )
                },
                modifier = Modifier.size(72.dp),
                backgroundColor = colorOrange
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(tasks) { task ->
                AcceptTaskCard(task = task)
            }
        }
    }
}


