package com.example.weatherforecastapp.alert.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherforecastapp.R
import android.app.AlarmManager
import android.app.Application
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.app.AlertDialog
import android.icu.text.SimpleDateFormat


import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecastapp.alert.viewmodel.AlertReceiver
import com.example.weatherforecastapp.home.viewmodel.HomeViewModel
import com.example.weatherforecastapp.home.viewmodel.HomeViewModelFactory
import com.example.weatherforecastapp.local.WeatherLocalDataSourceImpl
import com.example.weatherforecastapp.network.WeatherRemoteDataSourceImpl
import com.example.weatherforecastapp.repo.WeatherRepositoryImpl
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*



class AlertFragment : Fragment() {
    private lateinit var btnSetAlert: MaterialButton
    private lateinit var notificationManager: NotificationManager
    private lateinit var alarmManager: AlarmManager
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeFactory: HomeViewModelFactory
    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var currentWeatherState:String
    private lateinit var tvSelectedDate:TextView
    private lateinit var tvSelectedTime:TextView




    companion object {
        private const val CHANNEL_ID = "alert_channel"
        private const val NOTIFICATION_ID = 1
        const val EXTRA_DESCRIPTION = "description"
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAlertFragmentParameters()



        btnSetAlert.setOnClickListener {
            showAlertDialog()
        }



        lifecycleScope.launch(Dispatchers.Main) {
            homeViewModel.weatherResponse.observe(viewLifecycleOwner) { result ->

                if (result!=null)
                    currentWeatherState = result.list.get(0).weather.get(0).description


            }
        }

    }


    private fun showDatePickerDialog() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                showTimePickerDialog(tvSelectedDate)

                val selectedDateStr = String.format("%02d-%02d-%d", selectedDay, selectedMonth + 1, selectedYear)

                tvSelectedDate.text = selectedDateStr
                showTimePickerDialog(tvSelectedDate)
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }


    private fun showTimePickerDialog(tvSelectedDate: TextView) {
        val currentTime = Calendar.getInstance()
        val hourOfDay = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireActivity(),
            { _, selectedHourOfDay, selectedMinute ->
                calendar.set(Calendar.HOUR_OF_DAY, selectedHourOfDay)
                calendar.set(Calendar.MINUTE, selectedMinute)

                val amPm = if (selectedHourOfDay < 12) "AM" else "PM"
                val hour = if (selectedHourOfDay % 12 == 0) 12 else selectedHourOfDay % 12
                val selectedTimeStr = String.format("%02d:%02d %s", hour, selectedMinute, amPm)
                val selectedDateStr = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault()).format(calendar.time)

                tvSelectedDate.text = "$selectedDateStr $selectedTimeStr"

                handleNotificationSelection(calendar.time)
            },
            hourOfDay,
            minute,
            false // 24-hour format
        )

        timePickerDialog.show()
    }




    private fun handleNotificationSelection(selectedDateTime: Date) {
        val selectedTime = selectedDateTime.time
        val currentTime = System.currentTimeMillis()

        if (selectedTime <= currentTime) {

            Toast.makeText(requireContext(), "Selected time must be in the future", Toast.LENGTH_SHORT).show()
            return
        }

        val notificationIntent = Intent(requireContext(), AlertReceiver::class.java).apply {

            putExtra(EXTRA_DESCRIPTION, currentWeatherState)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            NOTIFICATION_ID,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.set(AlarmManager.RTC_WAKEUP, selectedTime, pendingIntent)
    }




    fun initAlertFragmentParameters(){

        btnSetAlert = requireView().findViewById(R.id.btnAddAlert)

        homeFactory =
            HomeViewModelFactory(
                WeatherRepositoryImpl.getInstance(
                    WeatherRemoteDataSourceImpl.getInstance(),
                    WeatherLocalDataSourceImpl(requireContext())
                ), requireContext().applicationContext as Application
            )
        homeViewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)



        notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager


    }


    private fun showAlertDialog() {
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_alert, null)

        tvSelectedDate = dialogView.findViewById<TextView>(R.id.tvSelectedDate)
        tvSelectedTime = dialogView.findViewById<TextView>(R.id.tvSelectedTime)

        tvSelectedDate.setOnClickListener {
            showDatePickerDialog()
        }

        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setView(dialogView)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        // You can find views and set click listeners here
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            // Handle save button click
            alertDialog.dismiss() // Dismiss the dialog after saving
        }
    }


}
