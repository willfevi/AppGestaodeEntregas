package com.example.belportas.model

import androidx.room.TypeConverter
import com.example.belportas.data.DeliveryStatus

class DeliveryStatusConverters {
    @TypeConverter
    fun fromDeliveryStatus(status: DeliveryStatus): String {
        return status.name
    }

    @TypeConverter
    fun toDeliveryStatus(status: String): DeliveryStatus {
        return DeliveryStatus.valueOf(status)
    }
}
