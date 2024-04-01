package com.example.weatherforecastapp.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.repo.WeatherRepository

class AlertViewModelFactory (private val _repo: WeatherRepository): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AlertViewModel::class.java)){
            AlertViewModel(_repo) as T
        }else{
            throw  IllegalArgumentException("View model class not found")
        }
    }
}

