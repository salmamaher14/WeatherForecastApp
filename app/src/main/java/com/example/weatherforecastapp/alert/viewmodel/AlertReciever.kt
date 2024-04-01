package com.example.weatherforecastapp.alert.viewmodel

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weatherforecastapp.MainActivity

import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.alert.view.AlarmActivity
import com.example.weatherforecastapp.alert.view.AlertFragment
import com.example.weatherforecastapp.home.view.HomeFragment

import com.example.weatherforecastapp.local.WeatherLocalDataSourceImpl
import com.example.weatherforecastapp.network.WeatherRemoteDataSourceImpl
import com.example.weatherforecastapp.repo.WeatherRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class AlertReciever():BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override  fun onReceive(context: Context?, intent: Intent?) {
        Log.i("recieveNotification ", "onReceive: ")




        val weatherLocalDataSource = context?.let { WeatherLocalDataSourceImpl(it) }
        val weatherRemoteDataSource = WeatherRemoteDataSourceImpl.getInstance()

        val repo =
            WeatherRepositoryImpl.getInstance(weatherRemoteDataSource, weatherLocalDataSource!!)



//        CoroutineScope(Dispatchers.Main).launch {
//            try {
//                repo.getForecastWeather(AlertFragment.AlertParameters.latitude,AlertFragment.AlertParameters.longitude,AlertFragment.AlertParameters.tempUnit,AlertFragment.AlertParameters.language)
//                    .collect{
//
//                        weatherResponse->
//                        val  weatherStatus=weatherResponse.list.get(0).weather.get(0).description
//                        Log.i("incollect", "onReceive: "+weatherStatus)
////                        showNotification(context,weatherStatus)
//                        showAlarm(context,weatherStatus)
//                    }
//
//            } catch (e: Exception) {
//                Log.e("AlertReceiver", "Error fetching weather data", e)
//            }
//        }
//
//
//
//    }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                repo.getForecastWeather(
                    AlertFragment.AlertParameters.latitude,
                    AlertFragment.AlertParameters.longitude,
                    AlertFragment.AlertParameters.tempUnit,
                    AlertFragment.AlertParameters.language
                ).collect { weatherResponse ->
                    val weatherStatus = weatherResponse.list[0].weather[0].description
                    Log.i("incollect", "onReceive: $weatherStatus")

                    val alarmType = intent?.getStringExtra("alarmType")

                    if (alarmType == "alarm") {
                        showAlarmActivity(context, weatherStatus)
                    } else {
                        showNotification(context, weatherStatus)
                    }
                }
            } catch (e: Exception) {
                Log.e("AlertReceiver", "Error fetching weather data", e)
            }
        }
    }

}


private fun showNotification(context: Context?, weatherStatus: String) {
    Log.i("shooo", "showNotification: "+weatherStatus)
    val channelId = "weather_notification_channel"
    val notificationId = 123



    val notificationBuilder = NotificationCompat.Builder(context!!, channelId)
        .setSmallIcon(R.drawable.weather_icon)
        .setContentTitle("Weather Update")
        .setContentText("Current weather: $weatherStatus")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)



    val notificationManager = NotificationManagerCompat.from(context)

    if (!notificationManager.areNotificationsEnabled()) {

        AlertDialog.Builder(context)
            .setTitle("Enable Notifications")
            .setMessage("To receive weather updates, please enable notifications for this app.")
            .setPositiveButton("Settings") { dialog, _ ->
                // Open app settings
                val intent = Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
        return
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Weather Updates",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {

        return
    }
    notificationManager.notify(notificationId, notificationBuilder.build())
}

@RequiresApi(Build.VERSION_CODES.O)
private fun showAlarm(context: Context?, weatherStatus: String) {

    Log.i("weatherstatus $weatherStatus", "showAlarm: ")

    val alertDialog = AlertDialog.Builder(context)
        .setTitle("Weather Alert")
        .setMessage("Weather status $weatherStatus")
        .setPositiveButton("Dismiss") { dialog, _ ->
            // Dismiss the alarm dialog
            dialog.dismiss()
        }
        .setCancelable(false) // Ensure the dialog cannot be canceled by clicking outside of it
        .create()

    // Show the dialog as an application overlay
    alertDialog.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
    alertDialog.show()
}


private fun showAlarmActivity(context: Context?, weatherStatus: String) {
    val intent = Intent(context, AlarmActivity::class.java).apply {
        putExtra("weatherStatus", weatherStatus)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context?.startActivity(intent)
}






