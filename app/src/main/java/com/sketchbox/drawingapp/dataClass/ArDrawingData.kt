package com.sketchbox.drawingapp.dataClass



import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity(
    tableName = "favorite_urls",
    indices = [Index(value = ["favouriteUrl"], unique = true)]
)

data class ArDrawingData(
    val favouriteUrl: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 0 signals Room to generate

)


