package com.example.weatherforecastapp.map
import android.graphics.Color
import android.graphics.Typeface
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.favlocations.view.FavLocationsFragmentDirections

import com.example.weatherforecastapp.favlocations.viewmodel.FavLocationsViewModel
import com.example.weatherforecastapp.favlocations.viewmodel.FavLocationsViewModelFactory
import com.example.weatherforecastapp.local.WeatherLocalDataSourceImpl
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.SettingsData
import com.example.weatherforecastapp.network.WeatherRemoteDataSourceImpl
import com.example.weatherforecastapp.repo.WeatherRepositoryImpl
import com.example.weatherforecastapp.settings.viewmodel.SettingsViewModel
import com.example.weatherforecastapp.settings.viewmodel.SettingsViewModelFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var gmap: GoogleMap
    private lateinit var btnSaveLocation: MaterialButton
    private lateinit var favViewModel:FavLocationsViewModel
    private lateinit var  favFactory:FavLocationsViewModelFactory
    private lateinit var selectedLocation:LocationData
    private lateinit var settingsFactory: SettingsViewModelFactory
    private lateinit var settingViewModel: SettingsViewModel

    private lateinit var selectedSettings: SettingsData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initMapFragmentParameters()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gmap = googleMap
        gmap.setOnMapClickListener { latLng ->
            gmap.addMarker(MarkerOptions().position(latLng))
            val latitude = latLng.latitude
            val longitude = latLng.longitude


            // Convert LatLng to city name using Geocoder
            val cityName = getCityName(latitude, longitude)
            Log.i("ciyname", "onMapReady: "+cityName)
            showSnackbar(cityName)
              selectedLocation= LocationData(cityName,latitude,longitude)



        }

                btnSaveLocation.setOnClickListener {

                    val args = MapFragmentArgs.fromBundle(requireArguments())

                    if(args.fromSettingFragment) {

                        selectedSettings.mapLocation = selectedLocation
                        settingViewModel.updateSettings(selectedSettings)
                    }


//                    }else if(args.fromAlertFragment){
//
//                        val action =MapFragmentDirections.actionMapFragmentToAlertFragment(
//                            selectedLocation
//                        )
//                        findNavController().navigate(action)
//
////                        selectedSettings.mapLocation=selectedLocation
////
////                        settingViewModel.updateSettings(selectedSettings)
//                    }


                    else{
                        favViewModel.insertLocation(selectedLocation)
                    }


                    val snackbar = Snackbar.make(
                        requireView(),
                        "Do you want to save this location?",
                        Snackbar.LENGTH_LONG
                    )

                    snackbar.setAction("Yes") {

                    }

                    snackbar.setAction("Dismiss") {
                        snackbar.dismiss()
                    }

                    // Customizing the Snackbar's text color and style
                    val snackbarView = snackbar.view
                    val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    textView.setTextColor(Color.WHITE)


                    snackbar.show()
                }
    }

    private fun getCityName(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val cityName = addresses[0].locality
                    if (cityName != null && cityName.isNotEmpty()) {
                        return cityName
                    }
                }
            } else
                return "invalid"
        } catch (e: IOException) {
            Log.e("Geocoder", "Error fetching address: ${e.message}", e)

        }
        return "invalid latitude and langitude"
    }




    private fun showSnackbar(cityName: String) {
        val snackbar = Snackbar.make(
            requireView(),
            "You are in $cityName",
            Snackbar.LENGTH_LONG
        )

        snackbar.setAction("Dismiss") {
            snackbar.dismiss()
        }

        // Customizing the Snackbar's text color and style
        val snackbarView = snackbar.view
        val textView =
            snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        textView.setTypeface(null, Typeface.BOLD)

        snackbar.show()
    }

    private fun initUi() {
        btnSaveLocation = requireView().findViewById(R.id.btnAddAlert)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapViewId) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initMapFragmentParameters(){


        favFactory =
            FavLocationsViewModelFactory(
                WeatherRepositoryImpl.getInstance(
                WeatherRemoteDataSourceImpl.getInstance(), WeatherLocalDataSourceImpl(requireContext())
                ))
        favViewModel = ViewModelProvider(this, favFactory).get(FavLocationsViewModel::class.java)

        settingsFactory = SettingsViewModelFactory(requireActivity().application)
        settingViewModel = ViewModelProvider(this, settingsFactory).get(SettingsViewModel::class.java)

        selectedSettings = settingViewModel.getSavedSettings()
    }
}

/*
  val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
    snackbar.show()
 */
