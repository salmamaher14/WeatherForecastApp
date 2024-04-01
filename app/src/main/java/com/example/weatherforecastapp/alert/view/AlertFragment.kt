package com.example.weatherforecastapp.alert.view


import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Application
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.alert.viewmodel.AlertReciever
import com.example.weatherforecastapp.alert.viewmodel.AlertViewModel
import com.example.weatherforecastapp.alert.viewmodel.AlertViewModelFactory
import com.example.weatherforecastapp.favlocations.view.FavLocationsListAdapter
import com.example.weatherforecastapp.favlocations.view.OnFavLocationClickListener
import com.example.weatherforecastapp.favlocations.view.OnRemoveLocationClickListener
import com.example.weatherforecastapp.home.viewmodel.HomeViewModel
import com.example.weatherforecastapp.home.viewmodel.HomeViewModelFactory
import com.example.weatherforecastapp.local.WeatherLocalDataSourceImpl
import com.example.weatherforecastapp.model.WeatherAlert
import com.example.weatherforecastapp.network.WeatherRemoteDataSourceImpl
import com.example.weatherforecastapp.repo.WeatherRepositoryImpl
import com.example.weatherforecastapp.settings.viewmodel.SettingsViewModel
import com.example.weatherforecastapp.settings.viewmodel.SettingsViewModelFactory
import com.example.weatherforecastapp.utilities.isNetworkAvailable
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AlertFragment : Fragment(), OnRemoveAlertClickListener {

    private lateinit var btnSetAlert: FloatingActionButton
    private lateinit var notificationManager: NotificationManager
    private lateinit var alarmManager: AlarmManager
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var settingsFactory:SettingsViewModelFactory

    private lateinit var settingViewModel: SettingsViewModel

    private val calendar: Calendar = Calendar.getInstance()

    private lateinit var tvSelectedDate: TextView
    private lateinit var tvSelectedTime: TextView

    private lateinit var alertsRecyclerView: RecyclerView
    private lateinit var alertListAdapter: AlertListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var alertViewModel: AlertViewModel
    private lateinit var  alertFactory: AlertViewModelFactory


    object AlertParameters {
        var latitude:Double=0.0
        var longitude:Double=0.0
        var language:String=""
        var tempUnit:String=""
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

            AlertParameters.latitude=alertLocation.latitude
            AlertParameters.longitude=alertLocation.longitude
            showAlertDialog()
            Log.i("from fav", "onViewCreated: " + alertLocation.cityName)

        }


        initAlertFragmentParameters()
        setRecyclerViewParameters()
        createNotificationChannel(requireContext())


        btnSetAlert.setOnClickListener {
            val action = AlertFragmentDirections.actionAlertFragmentToMapFragment(fromAlertFragment =true)

            findNavController().navigate(action)

        }

        lifecycleScope.launch {
            alertViewModel.storedAlerts.collect{
                    alertList ->
                alertListAdapter.submitList(alertList)

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

                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                val formattedDate = sdf.format(calendar.time)

                val weatherAlert = WeatherAlert(date = formattedDate)
                alertViewModel.insertAlert(weatherAlert)
                handleAlarmSelection(calendar.time)
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

        notificationIntent.putExtra("alarmType", "notification")

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

    private fun handleAlarmSelection(selectedDateTime: Date) {
        val selectedTime = selectedDateTime.time

        val notificationIntent = Intent(requireContext(), AlertReciever::class.java)

        // Pass an extra parameter indicating the user's choice
        notificationIntent.putExtra("alarmType", "alarm")

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(), 0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            selectedTime,
            pendingIntent
        )

        Toast.makeText(
            requireContext(),
            "Alarm set for ${selectedDateTime}",
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

        settingsFactory = SettingsViewModelFactory(requireActivity().application)
        settingViewModel = ViewModelProvider(requireActivity(), settingsFactory).get(SettingsViewModel::class.java)

        btnSetAlert= requireView().findViewById(R.id.btnAddAlert)
        notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        AlertParameters.language=settingViewModel.getSavedSettings().selectedLanguage
        AlertParameters.tempUnit=settingViewModel.getSavedSettings().selectedTemperatureUnit

        alertFactory =
            AlertViewModelFactory(
                WeatherRepositoryImpl.getInstance(
                    WeatherRemoteDataSourceImpl.getInstance(), WeatherLocalDataSourceImpl(requireContext())
                ))
        alertViewModel = ViewModelProvider(requireActivity(), alertFactory).get(AlertViewModel::class.java)


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

    override fun OnRemoveAlertClick(alert: WeatherAlert) {

            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage("Are you sure you want to delete this alert?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    // User clicked Yes, delete the location
                    alertViewModel.deleteAlert(alert)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, id ->
                    // User clicked No, do nothing
                    dialog.dismiss()
                }

            val alert = dialogBuilder.create()
            alert.show()

    }


    fun setRecyclerViewParameters(){

        alertsRecyclerView= requireView().findViewById(R.id.alertRecyclerView)

        layoutManager= LinearLayoutManager(context)
        alertListAdapter= AlertListAdapter(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        alertsRecyclerView.adapter = alertListAdapter

    }

    fun checkInternetConnection(context: Context) {
        if (isNetworkAvailable(context)) {
            // Internet connection is available
            Toast.makeText(context, "You are online", Toast.LENGTH_SHORT).show()
        } else {
            // No internet connection
            Toast.makeText(context, "You are offline", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        checkInternetConnection(requireContext())
    }

}



