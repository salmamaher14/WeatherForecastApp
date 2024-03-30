package com.example.weatherforecastapp.db
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.model.LocationData

import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao{

    @Query("SELECT * FROM fav_locations")
     fun getAllStoredLocations(): Flow<List<LocationData>>

    @Insert (onConflict= OnConflictStrategy.IGNORE)
    suspend fun insertLocation(location: LocationData): Long
    @Delete
    suspend fun deleteLocation(location: LocationData):Int


}