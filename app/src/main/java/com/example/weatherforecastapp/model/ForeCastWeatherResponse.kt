package com.example.weatherforecastapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast_weather_response")
data class ForeCastWeatherResponse(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cod: Int,
    val message: String,
    val cnt: Int,
    val list: List<WeatherData>,
    val city: City
)