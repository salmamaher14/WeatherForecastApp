package com.example.weatherforecastapp.network
import com.example.weatherforecastapp.model.CurrWeatherResponse
import com.example.weatherforecastapp.model.ForeCastWeatherResponse
import com.example.weatherforecastapp.model.WeatherData
import kotlinx.coroutines.flow.Flow


interface WeatherRemoteDataSource {
    suspend fun getCurrentWeatherOverNetwork(latitude:Double, longitude:Double): Flow<CurrWeatherResponse>
    suspend fun getForecastWeatherOverNetwork(latitude:Double,longitude:Double,tempUnit:String,language:String):Flow<ForeCastWeatherResponse>
}