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
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weatherforecastapp.MainActivity

import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.alert.view.AlertFragment
import com.example.weatherforecastapp.home.view.HomeFragment

import com.example.weatherforecastapp.local.WeatherLocalDataSourceImpl
import com.example.weatherforecastapp.network.WeatherRemoteDataSourceImpl
import com.example.weatherforecastapp.repo.WeatherRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class AlertReciever():BroadcastReceiver() {

    override  fun onReceive(context: Context?, intent: Intent?) {
        Log.i("recieveNotification ", "onReceive: ")




        val weatherLocalDataSource = context?.let { WeatherLocalDataSourceImpl(it) }
        val weatherRemoteDataSource = WeatherRemoteDataSourceImpl.getInstance()

        val repo =
            WeatherRepositoryImpl.getInstance(weatherRemoteDataSource, weatherLocalDataSource!!)

        CoroutineScope(Dispatchers.Main).launch {
            try {
//                repo.getForecastWeather(AlertFragment.AlertParameters.latitude,AlertFragment.AlertParameters.longitude)
                val weatherObject = repo.getWeatherObjectStoredFromDb()
                val weatherStatus = weatherObject?.list?.get(0)?.weather?.get(0)?.description
                if (weatherStatus != null) {
                    showNotification(context, weatherStatus)
                } else {
                    Log.e("AlertReceiver", "Weather status is null")
                }
            } catch (e: Exception) {
                Log.e("AlertReceiver", "Error fetching weather data", e)
            }
        }



    }
    }


private fun showNotification(context: Context?, weatherStatus: String) {
    Log.i("shooo", "showNotification: ")
    val channelId = "weather_notification_channel"
    val notificationId = 123

    val intent = Intent(context, MainActivity::class.java)
    intent.putExtra("FRAGMENT_TAG", "HOME_FRAGMENT_TAG") // Replace "HOME_FRAGMENT_TAG" with your fragment tag

    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

    // Create a PendingIntent for the notification
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notificationBuilder = NotificationCompat.Builder(context!!, channelId)
        .setSmallIcon(R.drawable.weather_icon)
        .setContentTitle("Weather Update")
        .setContentText("Current weather: $weatherStatus")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    val notificationManager = NotificationManagerCompat.from(context)

    // Check if the app has notification permission
    if (!notificationManager.areNotificationsEnabled()) {
        // Notify the user that they need to grant notification permissions
        // You can show a Snackbar, Toast, or create a dialog to inform the user
        // that they need to enable notifications for the app.

        // Here, we're creating an AlertDialog to prompt the user to go to app settings.
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

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
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
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    notificationManager.notify(notificationId, notificationBuilder.build())
}



