package com.example.weatherforecastapp.model





data class WeatherData(


    val dt: Long,
    val main: MainInfo,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val rain: Rain?,
    val sys: Sys,
    val dt_txt: String
)

data class MainInfo(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val sea_level: Int,
    val grnd_level: Int,
    val humidity: Int,
    val temp_kf: Double
)

data class Weather(

    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Clouds(
    val all: Int
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)

data class Rain(
    val `3h`: Double
)

data class Sys(
    val pod: String
)

data class City(

    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)

data class Coord(
    val lat: Double,
    val lon: Double
)