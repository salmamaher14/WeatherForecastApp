package com.example.weatherforecastapp.network

import com.example.weatherforecastapp.model.CurrWeatherResponse
import com.example.weatherforecastapp.model.ForeCastWeatherResponse


interface WeatherRemoteDataSource {
    suspend fun getWeatherOverNetwork(latitude:Double,longitude:Double,apiKey:String):CurrWeatherResponse
    suspend fun getForecastWeatherOverNetwork(latitude:Double,longitude:Double,apiKey:String):ForeCastWeatherResponse
}