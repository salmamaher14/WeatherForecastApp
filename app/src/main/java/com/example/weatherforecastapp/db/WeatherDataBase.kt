package com.example.weatherforecastapp.db
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecastapp.model.Converters
import com.example.weatherforecastapp.model.ForeCastWeatherResponse
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.WeatherAlert

@Database(entities = arrayOf(LocationData::class,WeatherAlert::class,ForeCastWeatherResponse::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getLocationDao(): LocationDao
    abstract fun getWeatherAlertDao(): WeatherAlertDao

    abstract fun getWeatherDataDao():WeatherDataDao

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
                    )
                        .fallbackToDestructiveMigration() // Add this line to enable destructive migrations
                        .build()
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
