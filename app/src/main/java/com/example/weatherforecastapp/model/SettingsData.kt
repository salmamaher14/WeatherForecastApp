package com.example.weatherforecastapp.model

data class SettingsData(

    var selectedLocationTool: String,
    var mapLocation:LocationData,
    var selectedTemperatureUnit: String,
    var selectedWindSpeedUnit: String,
    var selectedLanguage: String
)
