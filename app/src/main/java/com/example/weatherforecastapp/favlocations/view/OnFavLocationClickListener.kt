package com.example.weatherforecastapp.favlocations.view

import android.health.connect.datatypes.ExerciseRoute.Location
import com.example.weatherforecastapp.model.LocationData

interface OnFavLocationClickListener {
    fun OnFavLocationClick(location:LocationData)
}