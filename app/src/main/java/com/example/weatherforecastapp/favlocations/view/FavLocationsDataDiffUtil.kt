package com.example.weatherforecastapp.favlocations.view

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.example.weatherforecastapp.model.LocationData

class FavLocationsDataDiffUtil : DiffUtil.ItemCallback<LocationData>()  {
    override fun areItemsTheSame(oldItem: LocationData, newItem: LocationData): Boolean {
        return oldItem.cityName==newItem.cityName
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: LocationData, newItem: LocationData): Boolean {
        return newItem == oldItem
    }
}