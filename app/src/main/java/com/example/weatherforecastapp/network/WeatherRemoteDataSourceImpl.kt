package com.example.weatherforecastapp.network

import com.example.weatherforecastapp.model.CurrWeatherResponse
import com.example.weatherforecastapp.model.ForeCastWeatherResponse

class WeatherRemoteDataSourceImpl:WeatherRemoteDataSource{
    private val weatherService:WeatherService by lazy {
        RetrofitHelper.retrofitService
    }
    companion object{
        private var instance:WeatherRemoteDataSourceImpl?=null
        fun getInstance():WeatherRemoteDataSourceImpl{
            return instance?: synchronized(this){
                val temp=WeatherRemoteDataSourceImpl()
                instance=temp
                temp

            }
        }

    }

    override suspend fun getWeatherOverNetwork(
        latitude: Double,
        longitude: Double,
        apiKey: String
    ): CurrWeatherResponse {
        val response=weatherService.getCurrentWeather(latitude,longitude,apiKey)
        return  response
    }

    override suspend fun getForecastWeatherOverNetwork(
        latitude: Double,
        longitude: Double,
        apiKey: String
    ): ForeCastWeatherResponse {
        val response=weatherService.getForecastWeather(latitude,longitude,apiKey)
        return  response
    }


}

