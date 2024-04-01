package com.example.weatherforecastapp.repo
import com.example.weatherforecastapp.local.WeatherLocalDataSource
import com.example.weatherforecastapp.model.CurrWeatherResponse
import com.example.weatherforecastapp.model.ForeCastWeatherResponse
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.WeatherAlert
import com.example.weatherforecastapp.network.RetrofitHelper
import com.example.weatherforecastapp.network.WeatherRemoteDataSource
import com.example.weatherforecastapp.network.WeatherService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull


class WeatherRepositoryImpl private constructor(
    private var weatherRemoteDataSource: WeatherRemoteDataSource,
    private var weatherLocalDataSource: WeatherLocalDataSource


):WeatherRepository {
    private val weatherService:WeatherService by lazy{
        RetrofitHelper.retrofitService
    }


        companion object{
            private var instance:WeatherRepositoryImpl?=null
            fun getInstance(weatherRemoteDataSource: WeatherRemoteDataSource,
                            weatherLocalDataSource: WeatherLocalDataSource)
                    :WeatherRepositoryImpl{
                return instance?: synchronized(this){
                    val temp=WeatherRepositoryImpl(weatherRemoteDataSource,weatherLocalDataSource)
                    instance=temp
                    temp
                }
            }

        }



    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double
    ): Flow<CurrWeatherResponse> {

        return weatherRemoteDataSource.getCurrentWeatherOverNetwork(latitude,longitude)
    }


    override suspend fun getForecastWeather(
        latitude: Double,
        longitude: Double,
        tempUnit: String,
        language: String
    ): Flow<ForeCastWeatherResponse> {

        return weatherRemoteDataSource.getForecastWeatherOverNetwork(latitude,longitude,tempUnit,language)
    }

    override suspend fun insertLocation(location: LocationData) {
        weatherLocalDataSource.insertLocation(location)

    }

    override suspend fun deleteLocation(location: LocationData) {
        weatherLocalDataSource.deleteLocation(location)
    }

    override suspend fun getStoredLocations(): Flow<List<LocationData>> {
      return weatherLocalDataSource.getStoredLocations()
    }

    override suspend fun getStoredWeatherAlerts(): Flow<List<WeatherAlert>> {
        return  weatherLocalDataSource.getStoredWeatherAlerts()
    }

    override suspend fun insertWeatherAlert(alert: WeatherAlert) {
       weatherLocalDataSource.insertWeatherAlert(alert)
    }

    override suspend fun deleteAlert(alert: WeatherAlert) {
       weatherLocalDataSource.deleteAlert(alert)
    }

    override suspend fun getAllStoredWeatherData(): Flow<ForeCastWeatherResponse> {
       return weatherLocalDataSource.getAllStoredWeatherData()
    }

    override suspend fun insertWeatherObject(weatherData: ForeCastWeatherResponse) {
        weatherLocalDataSource.insertWeatherObject(weatherData)
    }

    override suspend fun deleteWeatherObject() {
        weatherLocalDataSource.deleteWeatherObject()
    }

    override suspend fun getWeatherObjectStoredFromDb(): ForeCastWeatherResponse?{
        return getAllStoredWeatherData().firstOrNull()
    }


}

