package com.example.belportas.presentation.view.tasklistscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DrawerValue
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.belportas.R
import com.example.belportas.data.DeliveryStatus
import com.example.belportas.model.TaskViewModel
import com.example.belportas.model.functionstasklistscreen.compareByDistanceThenName
import com.example.belportas.presentation.view.ConfirmDialog
import com.example.belportas.presentation.view.tasklistscreen.cards.DynamicTaskCard
import com.example.belportas.presentation.view.tasklistscreen.datepicker.showDatePicker
import com.example.belportas.presentation.view.tasklistscreen.menu.Menu
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TaskListScreen(
    taskViewModel: TaskViewModel,
    onNavigateToBarcode: () -> Unit,
    onNavigateToFile: () -> Unit,
    onNavigateToAddTaskScreen: () -> Unit,
    navController: NavController
) {
    val isSearchVisible = remember { mutableStateOf(false) }
    val searchTerm = remember { mutableStateOf(TextFieldValue("")) }
    val selectedDateMillis = remember { mutableStateOf<Long?>(null) }
    val context = LocalContext.current
    val tasksStateFlow = taskViewModel.tasks.collectAsState()

    val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    val selectedStatus = taskViewModel.getSelectedStatus()

    val filteredTasks = tasksStateFlow.value.filter { task ->
        val matches = task.deliveryStatus == selectedStatus &&
                task.noteNumber.contains(searchTerm.value.text) &&
                (selectedDateMillis.value == null || task.date?.let { sdf.format(it) } == sdf.format(selectedDateMillis.value))
        matches
    }

    val ongoingTasks = filteredTasks.filter { it.deliveryStatus == DeliveryStatus.PEDIDO_EM_TRANSITO }
    val completedTasks = filteredTasks.filter { it.deliveryStatus == DeliveryStatus.PEDIDO_ENTREGUE }
    val separatedTasks = filteredTasks.filter { it.deliveryStatus == DeliveryStatus.PEDIDO_SEPARADO }
    val deletedTasks = filteredTasks.filter { it.deliveryStatus == DeliveryStatus.PEDIDO_EXCLUIDO }

    val sortedOngoingTasks = ongoingTasks.sortedWith(::compareByDistanceThenName)
    val sortedCompletedTasks = completedTasks.sortedByDescending { it.date }
    val sortedDeletedTasks = deletedTasks.sortedByDescending { it.date }

    val finalSortedTasks = sortedOngoingTasks + sortedCompletedTasks + separatedTasks + sortedDeletedTasks

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val showDialogMakeRoute = remember { mutableStateOf(false) }

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            Menu(
                onNavigateToBarcode = onNavigateToBarcode,
                onRefresh = { taskViewModel.reloadAndUpdateDistancesForOngoingTasks() },
                onNavigateToAddTaskScreen = onNavigateToAddTaskScreen,
                createRoute = { showDialogMakeRoute.value = true },
                taskViewModel = taskViewModel,
                closeDrawerState = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                        if (!isSearchVisible.value) {
                            Box(modifier = Modifier.fillMaxWidth(0.50f)) {
                                Image(
                                    painter = painterResource(id = R.drawable.icon_belportas_topbar),
                                    contentDescription = "App top bar logo"
                                )
                            }
                        } else {
                            TextField(
                                value = searchTerm.value,
                                onValueChange = { newValue ->
                                    searchTerm.value = newValue
                                },
                                placeholder = {
                                    Text(
                                        modifier = Modifier,
                                        color = Color.White,
                                        text = "Digite o Nº da NFE:"
                                    )
                                },
                                maxLines = 1,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Search,
                                    keyboardType = KeyboardType.Number
                                ),
                                keyboardActions = KeyboardActions(onSearch = {
                                })
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearchVisible.value = !isSearchVisible.value }) {
                            Icon(Icons.Default.Search, contentDescription = "SearchNfeNumber")
                        }
                        IconButton(onClick = { showDatePicker(context, selectedDateMillis) }) {
                            Icon(Icons.Default.DateRange, contentDescription = "FilterDate")
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
                itemsIndexed(finalSortedTasks) { _, task ->
                    val isDetailsVisible = remember { mutableStateOf(false) }
                    DynamicTaskCard(task, isDetailsVisible, taskViewModel,editTaskScreen = {
                        navController.navigate("editTaskScreen/${task.id}")
                    })
                }
            }
        }
    }
    if (showDialogMakeRoute.value) {
        ConfirmDialog(
            question = "Deseja mesmo adicionar todas as tarefas separadas em sua rota ?",
            onConfirm = {
                taskViewModel.makeRoute()
                showDialogMakeRoute.value = false
            },
            onDismissRequest = {
                showDialogMakeRoute.value = false
            }
        )
    }
}


