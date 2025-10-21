package com.example.ardrawing.dbUtils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ardrawing.dataClass.ArDrawingData

@Database(
    entities = [ArDrawingData::class],
    version = 1,
    exportSchema = false
)
abstract class ArDrawingDatabase : RoomDatabase() {
    abstract fun arDrawingDataDao(): ArDrawingDataDao
}
