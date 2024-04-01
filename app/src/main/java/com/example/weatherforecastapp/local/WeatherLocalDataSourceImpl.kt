package com.example.weatherforecastapp.local

import android.content.Context
import com.example.weatherforecastapp.db.WeatherDataBase
import com.example.weatherforecastapp.db.LocationDao
import com.example.weatherforecastapp.db.WeatherAlertDao
import com.example.weatherforecastapp.db.WeatherDataDao
import com.example.weatherforecastapp.model.ForeCastWeatherResponse
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl(context: Context): WeatherLocalDataSource {

    private val locationDao: LocationDao by lazy {
        val db : WeatherDataBase = WeatherDataBase.getInstance(context)
        db.getLocationDao()
    }

    private val alertDao:WeatherAlertDao by lazy {
        val db : WeatherDataBase = WeatherDataBase.getInstance(context)
        db.getWeatherAlertDao()
    }


    private val weatherDataDao:WeatherDataDao by lazy {
        val db : WeatherDataBase = WeatherDataBase.getInstance(context)
        db.getWeatherDataDao()
    }

    override suspend fun insertLocation(location: LocationData) {
        locationDao.insertLocation(location)

    }

    override suspend fun deleteLocation(location: LocationData) {
        locationDao.deleteLocation(location)

    }



    override suspend fun getStoredLocations(): Flow<List<LocationData>> {
        return locationDao.getAllStoredLocations()
    }

    override suspend fun getStoredWeatherAlerts(): Flow<List<WeatherAlert>> {
       return alertDao.getAllWeatherAlerts()

    }

    override suspend fun deleteAlert(alert: WeatherAlert) {
        alertDao.deleteAlert(alert)
    }

    override suspend fun insertWeatherAlert(weatherAlert: WeatherAlert) {
        alertDao.insertWeatherAlert(weatherAlert)
    }

    override suspend fun getAllStoredWeatherData(): Flow<ForeCastWeatherResponse> {
        return weatherDataDao.getAllStoredWeatherData()
    }

    override suspend fun insertWeatherObject(weatherData: ForeCastWeatherResponse) {
        weatherDataDao.insertWeatherObject(weatherData)
    }

    override suspend fun deleteWeatherObject() {
        weatherDataDao.deleteAllWeatherData()
    }



}