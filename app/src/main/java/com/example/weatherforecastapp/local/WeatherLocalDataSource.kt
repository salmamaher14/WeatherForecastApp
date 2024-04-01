package com.example.weatherforecastapp.local


import com.example.weatherforecastapp.model.ForeCastWeatherResponse
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    suspend fun insertLocation(location: LocationData)
    suspend fun  deleteLocation(location: LocationData)

    suspend fun getStoredLocations():Flow<List<LocationData>>

    suspend fun getStoredWeatherAlerts():Flow<List<WeatherAlert>>
    suspend fun deleteAlert(alert: WeatherAlert)

    suspend fun insertWeatherAlert(weatherAlert: WeatherAlert)
    suspend fun getAllStoredWeatherData(): Flow<ForeCastWeatherResponse>
    suspend fun insertWeatherObject(weatherData: ForeCastWeatherResponse)
    suspend fun deleteWeatherObject()






}
