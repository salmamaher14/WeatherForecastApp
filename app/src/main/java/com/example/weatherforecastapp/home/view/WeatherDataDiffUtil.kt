package com.example.weatherforecastapp.home.view

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.example.weatherforecastapp.model.WeatherData

class WeatherDataDiffUtil:DiffUtil.ItemCallback<WeatherData>() {
    override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem.dt==newItem.dt
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return newItem == oldItem
    }
}

