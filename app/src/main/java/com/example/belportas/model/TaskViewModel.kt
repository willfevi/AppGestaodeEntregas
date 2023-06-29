package com.example.belportas.model

import android.app.Application
import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.belportas.model.data.LocationService
import com.example.belportas.model.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Semaphore
import android.widget.Toast
import kotlinx.coroutines.CoroutineExceptionHandler

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val locationService = LocationService(application)
    val userLocation: MutableState<Location> = mutableStateOf(Location(""))

    private val _tasks = MutableLiveData<MutableList<Task>>(mutableListOf())
    val tasks: LiveData<MutableList<Task>> get() = _tasks

    private val calculateDistanceSemaphore = Semaphore(1)

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Toast.makeText(context, "Erro: ${exception.message}", Toast.LENGTH_SHORT).show()
    }

    init {
        locationService.startLocationUpdates {
            userLocation.value = it
            updateAllDistances()
        }
        _tasks.value = _tasks.value ?: mutableListOf()
    }

    fun addTask(task: Task) {
        _tasks.value?.apply {
            add(task)
            _tasks.value = this
        }
    }

    fun deleteTask(task: Task) {
        _tasks.value?.apply {
            remove(task)
            _tasks.value = this
        }
    }

    fun updateAllDistances() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _tasks.value = _tasks.value?.map { task ->
                task.copy(distance = calculateDistanceSafe(task, userLocation.value))
            }?.toMutableList()
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
            }?.toMutableList()
        }
    }

    private suspend fun calculateDistanceSafe(task: Task, userLocation: Location): String {
        return try {
            calculateDistanceSemaphore.acquire()
            locationService.calculateDistanceAsync(userLocation, task.address).await().toString()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Erro ao calcular distância: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            "N/A"
        } finally {
            calculateDistanceSemaphore.release()
        }
    }
}
