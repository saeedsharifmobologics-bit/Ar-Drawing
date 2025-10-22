package com.sketchbox.drawingapp.dbUtils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sketchbox.drawingapp.dataClass.ArDrawingData

@Database(
    entities = [ArDrawingData::class],
    version = 1,
    exportSchema = false
)
abstract class ArDrawingDatabase : RoomDatabase() {
    abstract fun arDrawingDataDao(): ArDrawingDataDao
}
