package com.example.weatherforecastapp.favlocations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.home.viewmodel.HomeViewModel
import com.example.weatherforecastapp.model.WeatherRepository

class FavLocationsViewModelFactory (private val _repo: WeatherRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(FavLocationsViewModel::class.java)){
            FavLocationsViewModel(_repo) as T
        }else{
            throw  IllegalArgumentException("View model class not found")
        }
    }
}