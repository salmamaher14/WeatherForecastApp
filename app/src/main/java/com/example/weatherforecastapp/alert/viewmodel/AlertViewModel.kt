package com.example.weatherforecastapp.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.model.WeatherAlert
import com.example.weatherforecastapp.repo.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlertViewModel(private  val _irepo: WeatherRepository):ViewModel() {

    private var _storedAlerts : MutableStateFlow<List<WeatherAlert>> = MutableStateFlow<List<WeatherAlert>>(emptyList())
    val storedAlerts :StateFlow<List<WeatherAlert>> = _storedAlerts


    init {
        getLocalAlerts()
    }


    fun getLocalAlerts(){

        viewModelScope.launch(Dispatchers.IO) {

            _irepo.getStoredWeatherAlerts()
                .collect{
                        result->
                    _storedAlerts.value=result

                }

        }

    }

    fun insertAlert(alert: WeatherAlert){

        viewModelScope.launch(Dispatchers.IO) {
            _irepo.insertWeatherAlert(alert)
            getLocalAlerts()
        }
    }

    fun deleteAlert(alert: WeatherAlert){
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.deleteAlert(alert)
            getLocalAlerts()

        }
    }


}

