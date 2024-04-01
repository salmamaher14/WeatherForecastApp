package com.example.weatherforecastapp.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_alerts")
data class WeatherAlert(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String
)