package com.example.weatherforecastapp.model

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude:Double,longitude:Double,apiKey:String):CurrWeatherResponse
    suspend fun getForecastWeather(latitude:Double,longitude:Double,apiKey:String):ForeCastWeatherResponse

}

