package com.example.weatherforecastapp.utilities

import com.example.weatherforecastapp.model.ForeCastWeatherResponse


sealed class ForecastWeatherState {
    class Success(val data:ForeCastWeatherResponse):ForecastWeatherState()
    class Failure(val msg:Throwable):ForecastWeatherState()
    object Loading:ForecastWeatherState()
}