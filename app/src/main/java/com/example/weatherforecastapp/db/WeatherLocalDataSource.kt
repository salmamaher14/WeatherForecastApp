package com.example.weatherforecastapp


import com.example.weatherforecastapp.model.LocationData
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    suspend fun insertLocation(location: LocationData)
    suspend fun  deleteLocation(location: LocationData)

    suspend fun getStoredLocations():Flow<List<LocationData>>
}