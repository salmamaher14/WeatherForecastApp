import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.weatherforecastapp.model.SettingsData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class SettingsViewModel(application: Application): ViewModel() {

    private val settingSharedPreferences = application.getSharedPreferences("Setting", Context.MODE_PRIVATE)

    private val _settings = MutableSharedFlow<SettingsData>()

    val settings: SharedFlow<SettingsData> = _settings  // used by others applications

    init {

    }

    fun updateSettings(updatedSettings: SettingsData) {
        with(settingSharedPreferences.edit()) {
            Log.i("updateSettings", "updateSettings: "+updatedSettings.selectedLanguage)
            putString("selectedLocation", updatedSettings.selectedLocation)
            putString("selectedTemperatureUnit", updatedSettings.selectedTemperatureUnit)
            putString("selectedWindSpeedUnit", updatedSettings.selectedWindSpeedUnit)
            putString("selectedLanguage", updatedSettings.selectedLanguage)
            apply() // Commit changes

        }
        _settings.tryEmit(updatedSettings)
        Log.i("Emiit", "updateSettings: The Updated Temo Unit is ${_settings}")

    }

    fun getSavedSettings(): SettingsData {
        return SettingsData(
            selectedLocation = settingSharedPreferences.getString("selectedLocation", "") ?: "",
            selectedTemperatureUnit = settingSharedPreferences.getString("selectedTemperatureUnit", "") ?: "",
            selectedWindSpeedUnit = settingSharedPreferences.getString("selectedWindSpeedUnit", "") ?: "",
            selectedLanguage = settingSharedPreferences.getString("selectedLanguage", "") ?: ""
        )
    }
}
