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
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.app.AlertDialog
import android.app.NotificationChannel


import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log


import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast

import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.alert.viewmodel.AlertReciever
import com.example.weatherforecastapp.home.view.HomeFragmentArgs

import com.example.weatherforecastapp.home.viewmodel.HomeViewModel
import com.example.weatherforecastapp.home.viewmodel.HomeViewModelFactory
import com.example.weatherforecastapp.local.WeatherLocalDataSourceImpl
import com.example.weatherforecastapp.network.WeatherRemoteDataSourceImpl
import com.example.weatherforecastapp.repo.WeatherRepositoryImpl
import com.google.android.material.floatingactionbutton.FloatingActionButton

import java.util.*



class AlertFragment : Fragment() {

    private lateinit var btnSetAlert: FloatingActionButton
    private lateinit var notificationManager: NotificationManager
    private lateinit var alarmManager: AlarmManager
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory

    private val calendar: Calendar = Calendar.getInstance()

    private lateinit var tvSelectedDate: TextView
    private lateinit var tvSelectedTime: TextView


    object AlertParameters {
        var latitude:Double=0.0
        var longitude:Double=0.0
    }





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alert, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val alertLocation = AlertFragmentArgs.fromBundle(requireArguments()).alertLocation
        if (alertLocation != null) {
            Log.i("from fav", "onViewCreated: " + alertLocation.cityName)

        }


        initAlertFragmentParameters()

        createNotificationChannel(requireContext())


        btnSetAlert.setOnClickListener {
            showAlertDialog()


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
                val formattedDate =
                    SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault()).format(calendar.time)
                tvSelectedDate.text = formattedDate
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }


    private fun showTimePickerDialog() {
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
                val formattedTime = String.format("%02d:%02d %s", hour, selectedMinute, amPm)
                tvSelectedTime.text = formattedTime
            },
            hourOfDay,
            minute,
            false // 24-hour format
        )

        timePickerDialog.show()
    }

    private fun showAlertDialog() {
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_alert, null)

        tvSelectedDate = dialogView.findViewById(R.id.tvSelectedDate)
        tvSelectedTime = dialogView.findViewById(R.id.tvSelectedTime)

        val btnAlarm: RadioButton = dialogView.findViewById(R.id.radioButtonAlarm)
        val btnSave: Button = dialogView.findViewById(R.id.btnSave)

        tvSelectedDate.setOnClickListener {
            showDatePickerDialog()
        }

        tvSelectedTime.setOnClickListener {
            showTimePickerDialog()
        }

        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setView(dialogView)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        btnSave.setOnClickListener {
            if (btnAlarm.isChecked) {
//                handleAlarmSelection(calendar.time)
            } else {
                // Handle notification selection
                handleNotificationSelection(calendar.time)
            }
            alertDialog.dismiss()
        }
    }


    private fun handleNotificationSelection(selectedDateTime: Date) {
        val selectedTime = selectedDateTime.time



        val notificationIntent = Intent(requireContext(), AlertReciever::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(), 0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )



        Log.i("time is", "handleNotificationSelecnotion: " + selectedTime)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            selectedTime,
            pendingIntent
        )

        Toast.makeText(
            requireContext(),
            "Notification set for ${selectedDateTime}",
            Toast.LENGTH_SHORT
        ).show()
    }


    fun initAlertFragmentParameters() {

        homeViewModelFactory =
            HomeViewModelFactory(
                WeatherRepositoryImpl.getInstance(
                    WeatherRemoteDataSourceImpl.getInstance(),
                    WeatherLocalDataSourceImpl(requireContext())
                ), requireContext().applicationContext as Application
            )
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        btnSetAlert= requireView().findViewById(R.id.btnAddAlert)
        notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager


    }
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("increea", "createNotificationChannel: ")
            val channelId = "weather_notification_channel"
            val channelName = "Weather Updates"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)

             notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }





}







