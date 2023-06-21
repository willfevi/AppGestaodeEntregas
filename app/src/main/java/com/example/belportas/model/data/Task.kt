package com.example.belportas.model.data

import java.io.Serializable

data class Task(
    val id: Long ,
    val noteNumber: String,
    val value: String,
    val address: String,
    val city :String,
    val deliveryStatus: String,
    val date: String,
    val clientName: String
): Serializable