package com.example.weatherforecastapp.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.model.Constants.Companion.latitudeThreshold
import com.example.weatherforecastapp.model.Constants.Companion.longitudeThreshold
import com.example.weatherforecastapp.model.ForeCastWeatherResponse
import com.example.weatherforecastapp.repo.WeatherRepository
import com.example.weatherforecastapp.settings.viewmodel.SettingsViewModel
import com.example.weatherforecastapp.utilities.ForecastWeatherState
import com.example.weatherforecastapp.utilities.getCurrentDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.math.log


class HomeViewModel(private val _irepo: WeatherRepository):ViewModel() {

    private val _apiWeatherResponseState =
        MutableStateFlow<ForecastWeatherState>(ForecastWeatherState.Loading)

    val apiWeatherResponseState: StateFlow<ForecastWeatherState> = _apiWeatherResponseState



    private val _dbWeatherResponseState =
        MutableStateFlow<ForecastWeatherState>(ForecastWeatherState.Loading)

    val dbWeatherResponseState: StateFlow<ForecastWeatherState> = _dbWeatherResponseState


    private  val  _favResponseState=
        MutableStateFlow<ForecastWeatherState>(ForecastWeatherState.Loading)

    val favResponseState:StateFlow<ForecastWeatherState> = _favResponseState

    private lateinit var targetLanguage:String

    private lateinit var targetTempratureUnit:String
    private lateinit var targetDate:String
    private  var targetLongitude:Double=0.0
    private var targetLatitude :Double=0.0
    var iSettingsUpdated = false





    fun fetchForecastWeather(settingsViewModel: SettingsViewModel,latitude: Double, longitude: Double) {


        viewModelScope.launch(Dispatchers.IO) {

            Log.i("iSettingsUpdated", "fetchForecastWeather: "+iSettingsUpdated)



            if (iSettingsUpdated) {
                Log.i("locatonrecieved", "fetchForecastWeather: "+latitude+longitude)
                Log.i("yesUpdated", "fetchForecastWeather: ")
                fetchDataFromApi(latitude, longitude)
                targetLatitude=latitude
                targetLongitude=longitude

                iSettingsUpdated=false
            } else {
                val weatherObject = getWeatherObjectFromRoom()
                Log.i("weatherobject", "fetchForecastWeather: "+weatherObject)

                if (weatherObject != null) {
                    // Weather object exists in the database
                    targetDate = weatherObject.list?.get(0)?.dt_txt?.split(" ")?.get(0).toString()
                    targetLatitude = weatherObject.city?.coord?.lat ?: 0.0
                    targetLongitude = weatherObject.city?.coord?.lon ?: 0.0

                    // Logging for debugging purposes
                    Log.i(
                        "salmaaaaa",
                        "fetchForecastWeather: $targetDate $targetLatitude $targetLongitude $latitude $longitude"
                    )


                    if (targetDate == getCurrentDate() &&
                        Math.abs(targetLongitude - longitude) < longitudeThreshold &&
                        Math.abs(targetLatitude - latitude) < latitudeThreshold
                    ) {
                        // Data is up to date, no need to fetch from the API
                        Log.i("af", "fetchForecastWeather: ")
                        Log.i(
                            "TAG",
                            "fetchForecastWeather: $targetDate $targetLatitude $targetLongitude $latitude $longitude"
                        )
                        targetLatitude = latitude
                        targetLongitude = longitude
                        getSettingConfiguration(settingsViewModel)
                        fetchWeatherDataFromDataBase()
                    } else {
                        // Data is outdated, fetch from API and update the database
                        targetLatitude = latitude
                        targetLongitude = longitude
                        getSettingConfiguration(settingsViewModel)

                        fetchDataFromApi(targetLatitude, targetLongitude)
                    }

                } else {
                    // Weather object does not exist in the database, fetch from API

                    Log.i("out", "fetchForecastWeather: ")

                    getSettingConfiguration(settingsViewModel)

                    fetchDataFromApi(latitude, longitude)
                }

            }

        }
    }





