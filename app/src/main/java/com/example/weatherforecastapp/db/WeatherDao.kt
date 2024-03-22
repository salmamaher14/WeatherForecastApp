package com.example.weatherforecastapp
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.model.LocationData

import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao{

    @Query("SELECT * FROM favLocations_database")
     fun getAllStoredLocations(): Flow<List<LocationData>>

    @Insert (onConflict= OnConflictStrategy.IGNORE)
    suspend fun insert(location: LocationData): Long
    @Delete
    suspend fun delete(location: LocationData):Int


}