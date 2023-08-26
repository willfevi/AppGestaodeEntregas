package com.example.belportas.model

import android.app.Application
import android.location.Location
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.belportas.data.BelDatabase
import com.example.belportas.data.DeliveryStatus
import com.example.belportas.data.LocationService
import com.example.belportas.data.Task
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import java.util.concurrent.Semaphore

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val contextReference = WeakReference(application.applicationContext)

    private val locationService = LocationService(getApplication())
    private val userLocation = mutableStateOf(Location(""))
    private val calculateDistanceSemaphore = Semaphore(1)

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch(Dispatchers.Main) {
            contextReference.get()?.let {
                Toast.makeText(it, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val taskDao = BelDatabase.getDatabase(application).taskDao()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    init {
        initializeLocation()
    }

    fun initializeLocation() {
        locationService.startLocationUpdates {
            userLocation.value = it
            updateAllDistances()
        }
        getAllTasks()
    }
    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val existingTask = taskDao.getAll().find { it.noteNumber == task.noteNumber }
                if (existingTask != null) {
                    showToastMessage("Já existe uma tarefa ${task.noteNumber} com esse mesmo número de nota.")
                    return@launch
                }

                val distance = locationService.calculateDistanceAsync(userLocation.value, task.address, task.noteNumber).await()

                if (distance <= 0 || distance == Long.MAX_VALUE) {
                    showToastMessage("Erro ao calcular a distância, tarefa não adicionada.")
                    return@launch
                }

                val taskWithDistance = task.copy(distance = distance.toString())
                taskDao.insert(taskWithDistance)
                _tasks.value = taskDao.getAll()
                showToastMessage("Tarefa ${task.noteNumber} adicionada com sucesso à lista.")
            } catch (e: Exception) {
                showToastMessage("Erro ao adicionar tarefa: ${e.message}")
            }
        }
    }
    private fun showToastMessage(message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            contextReference.get()?.let {
                Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun getTaskById(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val result = taskDao.getTaskById(taskId)
                withContext(Dispatchers.Main) {
                    _task.value = result
                }
            } catch (e: Exception) {
                showToastMessage("Erro ao buscar tarefa por ID: ${e.message}")
            }
        }
    }
    fun reloadAndUpdateDistancesForOngoingTasks() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val ongoingTasks = withContext(Dispatchers.IO) {
                taskDao.getTasksByStatus(DeliveryStatus.PEDIDO_EM_TRANSITO)
            }
            val updatedTasks = ongoingTasks.map { task ->
                val newDistance = locationService.calculateDistanceAsync(userLocation.value, task.address, task.noteNumber).await()
                task.copy(distance = if (newDistance > 0 && newDistance != Long.MAX_VALUE) newDistance.toString() else task.distance)
            }
            var qtdTarefas=0
            withContext(Dispatchers.IO) {
                updatedTasks.forEach { updatedTask ->
                    taskDao.update(updatedTask)
                    qtdTarefas++
                }
            }
            showToastMessage("Todas as  $qtdTarefas estão com distância atualizada!")

            _tasks.value = withContext(Dispatchers.IO) {
                taskDao.getAll().sortedBy { it.distance }
            }
        }
    }


    fun addTaskExternal(task: Task) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val existingTask = taskDao.getAll().find { it.noteNumber == task.noteNumber }
                if (existingTask != null) {
                    showToastMessage("Já existe uma tarefa ${task.noteNumber} com esse mesmo número de nota.")
                    return@launch
                }

                val currentLocation = locationService.getCurrentLocationAsync().await()
                userLocation.value = currentLocation

                if (task.address.isEmpty()) {
                    showToastMessage("O endereço da tarefa não está definido.")
                    return@launch
                }

                val distance = locationService.calculateDistanceAsync(userLocation.value, task.address, task.noteNumber).await()

                if (distance > 0 && distance != Long.MAX_VALUE) {
                    val taskWithDistance = task.copy(distance = distance.toString())
                    taskDao.insert(taskWithDistance)
                    _tasks.value = taskDao.getAll()
                    showToastMessage("Tarefa ${task.noteNumber} adicionada com sucesso à lista.")
                } else {
                    showToastMessage("Erro ao calcular a distância, tarefa não adicionada.")
                }
            } catch (e: Exception) {
                showToastMessage("Erro ao adicionar tarefa: ${e.message}")
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            taskDao.update(task)
            _tasks.value = taskDao.getAll()
        }
    }

    private val _task = MutableStateFlow<Task?>(null)
    val task: StateFlow<Task?> = _task

    fun getAllTasks() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _tasks.value = if (selectedStatus.value == DeliveryStatus.PEDIDO_EM_TRANSITO) {
                taskDao.getAll()
            } else {
                taskDao.getTasksByStatus(selectedStatus.value)
            }
        }
    }
    fun deleteTask(task:Task){
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            taskDao.delete(task)
            taskDao.getAll()
        }
    }
    fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            taskDao.deleteAll()
            _tasks.value = taskDao.getAll()
        }
    }
    fun markAllAsDelivered() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val tasksToUpdate = taskDao.getAll().filter { it.deliveryStatus == DeliveryStatus.PEDIDO_EM_TRANSITO}
            tasksToUpdate.forEach {
                it.deliveryStatus = DeliveryStatus.PEDIDO_ENTREGUE
                taskDao.update(it)
            }
            _tasks.value = taskDao.getAll()
        }
    }
    fun makeRoute() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val tasksToUpdate = taskDao.getAll().filter { it.deliveryStatus == DeliveryStatus.PEDIDO_SEPARADO}
            tasksToUpdate.forEach {
                it.deliveryStatus = DeliveryStatus.PEDIDO_EM_TRANSITO
                taskDao.update(it)
            }
            _tasks.value = taskDao.getAll()
        }
    }

    private fun updateAllDistances() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _tasks.value = _tasks.value.map { task ->
                task.copy(distance = calculateDistanceSafe(task, userLocation.value))
            }.sortedBy {it.distance}
        }
    }

    private val selectedStatus = mutableStateOf(DeliveryStatus.PEDIDO_EM_TRANSITO)

    fun setSelectedStatus(status: DeliveryStatus) {
        selectedStatus.value = status
        getAllTasks()
    }

    fun getSelectedStatus(): DeliveryStatus {
        return selectedStatus.value
    }

    private suspend fun calculateDistanceSafe(task: Task, userLocation: Location): String {
        return if (task.distance != "N/A") {
            task.distance
        } else {
            try {
                withContext(Dispatchers.IO) {
                    calculateDistanceSemaphore.acquire()
                }
                val calculatedDistance = locationService.calculateDistanceAsync(userLocation, task.address, task.noteNumber).await().toString()

                if (calculatedDistance != "0") {
                    calculatedDistance
                } else {
                    showToastMessage("Erro ao calcular a distância, o endereço pode estar incorreto: $calculatedDistance")
                    "N/A"
                }
            } catch (e: Exception) {
                showToastMessage("Erro ao calcular a distância, o endereço pode estar incorreto: ${e.message}")
                "N/A"
            } finally {
                calculateDistanceSemaphore.release()
            }
        }
    }

}
