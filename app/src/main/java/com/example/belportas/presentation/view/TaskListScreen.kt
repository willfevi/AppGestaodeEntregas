package com.example.belportas.presentation.view
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.belportas.model.TaskViewModel
import com.example.belportas.presentation.view.TaskCard
import com.example.belportas.view.AddTaskScreen

@Composable
fun TaskListScreen(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    onNavigateToBarcode: () -> Unit,
    onNavigateToFile: () -> Unit
) {
    val showAddTaskScreen = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val text = buildAnnotatedString {
                        val customGreen = Color(0xFF4CAF50)
                        withStyle(style = SpanStyle(color = Color.White)) {
                            append("App Bel")
                        }
                        withStyle(style = SpanStyle(color = customGreen)) {
                            append("portas")
                        }
                    }
                    Text(text = text)
                },
                actions = {
                    IconButton(onClick = onNavigateToBarcode) {
                        Icon(Icons.Default.Menu, contentDescription = "menu")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNavigateToFile()
                },
                content = { Icon(Icons.Default.Add, contentDescription = "Add") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(taskViewModel.tasks.value.orEmpty().toList().sortedBy { it.city }) { task ->
                TaskCard(task = task)
            }

        }
    }

    if (showAddTaskScreen.value) {
        AddTaskScreen(
            onNavigateBack = {
                showAddTaskScreen.value = false
            },
            taskViewModel = taskViewModel
        )
    }
}
