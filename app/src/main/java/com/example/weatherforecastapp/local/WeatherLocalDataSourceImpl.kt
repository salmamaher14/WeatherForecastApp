package com.example.weatherforecastapp.local

import android.content.Context
import com.example.weatherforecastapp.WeatherDataBase
import com.example.weatherforecastapp.db.LocationDao
import com.example.weatherforecastapp.db.WeatherAlertDao
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl(context: Context): WeatherLocalDataSource {

    private val locationDao: LocationDao by lazy {
        val db : WeatherDataBase =WeatherDataBase.getInstance(context)
        db.getLocationDao()
    }

    private val alertDao:WeatherAlertDao by lazy {
        val db :WeatherDataBase=WeatherDataBase.getInstance(context)
        db.getWeatherAlertDao()
    }


    override suspend fun insertLocation(location: LocationData) {
        locationDao.insertLocation(location)

    }

    override suspend fun deleteLocation(location: LocationData) {
        locationDao.delete(location)

    }



    override suspend fun getStoredLocations(): Flow<List<LocationData>> {
        return locationDao.getAllStoredLocations()
    }

    override suspend fun getStoredWeatherAlerts(): Flow<List<WeatherAlert>> {
       return alertDao.getAllWeatherAlerts()

    }

    override suspend fun insertWeatherAlert(weatherAlert: WeatherAlert) {
        alertDao.insertWeatherAlert(weatherAlert)
    }


}