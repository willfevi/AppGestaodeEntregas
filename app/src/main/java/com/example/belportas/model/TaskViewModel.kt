package com.example.belportas.model
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.belportas.model.data.Task

class TaskViewModel : ViewModel() {
    private val _tasks = MutableLiveData<MutableList<Task>>(mutableListOf())
    val tasks: LiveData<MutableList<Task>> = _tasks

    fun addTask(task: Task) {
        val currentTasks = _tasks.value ?: mutableListOf()
        currentTasks.add(task)
        _tasks.value = currentTasks
    }
}
