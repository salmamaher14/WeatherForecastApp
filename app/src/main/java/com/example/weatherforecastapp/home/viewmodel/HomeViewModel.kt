package com.example.weatherforecastapp.home.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.model.Constants.Companion.latitudeThreshold
import com.example.weatherforecastapp.model.Constants.Companion.longitudeThreshold
import com.example.weatherforecastapp.model.ForeCastWeatherResponse
import com.example.weatherforecastapp.repo.WeatherRepository
import com.example.weatherforecastapp.settings.viewmodel.SettingsViewModel
import com.example.weatherforecastapp.utilities.DataSource
import com.example.weatherforecastapp.utilities.ForecastWeatherState
import com.example.weatherforecastapp.utilities.LocaleManager
import com.example.weatherforecastapp.utilities.getCurrentDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.math.log


class HomeViewModel(private val _irepo: WeatherRepository,private val application: Application):ViewModel() {

    private val _weatherResponseState =

        MutableStateFlow<ForecastWeatherState>(ForecastWeatherState.Loading)
    val weatherResponseState: StateFlow<ForecastWeatherState> = _weatherResponseState


    private val _weatherResponse: MutableLiveData<ForeCastWeatherResponse> =
        MutableLiveData()  // i will use it to collect
    val weatherResponse: LiveData<ForeCastWeatherResponse> = _weatherResponse

    var isDataInsertion = false



     fun getRemoteForecastWeather(
        latitude: Double,
        longitude: Double,
        tempUnit: String,
        language: String
    ) {// when
        // coroutines
        Log.i("requestCoords", "getRemoteForecastWeather: $latitude, $longitude")

          isDataInsertion = false

         viewModelScope.launch (Dispatchers.IO){
             _irepo.getAllStoredWeatherData()

                 .collect { storedWeatherData ->
                     if(!isDataInsertion ){

                         if (storedWeatherData != null) {
                             Log.i("in dbCollect", "getRemoteForecastWeather: $storedWeatherData")



                             Log.i(
                                 "testcoords",
                                 "getRemoteForecastWeather: city.coord.lon: ${storedWeatherData.city.coord.lon}, " +
                                         "longitude: $longitude, " +
                                         "storedWeatherData.city.coord.lat: ${storedWeatherData.city.coord.lat}, " +
                                         "latitude: $latitude")

                             Log.i("testDate", storedWeatherData.list[0].dt_txt.split(" ")[0]+ " "+ getCurrentDate())



                             if (storedWeatherData.list[0].dt_txt.split(" ")[0] == getCurrentDate() &&
                                 Math.abs(storedWeatherData.city.coord.lon - longitude) < longitudeThreshold &&
                                 Math.abs(storedWeatherData.city.coord.lat - latitude) < latitudeThreshold
                             ) {
                                 Log.i("secondIfTrue", "getRemoteForecastWeather: ")
                                 _weatherResponse.value = storedWeatherData
                                 _weatherResponseState.value = ForecastWeatherState.Success(storedWeatherData)
                             } else  {

                                 Log.i("insidedeletion", "getRemoteForecastWeather: ")

                                 try {

                                     _irepo.deleteWeatherObject(storedWeatherData)
                                     fetchDataFromApi(latitude, longitude, tempUnit, language)
                                     // Deletion was successful
                                 } catch (e: Exception) {
                                     Log.e("DeletionError", "Error deleting weather data: $e")
                                     // Handle deletion failure
                                 }



                             }
                         }
                         else {
                             Log.i("list is null", "getRemoteForecastWeather: ")

                             fetchDataFromApi(latitude, longitude, tempUnit, language)
                         }

                     }

                 }
         }

     }

     fun fetchDataFromApi(
        latitude: Double,
        longitude: Double,
        tempUnit: String,
        language: String
    ) {


        viewModelScope.launch {(Dispatchers.IO)
            _irepo.getForecastWeather(latitude, longitude, tempUnit, language)

                .collect { weatherResponse ->
                    Log.i("insideapi", "fetchDataFromApi: "+weatherResponse.list.get(0).weather)
//                    try {
//
//
//                        isDataInsertion = true
//                        _irepo.insertWeatherObject(weatherResponse)
//                        isDataInsertion = false
//                    } catch (e: Exception) {
//                        Log.e("errorInserting", "Error inserting weather data: $e")
//                    }
                    Log.i("afterinsert", "fetchDataFromApi: ")
                    _weatherResponse.value=weatherResponse
                    _weatherResponseState.value = ForecastWeatherState.Success(weatherResponse)

                    Log.i("weatherItem", "getRemoteForecastWeather: " + weatherResponse.list.get(0))
                    Log.i("succeded", "getRemoteForecastWeather: " + weatherResponse.city.name)
                }
        }
    }

     fun getSettingConfiguration(
        settingsViewModel: SettingsViewModel,
        latitude: Double,
        langitude: Double,
         source: DataSource =DataSource.AUTO
        // setting ->
        //fav-> api
    ) {

        // Access saved settings
        val savedSettings = settingsViewModel.getSavedSettings()

        val language = if (savedSettings.selectedLanguage == "Arabic") "ar" else "en"


        Log.i("lang", "initializeHomeParameters: $language")

        val targetTemperature = when (savedSettings.selectedTemperatureUnit) {
            "Celsius" -> "metric"
            "Fahrenheit" -> "imperial"
            else -> "standard"
        }


        val targetWindSpeed =
            if (savedSettings.selectedWindSpeedUnit == "Mile/hour") "Imperial" else "Metric"
        Log.i("wind", "initializeHomeParameters: $targetWindSpeed")

        Log.i("coords", "startHome: " + latitude + " " + langitude)


//        fetchDataFromApi(
//            latitude,
//            langitude,
//            targetTemperature,
//            language
//
//        )
    }


    fun observeSetting(settingsViewModel: SettingsViewModel, latitude: Double, langitude: Double) {


        viewModelScope.launch{
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


                fetchDataFromApi(
                    latitude,
                    langitude,
                    targetTemperature,
                    targetWindSpeed

                )


            }
        }

    }
}







