package com.example.weatherforecastapp.home.view
import SettingsViewModel
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.home.viewmodel.HomeViewModel
import com.example.weatherforecastapp.home.viewmodel.HomeViewModelFactory
import com.example.weatherforecastapp.model.WeatherRepositoryImpl
import com.example.weatherforecastapp.network.WeatherRemoteDataSourceImpl
import com.example.weatherforecastapp.settings.viewmodel.SettingsViewModelFactory
import com.example.weatherforecastapp.utilities.ForecastWeatherState

import com.example.weatherforecastapp.utilities.getAllDatesExceptCurrentDate
import com.example.weatherforecastapp.utilities.getAllDatesOfTheCurrentDate
import com.example.weatherforecastapp.utilities.metersPerSecondToMilesPerHour

import com.google.android.material.textview.MaterialTextView

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

    private lateinit var humidityTextView: MaterialTextView
    private lateinit var windSpeedTextView: MaterialTextView
    private lateinit var cloudsTextView: MaterialTextView
    private lateinit var pressureTextView: MaterialTextView
    private lateinit var weatherStatusImageView: ImageFilterView
    private lateinit var currentTempTextView: MaterialTextView
    private lateinit var weatherDescriptionTextView: MaterialTextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()

        setUpRecyclerView()

        initializeHomeParameters()

        setHoursPerDayInHome()

        homeViewModel.startHome(settingsViewModel)

        homeViewModel.observeSetting(settingsViewModel)

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun setHoursPerDayInHome() {
        lifecycleScope.launch {
            homeViewModel.weatherResponseState.collect { result ->
                when (result) {

                    is ForecastWeatherState.Loading -> {

                        loaderView.visibility = View.VISIBLE
                        hoursRecyclerView.visibility = View.GONE

                    }

                    is ForecastWeatherState.Success -> {
                        Log.i(
                            "setHoursPerDayInHome",
                            "setHoursPerDayInHome: " + result.data.list.get(0)
                        )
                        loaderView.visibility = View.GONE

                        hoursRecyclerView.visibility = View.VISIBLE

                        houresPerDayAdapter.submitList(getAllDatesOfTheCurrentDate(result.data.list))

                        daysListAdapter.submitList(getAllDatesExceptCurrentDate(result.data.list))

                        setAdditionalWeatherInfo(result)

                        Log.i(
                            "currList",
                            "setHoursPerDayInHome: " + getAllDatesOfTheCurrentDate(result.data.list)
                        )

                    }

                    is ForecastWeatherState.Failure -> {

                        loaderView.visibility = View.GONE
                        hoursRecyclerView.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()

                    }
                }

            }
        }
    }

    fun initUi() {

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
            HomeViewModelFactory(WeatherRepositoryImpl.getInstance(WeatherRemoteDataSourceImpl.getInstance()))
        homeViewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)
        settingsFactory = SettingsViewModelFactory(requireActivity().application)
        settingsViewModel =
            ViewModelProvider(this, settingsFactory).get(SettingsViewModel::class.java)


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


    }


    fun checkwindSpeedUnit(result: ForecastWeatherState.Success) {

        if(settingsViewModel.getSavedSettings().selectedWindSpeedUnit=="Mile/hour"){

            val targetWindSpeed:Double = metersPerSecondToMilesPerHour(result.data.list.get(0).wind.speed)

            windSpeedTextView.text = targetWindSpeed.toString()
        }
        else{
            windSpeedTextView.text = result.data.list.get(0).wind.speed.toString()
        }

    }




}


