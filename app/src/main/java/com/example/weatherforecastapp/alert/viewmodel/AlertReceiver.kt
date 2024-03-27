package com.example.weatherforecastapp.alert.viewmodel



import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri

import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.weatherforecastapp.R

class AlertReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {   // intent -> notification alarm
        showNotification(context)

        //showalarm()
    }

    private fun showNotification(context: Context) {
        // Check if the app has the necessary permission
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NOTIFICATION_POLICY)
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Create a notification channel for Android Oreo and higher
            createNotificationChannel(context)

            // Create a notification builder
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.weather_icon)
                .setContentTitle("Weather Alert")
                .setContentText("Time to check the weather forecast!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            // Show the notification
            try {
                with(NotificationManagerCompat.from(context)) {
                    notify(NOTIFICATION_ID, builder.build())
                }
            } catch (e: SecurityException) {
                // Handle SecurityException
                e.printStackTrace()
            }
        } else {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package", context.packageName, null)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }


    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Weather Alerts",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for weather alerts"
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "weather_alert_channel"
        private const val NOTIFICATION_ID = 123
    }
}
