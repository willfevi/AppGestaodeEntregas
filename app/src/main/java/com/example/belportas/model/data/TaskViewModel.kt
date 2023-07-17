package com.example.belportas.model.data

import android.app.Application
import android.location.Location
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
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

    private val _tasks = MutableLiveData<List<Task>>(emptyList())
    val tasks: LiveData<List<Task>> = _tasks

    init {
        locationService.startLocationUpdates {
            userLocation.value = it
            updateAllDistances()
        }
        getAllTasks()
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            taskDao.insert(task)
            _tasks.postValue(taskDao.getAll())
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            taskDao.delete(task)
            _tasks.postValue(taskDao.getAll())
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            taskDao.update(task)
            _tasks.postValue(taskDao.getAll())
        }
    }

    private fun getAllTasks() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _tasks.postValue(taskDao.getAll())
        }
    }

    fun updateAllDistances() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _tasks.value = _tasks.value?.map { task ->
                task.copy(distance = calculateDistanceSafe(task, userLocation.value))
            }?.sortedBy { it.distance }
        }
    }

    fun refreshDistancesForNull() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _tasks.value = _tasks.value?.map { task ->
                if (task.distance == "N/A") {
                    task.copy(distance = calculateDistanceSafe(task, userLocation.value))
                } else {
                    task
                }
            }
        }
    }

    private suspend fun calculateDistanceSafe(task: Task, userLocation: Location): String {
        return try {
            calculateDistanceSemaphore.acquire()
            locationService.calculateDistanceAsync(userLocation, task.address).await().toString()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error calculating distance: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            "N/A"
        } finally {
            calculateDistanceSemaphore.release()
        }
    }
}
