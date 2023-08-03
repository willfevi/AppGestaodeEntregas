package com.example.belportas.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.belportas.model.DateConverters
import com.example.belportas.model.DeliveryStatusConverters

@Database(entities = [Task::class], version = 1)
@TypeConverters(DateConverters::class,DeliveryStatusConverters::class)
abstract class BelDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: BelDatabase? = null

        fun getDatabase(context: Context): BelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BelDatabase::class.java,
                    "app_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
