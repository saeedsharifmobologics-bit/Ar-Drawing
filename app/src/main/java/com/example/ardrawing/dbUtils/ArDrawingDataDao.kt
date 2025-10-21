package com.example.ardrawing.dbUtils

import androidx.room.*
import com.example.ardrawing.dataClass.ArDrawingData
import kotlinx.coroutines.flow.Flow

@Dao
interface ArDrawingDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(data: ArDrawingData)

    @Query("DELETE FROM favorite_urls WHERE favouriteUrl = :url")
    suspend fun removeFavorite(url: String): Int

    @Query("SELECT * FROM favorite_urls")
    fun getAllFavorites(): Flow<List<ArDrawingData>>


    @Query("SELECT * FROM favorite_urls WHERE favouriteUrl = :url LIMIT 1")
    suspend fun getFavoriteByUrl(url: String): ArDrawingData?
}
