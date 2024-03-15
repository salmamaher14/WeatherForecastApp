package com.example.weatherforecastapp.network

import com.example.weatherforecastapp.model.Constants
import com.example.weatherforecastapp.model.CurrWeatherResponse
import com.example.weatherforecastapp.model.ForeCastWeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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

    override suspend fun getCurrentWeatherOverNetwork(
        latitude: Double,
        longitude: Double,
    ): Flow<CurrWeatherResponse> {

        return flow { emit(weatherService.getCurrentWeather(latitude,longitude,Constants.API_KEY)) }

//        val response=weatherService.getCurrentWeather(latitude,longitude,apiKey)
//        return  response
    }

    override suspend fun getForecastWeatherOverNetwork(
        latitude: Double,
        longitude: Double,

    ): Flow<ForeCastWeatherResponse>{
        return  flow { emit(weatherService.getForecastWeather(latitude, longitude, Constants.Companion.API_KEY)) }
//        val response=weatherService.getForecastWeather(latitude,longitude,apiKey)
//        return  response
    }


}

