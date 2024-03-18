package com.example.weatherforecastapp.model

import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude:Double,longitude:Double): Flow<CurrWeatherResponse>
    suspend fun getForecastWeather(latitude:Double,longitude:Double,tempUnit:String,language:String):Flow<ForeCastWeatherResponse>

}

