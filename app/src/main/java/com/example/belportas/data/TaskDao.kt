package com.example.belportas.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Query("SELECT * FROM task")
    fun getAll(): List<Task>
    @Query("SELECT * FROM task WHERE id = :taskId")
    fun getTaskById(taskId: Int): Task

    @Delete
    fun delete(task: Task)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(task: Task)

    @Query("DELETE FROM TASK")
    fun deleteAll()

    @Query("SELECT * FROM task WHERE noteNumber = :noteNumber LIMIT 1")
    fun getTaskByNoteNumber(noteNumber: String): Task?

}