package com.example.watertracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Dao
interface Dao   {
    @Insert
    fun insertItem(item: Item)
    @Delete
    suspend fun deleteItem(item: Item)
    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT SUM(volume) FROM items WHERE date = :date")
    fun getSumVolumeByDate(date:String):Flow<Float?>
    @Query("DELETE FROM items")
    suspend fun deleteAllItems()
}