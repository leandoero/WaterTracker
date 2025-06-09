package com.example.watertracker.data

import android.R
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null,
    @ColumnInfo("volume")
    var volume: Float,
    @ColumnInfo("time")
    var time:String,
    @ColumnInfo("date")
    var date:String
){
    override fun toString(): String{
    return "$volume ml — $time"}
}


