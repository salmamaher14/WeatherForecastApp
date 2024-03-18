package com.example.weatherforecastapp.settings.view

import SettingsViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.model.SettingsData
import com.example.weatherforecastapp.settings.viewmodel.SettingsViewModelFactory
import com.google.android.material.radiobutton.MaterialRadioButton
import kotlin.math.log


class SettingFragment : Fragment() {

    private lateinit var gpsRadioButton: MaterialRadioButton
    private lateinit var mapRadioButton: MaterialRadioButton
    private lateinit var englishRadioButton: MaterialRadioButton
    private lateinit var arabicRadioButton: MaterialRadioButton
    private lateinit var kelvinRadioButton: MaterialRadioButton
    private lateinit var celsiusRadioButton: MaterialRadioButton
    private lateinit var fahrenheitRadioButton: MaterialRadioButton
    private lateinit var meterRadioButton: MaterialRadioButton
    private lateinit var milesRadioButton: MaterialRadioButton
    private lateinit var settingsFactory:SettingsViewModelFactory

    private lateinit var viewModel: SettingsViewModel
    private lateinit var selectedSettings: SettingsData


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        settingsFactory = SettingsViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, settingsFactory).get(SettingsViewModel::class.java)

        selectedSettings = viewModel.getSavedSettings()

        displaySavedSettings(selectedSettings)

        viewModel.updateSettings(selectedSettings)?:getDefaultSettings()



        gpsRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected location setting to GPS
                selectedSettings.selectedLocation = getString(R.string.gps)
                Log.i("GPS", "setSetting: "+getString(R.string.gps))
                viewModel.updateSettings(selectedSettings)
            }
        }

        mapRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected location setting to Map
                selectedSettings.selectedLocation = getString(R.string.map)
                viewModel.updateSettings(selectedSettings)

            }
        }

        englishRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected language setting to English
                selectedSettings.selectedLanguage = getString(R.string.english)
                Log.i("english", "setSetting: "+selectedSettings.selectedLanguage )
                viewModel.updateSettings(selectedSettings)
            }
        }

        arabicRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected language setting to Arabic
                selectedSettings.selectedLanguage = getString(R.string.arabic)
                viewModel.updateSettings(selectedSettings)
            }
        }

        kelvinRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected temperature unit setting to Kelvin
                selectedSettings.selectedTemperatureUnit = getString(R.string.kelvin)
                viewModel.updateSettings(selectedSettings)
            }
        }

        celsiusRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected temperature unit setting to Celsius
                selectedSettings.selectedTemperatureUnit = getString(R.string.celsius)
                viewModel.updateSettings(selectedSettings)
            }
        }

        fahrenheitRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected temperature unit setting to Fahrenheit
                selectedSettings.selectedTemperatureUnit = getString(R.string.fahrenheit)
                viewModel.updateSettings(selectedSettings)
            }
        }

        meterRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected wind speed unit setting to Meter per Second
                selectedSettings.selectedWindSpeedUnit = getString(R.string.meterPerSecond)
                viewModel.updateSettings(selectedSettings)
            }
        }

        milesRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected wind speed unit setting to Miles per Hour
                selectedSettings.selectedWindSpeedUnit = getString(R.string.milesPerHour)
                viewModel.updateSettings(selectedSettings)
            }
        }


//
//        // Display the saved settings in the UI
//        displaySavedSettings(selectedSettings)?:getDefaultSettings()





    }

    override fun onResume() {
        super.onResume()
        selectedSettings = viewModel.getSavedSettings()
        displaySavedSettings(selectedSettings)
    }

    fun initUi() {
        gpsRadioButton = requireView().findViewById(R.id.gpsRadioButton)
        mapRadioButton = requireView().findViewById(R.id.mapRadioButton)
        englishRadioButton = requireView().findViewById(R.id.englishRadioButton)
        arabicRadioButton = requireView().findViewById(R.id.arabicRadioButton)
        kelvinRadioButton = requireView().findViewById(R.id.kelvinRadioButton)
        celsiusRadioButton = requireView().findViewById(R.id.celsiusRadioButton)
        fahrenheitRadioButton = requireView().findViewById(R.id.fahrenheitRadioButton)
        meterRadioButton = requireView().findViewById(R.id.meterRadioButton)
        milesRadioButton = requireView().findViewById(R.id.milesRadioButton)
    }







    fun  displaySavedSettings(savedSettings:SettingsData){

        gpsRadioButton.isChecked = savedSettings.selectedLocation == getString(R.string.gps)

        mapRadioButton.isChecked = savedSettings.selectedLocation == getString(R.string.map)

        englishRadioButton.isChecked = savedSettings.selectedLanguage == getString(R.string.english)

        arabicRadioButton.isChecked = savedSettings.selectedLanguage == getString(R.string.arabic)

        kelvinRadioButton.isChecked = savedSettings.selectedTemperatureUnit == getString(R.string.kelvin)

        celsiusRadioButton.isChecked = savedSettings.selectedTemperatureUnit == getString(R.string.celsius)

        fahrenheitRadioButton.isChecked = savedSettings.selectedTemperatureUnit == getString(R.string.fahrenheit)

        meterRadioButton.isChecked = savedSettings.selectedWindSpeedUnit == getString(R.string.meterPerSecond)

        milesRadioButton.isChecked = savedSettings.selectedWindSpeedUnit == getString(R.string.milesPerHour)

    }

    private fun getDefaultSettings(): SettingsData {
        // Initialize default settings here
        return SettingsData(
            selectedLocation = getString(R.string.gps),
            selectedTemperatureUnit = getString(R.string.celsius),
            selectedWindSpeedUnit = getString(R.string.meterPerSecond),
            selectedLanguage = getString(R.string.arabic)
        )
    }


}



