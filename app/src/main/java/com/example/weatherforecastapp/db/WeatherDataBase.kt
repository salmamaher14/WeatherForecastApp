package com.example.weatherforecastapp
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecastapp.db.LocationDao
import com.example.weatherforecastapp.db.WeatherAlertDao
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.WeatherAlert

@Database(entities = arrayOf(LocationData::class,WeatherAlert::class), version = 1, exportSchema = false)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getLocationDao(): LocationDao
    abstract fun getWeatherAlertDao(): WeatherAlertDao

    companion object {
        private const val TAG = "WeatherDataBase"

        @Volatile
        private var INSTANCE: WeatherDataBase? = null

        fun getInstance(ctx: Context): WeatherDataBase {
            Log.d(TAG, "Getting database instance...")
            return INSTANCE ?: synchronized(this) {
                try {
                    val instance = Room.databaseBuilder(
                        ctx.applicationContext, WeatherDataBase::class.java, "weather_forecast_database"
                    ).build()
                    Log.d(TAG, "Database instance created.")
                    INSTANCE = instance
                    instance
                } catch (ex: Exception) {
                    Log.e(TAG, "Error creating database", ex)
                    throw ex // Rethrow the exception after logging
                }
            }
        }
    }
}
