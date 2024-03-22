package com.example.weatherforecastapp.model

import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude:Double,longitude:Double): Flow<CurrWeatherResponse>
    suspend fun getForecastWeather(latitude:Double,longitude:Double,tempUnit:String,language:String):Flow<ForeCastWeatherResponse>

    suspend fun insertLocation(location: LocationData)
    suspend fun  deleteLocation(location: LocationData)
    suspend fun getStoredLocations(): Flow<List<LocationData>>

}

