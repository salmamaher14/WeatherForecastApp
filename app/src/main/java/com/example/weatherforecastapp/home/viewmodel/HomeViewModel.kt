package com.example.weatherforecastapp.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.model.ForeCastWeatherResponse
import com.example.weatherforecastapp.repo.WeatherRepository
import com.example.weatherforecastapp.settings.viewmodel.SettingsViewModel
import com.example.weatherforecastapp.utilities.ForecastWeatherState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class HomeViewModel(private val _irepo: WeatherRepository):ViewModel() {

    private val _weatherResponseState =
        MutableStateFlow<ForecastWeatherState>(ForecastWeatherState.Loading)
    val weatherResponseState: StateFlow<ForecastWeatherState> = _weatherResponseState


    private val _weatherResponse: MutableLiveData<ForeCastWeatherResponse> =
        MutableLiveData()  // i will use it to collect
    val weatherResponse: LiveData<ForeCastWeatherResponse> = _weatherResponse


    fun getRemoteForecastWeather(
        latitude: Double,
        longitude: Double,
        tempUnit: String,
        windSpeedUnit: String,
        language: String
    ) {
        // coroutines
        Log.i("requestCoords", "getRemoteForecastWeather: " + latitude + latitude)
        viewModelScope.launch(Dispatchers.Main) {

            _irepo.getForecastWeather(latitude, longitude, tempUnit, language)
                .catch { e ->
                    _weatherResponseState.value = ForecastWeatherState.Failure(e)
                    Log.i("getRemoteForecastWeather", "getRemoteForecastWeather: " + e.message)


                }
                .collect { weatherResponse ->
                    _weatherResponse.value = weatherResponse
                    _weatherResponseState.value = ForecastWeatherState.Success(weatherResponse)

                    Log.i("weatherItem", "getRemoteForecastWeather: " + weatherResponse.list.get(0))

                    Log.i("succeded", "getRemoteForecastWeather: " + weatherResponse.city.name)
                }
        }
    }


    fun getSettingConfiguration(
        settingsViewModel: SettingsViewModel,
        latitude: Double,
        langitude: Double
    ) {
        // Access saved settings
        val savedSettings = settingsViewModel.getSavedSettings()

        // Determine language based on selectedLanguage
        val language = if (savedSettings.selectedLanguage == "Arabic") "ar" else "en"
        Log.i("lang", "initializeHomeParameters: $language")

        // Determine temperature unit based on selectedTemperatureUnit
        val targetTemperature = when (savedSettings.selectedTemperatureUnit) {
            "Celsius" -> "metric"
            "Fahrenheit" -> "imperial"
            else -> "standard"
        }


        val targetWindSpeed =
            if (savedSettings.selectedWindSpeedUnit == "Mile/hour") "Imperial" else "Metric"
        Log.i("wind", "initializeHomeParameters: $targetWindSpeed")

        Log.i("coords", "startHome: " + latitude + " " + langitude)


        // Fetch weather data using determined parameters
        getRemoteForecastWeather(
            latitude,
            langitude,
            targetTemperature,
            targetWindSpeed,
            language

        )
    }


    fun observeSetting(settingsViewModel: SettingsViewModel, latitude: Double, langitude: Double) {


        viewModelScope.launch {
            Log.i("inside", "getSettingConfigeration: ")
            settingsViewModel.settings.collect { setting ->

                Log.i("getsetting", "getSettingConfigeration: " + setting.selectedLanguage)
                val targetLanguage = if (setting.selectedLanguage == "Arabic") "ar" else "en"
                Log.i("targetLanguage", "getSettingConfigeration: " + targetLanguage)

                val targetTemperature = when (setting.selectedTemperatureUnit) {
                    "Celsius" -> "metric"
                    "Fahrenheit" -> "imperial"
                    else -> "standard"
                }

                val targetWindSpeed =
                    if (setting.selectedWindSpeedUnit == "Mile/hour") "Imperial" else "Metric"


                getRemoteForecastWeather(
                    latitude,
                    langitude,
                    targetTemperature,
                    targetLanguage,
                    targetWindSpeed

                )


            }
        }

    }
}







