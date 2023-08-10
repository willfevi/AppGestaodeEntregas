package com.example.belportas.presentation.view

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.belportas.R
import com.example.belportas.data.DeliveryStatus
import com.example.belportas.data.Task
import com.example.belportas.model.TaskViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun TaskListScreen(
    taskViewModel: TaskViewModel,
    onNavigateToBarcode: () -> Unit,
    onNavigateToFile: () -> Unit,
    onNavigateToAddTaskScreen: () -> Unit,
    onNavigateEditTaskScreen: (Task) -> Unit
) {
    val isSearchVisible = remember { mutableStateOf(false) }
    val searchTerm = remember { mutableStateOf(TextFieldValue("")) }
    val selectedDateMillis = remember { mutableStateOf<Long?>(null) }
    val context = LocalContext.current
    val tasksStateFlow = taskViewModel.tasks.collectAsState()
    val tasks = tasksStateFlow.value
        .filter { task -> task.deliveryStatus == DeliveryStatus.PEDIDO_EM_TRANSITO }
    val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    val selectedStatus = taskViewModel.getSelectedStatus()
    val filteredTasks = tasksStateFlow.value.filter { task ->
        val matches = task.deliveryStatus == selectedStatus &&
                task.noteNumber.contains(searchTerm.value.text) &&
                (selectedDateMillis.value == null || sdf.format(task.date) == sdf.format(selectedDateMillis.value))
        Log.d("Filtering", "Task: $task, Matches: $matches")
        matches
    }


    val isRefreshing by taskViewModel.isRefreshing.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val showDialog = remember { mutableStateOf(false) }


    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            Menu(onNavigateToBarcode,
                onRefresh = {
                    isRefreshing
                    taskViewModel.refreshDistancesForNull()
                },
                onNavigateToAddTaskScreen = onNavigateToAddTaskScreen,
                onNavigateToFile = onNavigateToFile,
                deleteAllTasks = { showDialog.value = true},
                taskViewModel = taskViewModel)
        },
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
                        itemsIndexed(filteredTasks.sortedWith(::compareByDistance)) { index, task ->
                            val isDetailsVisible = remember { mutableStateOf(false) }
                            DynamicTaskCard(task, isDetailsVisible, taskViewModel, onNavigateEditTaskScreen)
                        }
                    }
            }
        }
        if (showDialog.value) {
            ConfirmDialog(
                question = "Deseja mesmo limpar todas as tarefas ?",
                onConfirm = {
                    taskViewModel.deleteAllTasks()
                    showDialog.value = false
                },
                onDismissRequest = {
                    showDialog.value = false
                }
            )
        }
    }

fun compareByDistance(task1: Task, task2: Task): Int {
    val distance1 = task1.distance.split("km")[0].toIntOrNull() ?: 0
    val distance2 = task2.distance.split("km")[0].toIntOrNull() ?: 0
    return distance1.compareTo(distance2)
}

fun showDatePicker(context: Context, selectedDate: MutableState<Long?>) {
    val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecione a data:")
            .build()

    datePicker.addOnPositiveButtonClickListener { timestamp ->
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.add(Calendar.DAY_OF_MONTH, +1)
        selectedDate.value = calendar.timeInMillis
    }

    val activity = context as? AppCompatActivity
    activity?.supportFragmentManager?.let { fragmentManager ->
        datePicker.show(fragmentManager, "DATE_PICKER")
    }
}

@Composable
fun DynamicTaskCard(
    task: Task,
    isDetailsVisible: MutableState<Boolean>,
    taskViewModel: TaskViewModel,
    onNavigateEditTaskScreen: (Task) -> Unit
) {
    val updatedTaskState by rememberUpdatedState(task)

    when (updatedTaskState.deliveryStatus) {
        DeliveryStatus.PEDIDO_SEPARADO -> { AcceptTaskCard(updatedTaskState,taskViewModel) }
        DeliveryStatus.PEDIDO_EM_TRANSITO -> { TaskCard(updatedTaskState, isDetailsVisible,
            taskViewModel) { onNavigateEditTaskScreen(updatedTaskState) }}
        else -> { DoneTaskCard(updatedTaskState,taskViewModel)}
    }
}