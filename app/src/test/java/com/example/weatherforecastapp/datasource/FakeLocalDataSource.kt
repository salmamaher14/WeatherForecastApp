package com.example.weatherforecastapp.datasource

import com.example.weatherforecastapp.local.WeatherLocalDataSource
import com.example.weatherforecastapp.model.ForeCastWeatherResponse
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.WeatherAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource : WeatherLocalDataSource {
    // Fake data storage
    val storedLocations = mutableListOf<LocationData>()
    val storedWeatherAlerts = mutableListOf<WeatherAlert>()

    override suspend fun insertLocation(location: LocationData) {
        // Insert the location into the list
        storedLocations.add(location)
    }

    override suspend fun deleteLocation(location: LocationData) {
        // Remove the location from the list
        storedLocations.remove(location)
    }

    override suspend fun getStoredLocations(): Flow<List<LocationData>> {
        // Return a flow emitting the list of stored locations
        return flow {
            emit(storedLocations)
        }
    }

    override suspend fun getStoredWeatherAlerts(): Flow<List<WeatherAlert>> {
        // Return a flow emitting the list of stored weather alerts
        return flow {
            emit(storedWeatherAlerts)
        }
    }

    override suspend fun deleteAlert(alert: WeatherAlert) {
        // Remove the alert from the list
        storedWeatherAlerts.remove(alert)
    }

    override suspend fun insertWeatherAlert(weatherAlert: WeatherAlert) {
        // Insert the weather alert into the list
        storedWeatherAlerts.add(weatherAlert)
    }

    override suspend fun getAllStoredWeatherData(): Flow<ForeCastWeatherResponse> {
        TODO("Not yet implemented")
    }


    override suspend fun insertWeatherObject(weatherData: ForeCastWeatherResponse) {
        // Implementation for inserting weather data if needed in your tests
    }

    override suspend fun deleteWeatherObject() {
        // Implementation for deleting weather data if needed in your tests
    }
}
