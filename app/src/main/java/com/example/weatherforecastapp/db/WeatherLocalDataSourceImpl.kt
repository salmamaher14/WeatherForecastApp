package com.example.weatherforecastapp
import android.content.Context
import com.example.weatherforecastapp.model.LocationData
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl(context: Context): WeatherLocalDataSource {

    private val dao:WeatherDao by lazy {
        val db :WeatherDataBase=WeatherDataBase.getInstance(context)
        db.getWeatherDao()
    }
    override suspend fun insertLocation(location: LocationData) {
        dao.insert(location)

    }

    override suspend fun deleteLocation(location: LocationData) {
        dao.delete(location)

    }



    override suspend fun getStoredLocations(): Flow<List<LocationData>> {
        return dao.getAllStoredLocations()
    }
}