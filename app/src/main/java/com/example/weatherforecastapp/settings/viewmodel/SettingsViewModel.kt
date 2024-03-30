package com.example.weatherforecastapp.settings.viewmodel
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.SettingsData
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

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
            if (updatedSettings.mapLocation != null) {
                val json = gson.toJson(updatedSettings.mapLocation)
                putString("selectedLocation", json)
            } else {
                throw IllegalStateException("selected location is null")
            }
            apply()
        }

         viewModelScope.launch {
             _settings.emit(updatedSettings)
             Log.i("Emiit", "updateSettings: The Updated Settings are $updatedSettings")
         }


    }



    fun getSavedSettings(): SettingsData {
        try {
            val mapLocation = settingSharedPreferences.getString("selectedLocation", null)
            val locationData = if (mapLocation != null) {
                Log.i("getSavedSettings", "getSavedSettings: $mapLocation")
                gson.fromJson(mapLocation, LocationData::class.java)
            } else {
                null
            }
            Log.i("storedata", "getSavedSettings: "+settingSharedPreferences.all)

            return SettingsData(
                selectedLocationTool = settingSharedPreferences.getString("selectedLocationTool", "Gps") ?: "Gps",
                selectedTemperatureUnit = settingSharedPreferences.getString("selectedTemperatureUnit", "Celsius") ?: "Celsius",
                selectedWindSpeedUnit = settingSharedPreferences.getString("selectedWindSpeedUnit", "Meter/sec") ?: "Meter/sec",
                selectedLanguage = settingSharedPreferences.getString("selectedLanguage", "Arabic") ?: "Arabic",
                mapLocation = locationData ?: LocationData("", 0.0, 0.0) // Provide default empty LocationData if null
            )
        } catch (e: Exception) {
            // Log the error for debugging purposes
            Log.e("getSavedSettings", "Error while retrieving settings", e)

            // Provide default settings or handle the error based on your application's requirements
            return getDefaultSettings()
        }
    }

    fun getDefaultSettings(): SettingsData {
        // Provide default settings here
        return SettingsData(
            selectedLocationTool = "Gps",
            selectedTemperatureUnit = "Celsius",
            selectedWindSpeedUnit = "Meter/sec",
            selectedLanguage = "Arabic",
            mapLocation = LocationData("", 0.0, 0.0)
        )
    }





}
