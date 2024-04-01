package com.example.weatherforecastapp.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecastapp.db.WeatherDataBase
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.WeatherAlert
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherLocalDataSourceImplTest {

    private lateinit var database: WeatherDataBase
    private lateinit var dataSource: WeatherLocalDataSourceImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, WeatherDataBase::class.java).build()
        dataSource = WeatherLocalDataSourceImpl(context)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertLocationAndRetrieve() = runBlocking {
        // Given
        val location = LocationData("Saqqara",88.9,55.8)

        // When
        dataSource.insertLocation(location)

        // Then
        val storedLocations = dataSource.getStoredLocations().first()
        assertEquals(1, storedLocations.size)
        assertEquals(location, storedLocations[0])
    }

    @Test
    fun testDeleteLocation() = runBlocking {
        // Given
        val location = LocationData("Alharam",9.9,7.6)
        dataSource.insertLocation(location)

        // When
        dataSource.deleteLocation(location)

        // Then
        val storedLocations = dataSource.getStoredLocations().first()
        assertEquals(0, storedLocations.size)
    }

    @Test
    fun testInsertWeatherAlertAndRetrieve() = runBlocking {
        // Given
        val weatherAlert = WeatherAlert(7,"12-3-2024")

        // When
        dataSource.insertWeatherAlert(weatherAlert)

        // Then
        val storedAlerts = dataSource.getStoredWeatherAlerts().first()
        assertEquals(1, storedAlerts.size)
        assertEquals(weatherAlert, storedAlerts[0])
    }

    @Test
    fun testDeleteAlert() = runBlocking {
        // Given
        val weatherAlert = WeatherAlert(9,"1-5-2024")
        dataSource.insertWeatherAlert(weatherAlert)

        // When
        dataSource.deleteAlert(weatherAlert)

        // Then
        val storedAlerts = dataSource.getStoredWeatherAlerts().first()
        assertEquals(0, storedAlerts.size)
    }


}
