package com.example.belportas.model

import android.app.Application
import android.location.Location
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.belportas.data.AppDatabase
import com.example.belportas.data.LocationService
import com.example.belportas.data.Task
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Semaphore

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val locationService = LocationService(application)
    val userLocation = mutableStateOf(Location(""))
    private val calculateDistanceSemaphore = Semaphore(1)

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
    }
    private val taskDao = AppDatabase.getDatabase(application).taskDao()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        locationService.startLocationUpdates {
            userLocation.value = it
            updateAllDistances()
        }
        getAllTasks()
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val distance = locationService.calculateDistanceAsync(userLocation.value, task.address).await().toString()
            val taskWithDistance = task.copy(distance = distance)
            taskDao.insert(taskWithDistance)
            _tasks.value = taskDao.getAll()
        }
    }
    fun addTaskExternal(task: Task) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val currentLocationDeferred = locationService.getCurrentLocationAsync()
                currentLocationDeferred.await()?.let { currentLocation ->
                    userLocation.value = currentLocation
                    if(task.address.isNullOrEmpty()) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "O endereço da tarefa não está definido.", Toast.LENGTH_SHORT).show()
                        }
                        return@launch
                    }

                    val distance = locationService.calculateDistanceAsync(userLocation.value, task.address).await().toString()

                    val taskWithDistance = task.copy(distance = distance)
                    taskDao.insert(taskWithDistance)
                    _tasks.value = taskDao.getAll()
                } ?: run {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Não foi possível obter a localização atual do usuário.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Erro ao adicionar tarefa: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }




    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            taskDao.delete(task)
            _tasks.value = taskDao.getAll()
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

    fun getTaskById(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val result = taskDao.getTaskById(taskId)
            withContext(Dispatchers.Main) {
                _task.value = result
            }
        }
    }

    fun getAllTasks() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _tasks.value = taskDao.getAll()
        }
    }
    fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            taskDao.deleteAll()
            _tasks.value = taskDao.getAll()
        }
    }

    fun updateAllDistances() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _tasks.value = _tasks.value.map { task ->
                task.copy(distance = calculateDistanceSafe(task, userLocation.value))
            }.sortedBy {it.distance}
        }
    }
    fun refreshDistancesForNull() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _isRefreshing.value = true
            _tasks.value = _tasks.value.map { task ->
                if (task.distance == "0" || task.distance == "N/A") {
                    task.copy(distance = calculateDistanceSafe(task, userLocation.value))
                } else {
                    task
                }
            }
            _isRefreshing.value = false
        }
    }

    private suspend fun calculateDistanceSafe(task: Task, userLocation: Location): String {
        return if (task.distance != "N/A") {
            task.distance
        } else {
            try {
                calculateDistanceSemaphore.acquire()
                locationService.calculateDistanceAsync(userLocation, task.address).await().toString()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Erro ao calcular a distância, o endereço pode estar incorreto: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                "N/A"
            } finally {
                calculateDistanceSemaphore.release()
            }
        }
    }
}