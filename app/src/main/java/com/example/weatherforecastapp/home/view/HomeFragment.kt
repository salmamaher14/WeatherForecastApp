package com.example.weatherforecastapp.home.view

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.home.view.HomeFragment.LocationParameters.count
import com.example.weatherforecastapp.home.view.HomeFragment.LocationParameters.fromFav
import com.example.weatherforecastapp.home.view.HomeFragment.LocationParameters.inCheckLocation
import com.example.weatherforecastapp.home.view.HomeFragment.LocationParameters.isGpsLocation
import com.example.weatherforecastapp.home.view.HomeFragment.LocationParameters.isMapLocation
import com.example.weatherforecastapp.home.view.HomeFragment.LocationParameters.resumeCount
import com.example.weatherforecastapp.home.view.HomeFragment.LocationParameters.targetLanguage
import com.example.weatherforecastapp.home.view.HomeFragment.LocationParameters.targetTempUnit
import com.example.weatherforecastapp.home.viewmodel.HomeViewModel
import com.example.weatherforecastapp.home.viewmodel.HomeViewModelFactory
import com.example.weatherforecastapp.local.WeatherLocalDataSourceImpl
import com.example.weatherforecastapp.model.Constants
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.network.WeatherRemoteDataSourceImpl
import com.example.weatherforecastapp.repo.WeatherRepositoryImpl
import com.example.weatherforecastapp.settings.viewmodel.SettingsViewModel
import com.example.weatherforecastapp.settings.viewmodel.SettingsViewModelFactory
import com.example.weatherforecastapp.utilities.ForecastWeatherState
import com.example.weatherforecastapp.utilities.getAllDatesExceptCurrentDate
import com.example.weatherforecastapp.utilities.getAllDatesOfTheCurrentDate
import com.example.weatherforecastapp.utilities.getCurrentDate
import com.example.weatherforecastapp.utilities.getSeparateDataAndTime
import com.example.weatherforecastapp.utilities.metersPerSecondToMilesPerHour
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var settingsViewModel: SettingsViewModel

    private lateinit var hoursRecyclerView: RecyclerView
    private lateinit var daysRecyclerView: RecyclerView
    private lateinit var houresPerDayAdapter: HoursPerDayListAdapter
    private lateinit var daysListAdapter: DaysListAdapter
    private lateinit var loaderView: ProgressBar
    private lateinit var hoursPerDaylayoutManager: LinearLayoutManager
    private lateinit var daysListLayoutManager: LinearLayoutManager

    private lateinit var homeFactory: HomeViewModelFactory
    private lateinit var settingsFactory: SettingsViewModelFactory

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var humidityTextView: MaterialTextView
    private lateinit var windSpeedTextView: MaterialTextView
    private lateinit var cloudsTextView: MaterialTextView
    private lateinit var pressureTextView: MaterialTextView
    private lateinit var weatherStatusImageView: ImageFilterView
    private lateinit var currentTempTextView: MaterialTextView
    private lateinit var weatherDescriptionTextView: MaterialTextView
    private lateinit var locationTextView: MaterialTextView
    private lateinit var dateTextView: MaterialTextView




    object LocationParameters {
        var gpsLatitude: Double = 0.0
        var gpsLongitude: Double = 0.0
        var mapLatitude: Double = 0.0
        var mapLongitude: Double = 0.0
        var targetLanguage:String=""
        var targetTempUnit:String=""
        var count:Int=0
        var resumeCount:Int=0
        var isGpsLocation: Boolean = false
        var isMapLocation: Boolean = false
        var fromFav:Boolean=false
        var inCheckLocation:Int= 0
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initUi()

        setUpRecyclerView()

        initializeHomeParameters()




        val favLocation = HomeFragmentArgs.fromBundle(requireArguments()).location
        if (favLocation != null) {
            fromFav=true
            Log.i("from fav", "onViewCreated: " + favLocation.cityName)
            Log.i("favLocation", "onViewCreated: "+favLocation.latitude+favLocation.longitude)


            homeViewModel.getSettingConfiguration(settingsViewModel)

            homeViewModel.fetchFavWeatherFromApi(favLocation.latitude, favLocation.longitude)
        } else {
            LocationParameters.count += 1
            Log.i("beforesetting${LocationParameters.count}", "onViewCreated: ")

            homeViewModel.observeSetting(settingsViewModel)
            checkLocationProvideGpsOrMap()

            Log.i("afterobserving${LocationParameters.count}", "onViewCreated: ")
        }




        lifecycleScope.launch(Dispatchers.Main) {
            homeViewModel.dbWeatherResponseState.collect { result ->
                Log.i("fetchfromdb", "onViewCreated: ")
                when (result) {

                    is ForecastWeatherState.Loading -> {

                        loaderView.visibility = View.VISIBLE
                        hoursRecyclerView.visibility = View.GONE

                    }

                    is ForecastWeatherState.Success -> {
                        Log.i(
                            "setHoursPerDayInHomeDb",
                            "setHoursPerDayInHome: " + result.data.list.get(0)
                        )
                        loaderView.visibility = View.GONE

                        hoursRecyclerView.visibility = View.VISIBLE

                        houresPerDayAdapter.submitList(getAllDatesOfTheCurrentDate(result.data.list))
                        Log.i("houresPerDayAdapter", "setHoursPerDayInHome: " + result.data.list)
                        Log.i(
                            "testDate",
                            result.data.list[0].dt_txt.split(" ")[0] + " " + getCurrentDate()
                        )


                        daysListAdapter.submitList(getAllDatesExceptCurrentDate(result.data.list))

                        dateTextView.text =
                            getSeparateDataAndTime(result.data.list.get(0).dt_txt.toString()).second.toString() + " "
                        getSeparateDataAndTime(result.data.list.get(0).dt_txt.toString()).first

                        setAdditionalWeatherInfo(result)

                        Log.i(
                            "currList",
                            "setHoursPerDayInHome: " + result.data.city.name
                        )

                    }

                    is ForecastWeatherState.Failure -> {
                        loaderView.visibility = View.GONE
                        hoursRecyclerView.visibility = View.GONE
                        val errorMessage =
                            "Error: ${result.msg}" // Extracting error message from Throwable
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                    }
                }

            }
        }




        lifecycleScope.launch(Dispatchers.Main) {
            homeViewModel.apiWeatherResponseState.collect { result ->

                Log.i("apiiii", "onViewCreated: "+result)

                when (result) {

                    is ForecastWeatherState.Loading -> {

                        loaderView.visibility = View.VISIBLE
                        hoursRecyclerView.visibility = View.GONE

                    }

                    is ForecastWeatherState.Success -> {
                        Log.i(
                            "setHoursPerDayInHomeDb",
                            "setHoursPerDayInHome: " + result.data.list.get(0)
                        )
                        loaderView.visibility = View.GONE

                        hoursRecyclerView.visibility = View.VISIBLE

                        houresPerDayAdapter.submitList(getAllDatesOfTheCurrentDate(result.data.list))
                        Log.i("houresPerDayAdapter", "setHoursPerDayInHome: " + result.data.list)
                        Log.i(
                            "testDate",
                            result.data.list[0].dt_txt.split(" ")[0] + " " + getCurrentDate()
                        )


                        daysListAdapter.submitList(getAllDatesExceptCurrentDate(result.data.list))

                        dateTextView.text =
                            getSeparateDataAndTime(result.data.list.get(0).dt_txt.toString()).second.toString() + " "
                        getSeparateDataAndTime(result.data.list.get(0).dt_txt.toString()).first

                        setAdditionalWeatherInfo(result)

                        Log.i(
                            "currList",
                            "setHoursPerDayInHome: " + result.data.city.name
                        )

                    }

                    is ForecastWeatherState.Failure -> {
                        loaderView.visibility = View.GONE
                        hoursRecyclerView.visibility = View.GONE
                        val errorMessage =
                            "Error: ${result.msg}" // Extracting error message from Throwable
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                    }
                }

            }
        }






        lifecycleScope.launch(Dispatchers.Main) {
            homeViewModel.favResponseState.collect { result ->


                when (result) {


                    is ForecastWeatherState.Loading -> {

                        loaderView.visibility = View.VISIBLE
                        hoursRecyclerView.visibility = View.GONE

                    }

                    is ForecastWeatherState.Success -> {

                        Log.i("fetchfav", "onViewCreated: "+result.data.city.name)


                        Log.i(
                            "setHoursPerDayInHomeApi",
                            "setHoursPerDayInHome: " + result.data.list.get(0)
                        )
                        loaderView.visibility = View.GONE

                        hoursRecyclerView.visibility = View.VISIBLE

                        houresPerDayAdapter.submitList(getAllDatesOfTheCurrentDate(result.data.list))
                        Log.i("houresPerDayAdapter", "setHoursPerDayInHome: " + result.data.list)
                        Log.i(
                            "testDate",
                            result.data.list[0].dt_txt.split(" ")[0] + " " + getCurrentDate()
                        )


                        daysListAdapter.submitList(getAllDatesExceptCurrentDate(result.data.list))

                        dateTextView.text =
                            getSeparateDataAndTime(result.data.list.get(0).dt_txt.toString()).second.toString() + " "
                        getSeparateDataAndTime(result.data.list.get(0).dt_txt.toString()).first

                        setAdditionalWeatherInfo(result)

                        Log.i(
                            "currList",
                            "setHoursPerDayInHome: " + result.data.city.name
                        )

                    }

                    is ForecastWeatherState.Failure -> {
                        loaderView.visibility = View.GONE
                        hoursRecyclerView.visibility = View.GONE
                        val errorMessage =
                            "Error: ${result.msg}" // Extracting error message from Throwable
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                    }
                }

            }
        }




    }


    private fun setUpRecyclerView() {
        hoursPerDaylayoutManager = LinearLayoutManager(context)

        daysListLayoutManager = LinearLayoutManager(context)

        houresPerDayAdapter = HoursPerDayListAdapter()

        daysListAdapter = DaysListAdapter()

        hoursPerDaylayoutManager.orientation = RecyclerView.HORIZONTAL
        daysListLayoutManager.orientation = RecyclerView.VERTICAL

        hoursRecyclerView.adapter = houresPerDayAdapter

        daysRecyclerView.adapter = daysListAdapter

        hoursRecyclerView.layoutManager = hoursPerDaylayoutManager

        daysRecyclerView.layoutManager = daysListLayoutManager

    }


    fun initUi() {

        locationTextView = requireView().findViewById(R.id.locationInHomeTextView)

        dateTextView = requireView().findViewById(R.id.dateTextView)

        hoursRecyclerView = requireView().findViewById(R.id.hoursRecyclerView)

        daysRecyclerView = requireView().findViewById(R.id.daysRecyclerView)

        loaderView = requireView().findViewById(R.id.loader)

        humidityTextView = requireView().findViewById(R.id.humadityTextView)

        cloudsTextView = requireView().findViewById(R.id.cloudsTextView)

        windSpeedTextView = requireView().findViewById(R.id.windTextView)

        pressureTextView = requireView().findViewById(R.id.pressureTextView)

        weatherStatusImageView = requireView().findViewById(R.id.weatherImageView)

        currentTempTextView = requireView().findViewById(R.id.currentTempTextView)

        weatherDescriptionTextView = requireView().findViewById(R.id.statusTextView)

    }

    fun initializeHomeParameters() {
        homeFactory =
            HomeViewModelFactory(
                WeatherRepositoryImpl.getInstance(
                    WeatherRemoteDataSourceImpl.getInstance(),
                    WeatherLocalDataSourceImpl(requireContext())
                ), requireContext().applicationContext as Application
            )
        homeViewModel = ViewModelProvider(requireActivity(), homeFactory).get(HomeViewModel::class.java)
        settingsFactory = SettingsViewModelFactory(requireActivity().application)
        settingsViewModel =
            ViewModelProvider(requireActivity(), settingsFactory).get(SettingsViewModel::class.java)


    }


    fun setAdditionalWeatherInfo(result: ForecastWeatherState.Success) {

        humidityTextView.text = result.data.list.get(0).main.humidity.toString()

        checkwindSpeedUnit(result)

        cloudsTextView.text = result.data.list.get(0).clouds.toString()

        pressureTextView.text = result.data.list.get(0).main.pressure.toString()

        context?.let {
            Glide.with(it)
                .load("https://openweathermap.org/img/wn/${result.data.list.get(0).weather[0].icon}.png")
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(weatherStatusImageView)
        }

        currentTempTextView.text = result.data.list.get(0).main.temp.toString()

        weatherDescriptionTextView.text = result.data.list.get(0).weather[0].description
        Log.i("TAG", "setAdditionalWeatherInf ${result.data.city.name}")

        locationTextView.text = result.data.city.name


    }


    fun checkwindSpeedUnit(result: ForecastWeatherState.Success) {

        if (settingsViewModel.getSavedSettings().selectedWindSpeedUnit == "Mile/hour") {

            val targetWindSpeed: Double =
                metersPerSecondToMilesPerHour(result.data.list.get(0).wind.speed)

            windSpeedTextView.text = targetWindSpeed.toString()
        } else {
            windSpeedTextView.text = result.data.list.get(0).wind.speed.toString()
        }

    }


    fun checkPermissions(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    fun enableLocationServices() {
        Log.i("enable ", "enableLocationServices: ")
        Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun getFreshLocation() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedLocationClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply {
                setPriority(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY)
            }.build(),

            object : LocationCallback() {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    val location = locationResult.lastLocation

                    if (location != null) {
                        LocationParameters.gpsLatitude = location.latitude
                        LocationParameters.gpsLongitude = location.longitude

                        Log.i(
                            "gpslocation",
                            "onLocationResult: ${LocationParameters.gpsLatitude} + ${LocationParameters.gpsLongitude}"
                        )

                        // Call a coroutine to observe settings



                        homeViewModel.fetchForecastWeather(
                            settingsViewModel,
                            LocationParameters.gpsLatitude,
                            LocationParameters.gpsLongitude
                        )


                    } else {
                        Log.i("locationgps", "onLocationResult: $location")
                    }
                }
            },
            Looper.myLooper()
        )
    }



    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_LOCATION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, handle the permission result
                Log.i("userPermission", "onRequestPermissionsResult: ")
                getFreshLocation()
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
                Log.e("HomeFragment", "Location permission denied")
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
     fun checkLocationProvideGpsOrMap() {
        Log.i("IncheckLocation ${inCheckLocation++}", "checkLocationProvideGpsOrMap: ")

        val savedSettings = settingsViewModel.getSavedSettings()

        targetLanguage=savedSettings.selectedLanguage
        targetTempUnit=savedSettings.selectedTemperatureUnit
        Log.i("savedsetting", "checkLocationProvideGpsOrMap: " + savedSettings)
        var locationTool = savedSettings.selectedLocationTool
        Log.i("locationtool", "checkLocationProvideGpsOrMap: " + locationTool)


        if (locationTool == "Gps") {
            Log.i("yesGps", "checkLocationProvideGpsOrMap: ")
            isGpsLocation = true

            getLocationByGps()
        } else {
            isMapLocation = true
            Log.i("yesMap", "checkLocationProvideGpsOrMap: ")
            val locationData = settingsViewModel.getSavedSettings().mapLocation  // location of map
            Log.i("maplocation", "checkLocationProvideGpsOrMap: "+locationData)
            getLocationByMap(locationData)

        }


    }

    fun getLocationByGps() {



            if (checkPermissions(requireContext())) {
                if (isLocationEnabled(requireContext())) {
                    Log.i("inside ", "location enabled: ")
                    getFreshLocation()


                } else {
                    enableLocationServices()

                    getFreshLocation()


                }
            } else {
                Log.i("in else", "onStart: ")   //  take permission from user
                if (isAdded && activity != null) {

                    Log.i("isadded", "getLocationByGps: ")

                    // Fragment is attached to an activity
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        Constants.REQUEST_LOCATION_CODE
                    )
                } else {
                    // Fragment is not attached to an activity
                    Log.e("HomeFragment", "Fragment is not attached to any activity")
                }


            }
        }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getLocationByMap(location: LocationData) {

        LocationParameters.mapLatitude = location.latitude
        LocationParameters.mapLongitude = location.longitude
        Log.i("in getlocation by map", "getLocationByMap: "+LocationParameters.mapLatitude+LocationParameters.mapLongitude)



        homeViewModel.fetchForecastWeather(
            settingsViewModel,
            LocationParameters.mapLatitude,
            LocationParameters.mapLongitude
        )





    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onResume() {
//        super.onResume()
//
//        if(!fromFav){
//            checkLocationProvideGpsOrMap()
//
//
//        }else{
//            fromFav=false
//        }
//
//
//        Log.i("onresume+${resumeCount++}", "onResume: ")
//
//
//    }

}