    fun fetchDataFromApi(latitude: Double, longitude: Double) {
        Log.i("in", "fetchDataFromApi: ")


        viewModelScope.launch(Dispatchers.IO){


            _irepo.getForecastWeather(latitude, longitude, targetTempratureUnit, targetLanguage)

                .catch { e ->

                    _apiWeatherResponseState.value = ForecastWeatherState.Failure(e)
                }
                .collect { weatherResponse ->


                        Log.i("beforedelete", "fetchDataFromApi: ")
                        deleteWeatherObjectToRoom()
                        Log.i("afterdelete", "fetchDataFromApi: ")
                        insertWeatherObjectToRoom(weatherResponse)
                        _apiWeatherResponseState.value = ForecastWeatherState.Success(weatherResponse)

                        Log.i("inserted", "fetchDataFromApi: "+weatherResponse.city)

                    }


                }
        }



    fun fetchFavWeatherFromApi(latitude: Double, longitude: Double) {
        Log.i("favlocation", "fetchFavWeatherFromApi: "+latitude+longitude)

        viewModelScope.launch {
            (Dispatchers.IO)
            _irepo.getForecastWeather(latitude, longitude, targetTempratureUnit, targetLanguage)

                .catch { e ->

                    _favResponseState.value = ForecastWeatherState.Failure(e)
                }
                .collect { weatherResponse ->


                    Log.i("fetchapifav", "fetchFavWeatherFromApi: " + weatherResponse.city.name)

                    _favResponseState.value = ForecastWeatherState.Success(weatherResponse)
                }
        }


    }

     fun getSettingConfiguration( settingsViewModel: SettingsViewModel) {

        // Access saved settings
        val savedSettings = settingsViewModel.getSavedSettings()

         targetLanguage = if (savedSettings.selectedLanguage == "Arabic" || savedSettings.selectedLanguage == "العربية") "ar" else "en"


        Log.i("languageselected", "initializeHomeParameters: $targetLanguage")

         targetTempratureUnit = when (savedSettings.selectedTemperatureUnit) {
             "Celsius", "درجة مئوية" -> "metric"
             "Fahrenheit", "درجة فهرنهايت" -> "imperial"
             "Kelvin", "كلفن" -> "standard"
             else -> "standard" // Default to standard in any case
         }

    }


    fun observeSetting(settingsViewModel: SettingsViewModel) {


        viewModelScope.launch{
            Log.i("insideobserver", "getSettingConfigeration: ")
            settingsViewModel.settings.collect { setting ->

                iSettingsUpdated=true

                Log.i("getsetting", "getSettingConfigeration: " + setting.selectedLanguage)

                targetLanguage = if (setting.selectedLanguage == "Arabic" || setting.selectedLanguage == "العربية") "ar" else "en"
                Log.i("targetLanguage", "getSettingConfigeration: " + targetLanguage)

                targetTempratureUnit = when (setting.selectedTemperatureUnit) {
                    "Celsius", "درجة مئوية" -> "metric"
                    "Fahrenheit", "درجة فهرنهايت" -> "imperial"
                    "Kelvin", "كلفن" -> "standard"
                    else -> "standard" // Default to standard in any case
                }

            }
        }

    }



    fun fetchWeatherDataFromDataBase() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("in database", "fetchWeatherDataFromDataBase: ")
            _irepo.getAllStoredWeatherData()
                .catch { e ->
                    _dbWeatherResponseState.value = ForecastWeatherState.Failure(e)
                }
                .collect { weatherResponse ->
                    if (weatherResponse != null) {
                        _dbWeatherResponseState.value = ForecastWeatherState.Success(weatherResponse)
                    } else {
                        // Handle the case when weatherResponse is null
                        // For example, you could emit a different state or log an error
                        Log.e("fetchWeatherDataFromDataBase", "weatherResponse is null")
                    }
                }
        }
    }


    suspend fun insertWeatherObjectToRoom(weatherResponse: ForeCastWeatherResponse){
        _irepo.insertWeatherObject(weatherResponse)
    }

    suspend fun deleteWeatherObjectToRoom(){
        _irepo.deleteWeatherObject()
    }

    suspend fun getWeatherObjectFromRoom(): ForeCastWeatherResponse? {
        return _irepo.getAllStoredWeatherData().firstOrNull()
    }



}








