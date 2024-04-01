package com.example.weatherforecastapp.favlocations.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.repo.WeatherRepository
import com.example.weatherforecastapp.utilities.ForecastWeatherState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class FavLocationsViewModel (private  val _irepo: WeatherRepository): ViewModel(){

    private var _favLocations : MutableStateFlow<List<LocationData>> = MutableStateFlow<List<LocationData>>(emptyList())
    val favLocations : StateFlow<List<LocationData>> = _favLocations


//
//    private var _favLocations: MutableLiveData<List<LocationData>> = MutableLiveData<List<LocationData>>()
//    val favLocations: LiveData<List<LocationData>> = _favLocations

    init {
        getLocalLocations()
    }


    fun getLocalLocations(){

        viewModelScope.launch(Dispatchers.IO) {
            _irepo.getStoredLocations()
                .collect{
                    result->
                    _favLocations.value=result

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
