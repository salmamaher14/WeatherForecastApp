package com.example.weatherforecastapp.settings.viewmodel
import android.app.Application
import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.SettingsData
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class SettingsViewModel(application: Application): ViewModel() {

    private val settingSharedPreferences = application.getSharedPreferences("Setting", Context.MODE_PRIVATE)
    private val gson = Gson()


    private val _settings = MutableSharedFlow<SettingsData>()

    val settings: SharedFlow<SettingsData> = _settings  // used by others applications



    fun updateSettings(updatedSettings: SettingsData) {
        with(settingSharedPreferences.edit()) {
            putString("selectedLocationTool", updatedSettings.selectedLocationTool)
            putString("selectedTemperatureUnit", updatedSettings.selectedTemperatureUnit)
            putString("selectedWindSpeedUnit", updatedSettings.selectedWindSpeedUnit)
            putString("selectedLanguage", updatedSettings.selectedLanguage)
            if (updatedSettings.selectedLocation != null) {
                val json = gson.toJson(updatedSettings.selectedLocation)
                putString("selectedLocation", json)
            } else {
                throw IllegalStateException("selected location is null")
            }
            apply()
        }

        _settings.tryEmit(updatedSettings)
        Log.i("Emiit", "updateSettings: The Updated Settings are $updatedSettings")
    }



    fun getSavedSettings(): SettingsData {

        val selectedLocation = settingSharedPreferences.getString("selectedLocation", null)
        val locationData = if (selectedLocation != null) {
            Log.i("getSavedSettings", "getSavedSettings: "+selectedLocation)
            gson.fromJson(selectedLocation, LocationData::class.java)
        } else {
            null
        }

        return SettingsData(
            selectedLocationTool = settingSharedPreferences.getString("selectedLocationTool", "") ?: "Gps",
            selectedTemperatureUnit = settingSharedPreferences.getString("selectedTemperatureUnit", "") ?: "Meter/sec",
            selectedWindSpeedUnit = settingSharedPreferences.getString("selectedWindSpeedUnit", "") ?: "Celsius",
            selectedLanguage = settingSharedPreferences.getString("selectedLanguage", "") ?: "Arabic",
            selectedLocation = locationData ?: LocationData("", 0.0, 0.0) // Provide default empty LocationData if null
        )
    }




}
