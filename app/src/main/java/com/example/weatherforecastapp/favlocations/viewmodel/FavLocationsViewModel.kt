package com.example.weatherforecastapp.favlocations.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.model.ForeCastWeatherResponse
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.WeatherData
import com.example.weatherforecastapp.model.WeatherRepository
import com.example.weatherforecastapp.utilities.ForecastWeatherState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class FavLocationsViewModel (private  val _irepo:WeatherRepository): ViewModel(){

    private val _weatherResponseState= MutableStateFlow<ForecastWeatherState>(ForecastWeatherState.Loading)
    val weatherResponseState: StateFlow<ForecastWeatherState> = _weatherResponseState



    private var _favLocations: MutableLiveData<List<LocationData>> = MutableLiveData<List<LocationData>>()
    val favLocations: LiveData<List<LocationData>> = _favLocations

    init {
        getLocalLocations()
    }


    fun getLocalLocations(){
        viewModelScope.launch (Dispatchers.IO ){

            _irepo.getStoredLocations().collect{
                    value -> _favLocations.postValue(value)
            }

        }
    }

    fun insertLocation(location: LocationData){

        viewModelScope.launch(Dispatchers.IO) {
            _irepo.insertLocation(location)
            getLocalLocations()
        }
    }

    fun deleteLocation(location: LocationData){
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.deleteLocation(location)
            getLocalLocations()

        }
    }

}
