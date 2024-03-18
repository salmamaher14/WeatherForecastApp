package com.example.weatherforecastapp.home.viewmodel

import SettingsViewModel
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.model.WeatherRepository
import com.example.weatherforecastapp.settings.viewmodel.SettingsViewModelFactory
import com.example.weatherforecastapp.utilities.ForecastWeatherState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Language
import kotlin.math.log


class HomeViewModel(private val _irepo:WeatherRepository):ViewModel() {
    private val _weatherResponseState= MutableStateFlow<ForecastWeatherState>(ForecastWeatherState.Loading)
    val weatherResponseState: StateFlow<ForecastWeatherState> = _weatherResponseState




    fun getRemoteForecastWeather(latitude:Double,longitude:Double,tempUnit:String,windSpeedUnit:String,language: String){
        // coroutines
        viewModelScope.launch(Dispatchers.IO){

            _irepo.getForecastWeather(latitude,longitude,tempUnit,language)
                .catch { e->
                    _weatherResponseState.value= ForecastWeatherState.Failure(e)
                    Log.i("getRemoteForecastWeather", "getRemoteForecastWeather: "+e.message)


                }
                .collect{
                    weatherResponse->_weatherResponseState.value=ForecastWeatherState.Success(weatherResponse)
                    Log.i("succeded", "getRemoteForecastWeather: "+weatherResponse.city)
                }
        }
    }


    fun startHome(settingsViewModel: SettingsViewModel) {
        // Access saved settings
        val savedSettings = settingsViewModel.getSavedSettings()

        // Determine language based on selectedLanguage
        val lang = if (savedSettings.selectedLanguage == "Arabic") "ar" else "en"
        Log.i("lang", "initializeHomeParameters: $lang")

        // Determine temperature unit based on selectedTemperatureUnit
        val targetTemperature = when (savedSettings.selectedTemperatureUnit) {
            "Celsius" -> "metric"
            "Fahrenheit" -> "imperial"
            else -> "standard"
        }
        Log.i("temp", "initializeHomeParameters: $targetTemperature")

        Log.i("saved wind", "startHome: "+savedSettings.selectedWindSpeedUnit)

        val targetWindSpeed = if (savedSettings.selectedWindSpeedUnit == "Mile/hour") "Imperial" else "Metric"
        Log.i("wind", "initializeHomeParameters: $targetWindSpeed")


        // Fetch weather data using determined parameters
        getRemoteForecastWeather(
            latitude = 57.0,
            longitude = -2.15,
            tempUnit = targetTemperature,
            targetWindSpeed,
            lang

        )
    }


    fun observeSetting(settingsViewModel: SettingsViewModel) {


        viewModelScope.launch {
            Log.i("inside", "getSettingConfigeration: ")
            settingsViewModel.settings.collect { setting ->

                Log.i("getsetting", "getSettingConfigeration: "+setting.selectedLanguage)
                val targetLanguage = if (setting.selectedLanguage == "Arabic") "ar" else "en"
                Log.i("targetLanguage", "getSettingConfigeration: "+targetLanguage)

                val targetTemperature = when (setting.selectedTemperatureUnit) {
                    "Celsius" -> "metric"
                    "Fahrenheit" -> "imperial"
                    else -> "standard"
                }

                val targetWindSpeed = if (setting.selectedWindSpeedUnit == "Mile/hour") "Imperial" else "Metric"



                getRemoteForecastWeather(
                    latitude = 57.0,
                    longitude = -2.15,
                    targetTemperature,
                    targetLanguage,
                    targetWindSpeed

                )
            }
        }


    }





}

