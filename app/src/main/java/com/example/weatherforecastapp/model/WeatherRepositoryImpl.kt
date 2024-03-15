package com.example.weatherforecastapp.model
import com.example.weatherforecastapp.network.RetrofitHelper
import com.example.weatherforecastapp.network.WeatherRemoteDataSource
import com.example.weatherforecastapp.network.WeatherRemoteDataSourceImpl
import com.example.weatherforecastapp.network.WeatherService
import kotlinx.coroutines.flow.Flow


class WeatherRepositoryImpl private constructor(
    private var weatherRemoteDataSource: WeatherRemoteDataSource

):WeatherRepository {
    private val weatherService:WeatherService by lazy{
        RetrofitHelper.retrofitService
    }

    companion object {
        private var instance:WeatherRemoteDataSourceImpl?=null

        fun getInstance():WeatherRemoteDataSourceImpl{
            return instance ?: synchronized(this) {
                // Create an instance of ProductsRemoteDataSourceImpl
                val temp = WeatherRemoteDataSourceImpl()
                instance = temp
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
        longitude: Double
    ): Flow<ForeCastWeatherResponse> {

        return weatherRemoteDataSource.getForecastWeatherOverNetwork(latitude,longitude)
    }
}

