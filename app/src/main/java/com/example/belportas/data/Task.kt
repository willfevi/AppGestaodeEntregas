package com.example.belportas.data

import android.app.Application
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable
import java.lang.ref.WeakReference
import java.util.Date
import java.util.concurrent.Semaphore

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
    var date: Date?,
    val clientName: String,
    var imagePath: String? = null,
    var observation : String?= null
): Serializable
enum class DeliveryStatus {
    PEDIDO_SEPARADO,
    PEDIDO_EM_TRANSITO,
    PEDIDO_ENTREGUE,
    PEDIDO_EXCLUIDO
}
