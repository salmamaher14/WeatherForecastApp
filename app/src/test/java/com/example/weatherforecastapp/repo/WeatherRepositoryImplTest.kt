package com.example.weatherforecastapp.repo

import com.example.weatherforecastapp.datasource.FakeLocalDataSource
import com.example.weatherforecastapp.datasource.FakeRemoteDataSource
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.model.WeatherAlert

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {

    private val fakeRemoteDataSource = FakeRemoteDataSource()
    private val fakeLocalDataSource = FakeLocalDataSource()

    private lateinit var weatherRepository: WeatherRepository

    @Before
    fun setUp() {
        weatherRepository = WeatherRepositoryImpl.getInstance(fakeRemoteDataSource, fakeLocalDataSource)
    }



    @Test
    fun insertLocation() = runBlockingTest {
        // Given
        val locationData = LocationData("saqqara",9.9,77.0)

            // When
            weatherRepository.insertLocation(locationData)

        // Then
        val storedLocations = fakeLocalDataSource.getStoredLocations().first()
        assertThat(storedLocations.contains(locationData), `is`(true))
    }

    @Test
    fun deleteLocation() = runBlockingTest {
        // Given
        val locationData = LocationData("Alex",8.8,77.7)

            // When
            weatherRepository.insertLocation(locationData)
        weatherRepository.deleteLocation(locationData)

        // Then
        val storedLocations = fakeLocalDataSource.getStoredLocations().first()
        assertThat(storedLocations.contains(locationData), `is`(false))
    }

    @Test
    fun getStoredLocations() = runBlockingTest {
        // When
        val result = weatherRepository.getStoredLocations().first()

        // Then
        assertThat(result, `is`(fakeLocalDataSource.storedLocations))
    }

    @Test
    fun insertWeatherAlert() = runBlockingTest {
        // Given
        val weatherAlert = WeatherAlert(1,"1-4-2024")

        // When
        fakeLocalDataSource.insertWeatherAlert(weatherAlert)

        // Then
        val storedAlerts = fakeLocalDataSource.getStoredWeatherAlerts().first()
        assertThat(storedAlerts?.contains(weatherAlert), `is` (true))
    }

    @Test
    fun deleteAlert() = runBlockingTest {
        // Given
        val weatherAlert = WeatherAlert(3,"1-5-2006")
        fakeLocalDataSource.insertWeatherAlert(weatherAlert)

        // When
        fakeLocalDataSource.deleteAlert(weatherAlert)

        // Then
        val storedAlerts = fakeLocalDataSource.getStoredWeatherAlerts().first()
        assertThat(storedAlerts?.contains(weatherAlert), `is`(false))
    }
}
