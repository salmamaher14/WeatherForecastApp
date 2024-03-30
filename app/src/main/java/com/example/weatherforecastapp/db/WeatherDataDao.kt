package com.example.weatherforecastapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.model.ForeCastWeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDataDao {

    @Query("SELECT * FROM forecast_weather_response")
    fun getAllStoredWeatherData(): Flow<ForeCastWeatherResponse>

    @Insert(onConflict = OnConflictStrategy.IGNORE)

    suspend fun insertWeatherObject(weatherData: ForeCastWeatherResponse)

    @Query("DELETE FROM forecast_weather_response")
    suspend fun deleteAllWeatherData()
}



