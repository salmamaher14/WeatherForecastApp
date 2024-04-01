package com.example.weatherforecastapp.alert.view

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherforecastapp.model.WeatherAlert

class AlertDataDiffUtil: DiffUtil.ItemCallback<WeatherAlert>() {
    override fun areItemsTheSame(oldItem: WeatherAlert, newItem: WeatherAlert): Boolean {
        return oldItem.date==newItem.date
    }

    override fun areContentsTheSame(oldItem: WeatherAlert, newItem: WeatherAlert): Boolean {
        return newItem == oldItem
    }
}



