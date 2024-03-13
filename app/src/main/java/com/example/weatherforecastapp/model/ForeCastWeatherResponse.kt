package com.example.weatherforecastapp.model

data class ForeCastWeatherResponse(
    val cod: Int,
    val message: String,
    val cnt: Int,
    val list: List<WeatherData>,
    val city: City
)