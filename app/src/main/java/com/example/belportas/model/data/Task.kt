package com.example.belportas.model.data

import java.io.Serializable
import java.util.Date

data class Task(
    val id: Long,
    val noteNumber: String,
    val phoneClient:String,
    val value: String,
    val address: String,
    var distance :String,
    val deliveryStatus: String,
    val date: Date?,
    val clientName: String
): Serializable