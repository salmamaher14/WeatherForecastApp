package com.example.weatherforecastapp.favlocations.viewmodel

import com.example.weatherforecastapp.model.CurrWeatherResponse
import com.example.weatherforecastapp.model.ForeCastWeatherResponse
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.WeatherAlert
import com.example.weatherforecastapp.repo.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class WeatherFakeRepository:WeatherRepository {

    private val storedLocations = mutableListOf<LocationData>()


    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double
    ): Flow<CurrWeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getForecastWeather(
        latitude: Double,
        longitude: Double,
        tempUnit: String,
        language: String
    ): Flow<ForeCastWeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun insertLocation(location: LocationData) {
        storedLocations.add(location)

    }

    override suspend fun deleteLocation(location: LocationData) {
        storedLocations.remove(location)
    }

    override suspend fun getStoredLocations(): Flow<List<LocationData>> {
        return flowOf(storedLocations)
    }

    override suspend fun getStoredWeatherAlerts(): Flow<List<WeatherAlert>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertWeatherAlert(alert: WeatherAlert) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alert: WeatherAlert) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllStoredWeatherData(): Flow<ForeCastWeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun insertWeatherObject(weatherData: ForeCastWeatherResponse) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWeatherObject() {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherObjectStoredFromDb(): ForeCastWeatherResponse? {
        TODO("Not yet implemented")
    }
}

// 5 method

