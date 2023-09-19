package com.example.belportas.model.functionstasklistscreen

import com.example.belportas.data.Task

fun compareByDistanceThenName(task1: Task, task2: Task): Int {
    val distance1 = task1.distance.split("km")[0].toIntOrNull() ?: 0
    val distance2 = task2.distance.split("km")[0].toIntOrNull() ?: 0
    val distanceComparison = distance1.compareTo(distance2)

    return if (distanceComparison == 0) {
        task1.clientName.compareTo(task2.clientName)
    } else {
        distanceComparison
    }
}