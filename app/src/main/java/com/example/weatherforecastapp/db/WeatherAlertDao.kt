package com.example.weatherforecastapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherAlertDao {
    @Query("SELECT * FROM weather_alerts")
    fun getAllWeatherAlerts(): Flow<List<WeatherAlert>>

    @Insert(onConflict= OnConflictStrategy.IGNORE)
    suspend fun insertWeatherAlert(weatherAlert: WeatherAlert)

    @Delete
    suspend fun deleteAlert(alert: WeatherAlert)
}
