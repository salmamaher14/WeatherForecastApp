package com.example.weatherforecastapp.network
import com.example.weatherforecastapp.model.CurrWeatherResponse
import com.example.weatherforecastapp.model.ForeCastWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String

    ):CurrWeatherResponse

    @GET("forecast")
    suspend fun getForecastWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") temperatureUnits:String,
        @Query("lang")  language:String

    ):ForeCastWeatherResponse
}

/*
https://api.openweathermap.org/data/2.5/forecast?lat=57&lon=-2.15&appid=3bc0ed335a4d22f215fe489f89eb2c98
&units=metric&lang=ar
 */