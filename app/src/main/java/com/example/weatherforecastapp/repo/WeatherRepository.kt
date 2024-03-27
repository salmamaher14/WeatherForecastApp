package com.example.weatherforecastapp.repo

import com.example.weatherforecastapp.model.CurrWeatherResponse
import com.example.weatherforecastapp.model.ForeCastWeatherResponse
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude:Double,longitude:Double): Flow<CurrWeatherResponse>
    suspend fun getForecastWeather(latitude:Double,longitude:Double,tempUnit:String,language:String):Flow<ForeCastWeatherResponse>

    suspend fun insertLocation(location: LocationData)
    suspend fun  deleteLocation(location: LocationData)
    suspend fun getStoredLocations(): Flow<List<LocationData>>

    suspend fun getStoredWeatherAlerts(): Flow<List<WeatherAlert>>

    suspend fun insertWeatherAlert(alert:WeatherAlert)

    suspend fun getAllStoredWeatherData(): Flow<ForeCastWeatherResponse>
    suspend fun insertWeatherObject(weatherData: ForeCastWeatherResponse)
    suspend fun deleteWeatherObject(weatherData: ForeCastWeatherResponse)


}

