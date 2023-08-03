package com.example.belportas.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long=0,
    val noteNumber: String,
    val phoneClient:String,
    val value: String,
    val address: String,
    val cep: String,
    var distance :String,
    var deliveryStatus: DeliveryStatus,
    val date: Date?,
    val clientName: String
): Serializable
enum class DeliveryStatus {
    PEDIDO_SEPARADO,
    PEDIDO_EM_TRANSITO,
    PEDIDO_ENTREGUE
}