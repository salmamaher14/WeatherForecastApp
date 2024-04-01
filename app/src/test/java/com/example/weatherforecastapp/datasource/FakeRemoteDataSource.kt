package com.example.weatherforecastapp.datasource

import com.example.weatherforecastapp.model.CurrWeatherResponse
import com.example.weatherforecastapp.model.ForeCastWeatherResponse
import com.example.weatherforecastapp.network.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class FakeRemoteDataSource:WeatherRemoteDataSource {
    override suspend fun getCurrentWeatherOverNetwork(
        latitude: Double,
        longitude: Double
    ): Flow<CurrWeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getForecastWeatherOverNetwork(
        latitude: Double,
        longitude: Double,
        tempUnit: String,
        language: String
    ): Flow<ForeCastWeatherResponse> {
        TODO("Not yet implemented")
    }
}