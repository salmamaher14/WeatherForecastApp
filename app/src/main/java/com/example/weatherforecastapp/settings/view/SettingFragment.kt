package com.example.weatherforecastapp.settings.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.SettingsData
import com.example.weatherforecastapp.settings.viewmodel.SettingsViewModel
import com.example.weatherforecastapp.settings.viewmodel.SettingsViewModelFactory
import com.example.weatherforecastapp.utilities.LocaleManager
import com.google.android.material.radiobutton.MaterialRadioButton


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

        displaySavedSettings(selectedSettings)  // display stored values in shared preferences




        gpsRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected location setting to GPS
                selectedSettings.selectedLocationTool = getString(R.string.gps)
                Log.i("GPS", "setSetting: "+getString(R.string.gps))
                viewModel.updateSettings(selectedSettings)
                mapRadioButton.isChecked=false

            }
        }

        mapRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected location setting to Map
                selectedSettings.selectedLocationTool = getString(R.string.map)
                gpsRadioButton.isChecked=false
                viewModel.updateSettings(selectedSettings)

                val action = SettingFragmentDirections.actionSettingFragmentToMapFragment(fromSettingFragment =true)
                findNavController().navigate(action)

            }
        }

        englishRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected language setting to English
                selectedSettings.selectedLanguage = getString(R.string.english)
                Log.i("english", "setSetting: "+selectedSettings.selectedLanguage )
                viewModel.updateSettings(selectedSettings)
                arabicRadioButton.isChecked=false

                LocaleManager.setLocale(requireContext(),"en")
                requireActivity().recreate()
            }
        }

        arabicRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected language setting to Arabic
                selectedSettings.selectedLanguage = getString(R.string.arabic)
                viewModel.updateSettings(selectedSettings)
                englishRadioButton.isChecked=false

                LocaleManager.setLocale(requireContext(),"ar")
                requireActivity().recreate()
            }
        }

        kelvinRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected temperature unit setting to Kelvin
                selectedSettings.selectedTemperatureUnit = getString(R.string.kelvin)
                viewModel.updateSettings(selectedSettings)

                celsiusRadioButton.isChecked=false
                fahrenheitRadioButton.isChecked=false
            }
        }

        celsiusRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected temperature unit setting to Celsius
                selectedSettings.selectedTemperatureUnit = getString(R.string.celsius)
                viewModel.updateSettings(selectedSettings)
                kelvinRadioButton.isChecked=false
                fahrenheitRadioButton.isChecked=false
            }
        }

        fahrenheitRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected temperature unit setting to Fahrenheit
                selectedSettings.selectedTemperatureUnit = getString(R.string.fahrenheit)
                viewModel.updateSettings(selectedSettings)
                kelvinRadioButton.isChecked=false
                celsiusRadioButton.isChecked=false
            }
        }

        meterRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected wind speed unit setting to Meter per Second
                selectedSettings.selectedWindSpeedUnit = getString(R.string.meterPerSecond)
                viewModel.updateSettings(selectedSettings)
                milesRadioButton.isChecked=false
            }
        }

        milesRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Update selected wind speed unit setting to Miles per Hour
                selectedSettings.selectedWindSpeedUnit = getString(R.string.milesPerHour)
                viewModel.updateSettings(selectedSettings)
                meterRadioButton.isChecked=false
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

        gpsRadioButton.isChecked = savedSettings.selectedLocationTool == getString(R.string.gps)

        mapRadioButton.isChecked = savedSettings.selectedLocationTool == getString(R.string.map)

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
            selectedLocationTool = getString(R.string.gps),
            selectedTemperatureUnit = getString(R.string.celsius),
            selectedWindSpeedUnit = getString(R.string.meterPerSecond),
            selectedLanguage = getString(R.string.arabic),
            selectedLocation = LocationData("",0.0,0.0)

        )
    }


}

// object locale manager




