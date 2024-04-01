package com.example.weatherforecastapp.viewmodel

import com.example.weatherforecastapp.alert.viewmodel.AlertViewModel
import com.example.weatherforecastapp.favlocations.viewmodel.WeatherFakeRepository
import com.example.weatherforecastapp.model.WeatherAlert
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo


@ExperimentalCoroutinesApi
class AlertViewModelTest {

    lateinit var alertViewModel: AlertViewModel
    lateinit var fakeRepo: WeatherFakeRepository

    @Before
    fun setUP() {
        fakeRepo = WeatherFakeRepository()
        alertViewModel = AlertViewModel(fakeRepo)
    }

    @Test
    fun `test getLocalAlerts`() = runBlocking {
        // Given
        val alerts = listOf(WeatherAlert(1, "2024-04-01"))
        fakeRepo.insertWeatherAlert(alerts.get(0))

        // When
        alertViewModel.getLocalAlerts()

        // Then
        assertThat(alertViewModel.storedAlerts.value, `is`(equalTo(alerts)))
    }

    @Test
    fun `test insertAlert`() = runBlocking {
        // Given
        val alert = WeatherAlert(1, "2024-04-01")

        // When
        alertViewModel.insertAlert(alert)

        // Then
        assertThat(alertViewModel.storedAlerts.value?.first(), `is`(equalTo(alert)))
    }

    @Test
    fun `test deleteAlert`() = runBlocking {
        // Given
        val alert = WeatherAlert(1, "2024-04-01")
        fakeRepo.insertWeatherAlert(alert)

        // When
        alertViewModel.deleteAlert(alert)

        // Then
        assertThat(alertViewModel.storedAlerts.value?.size, `is`(equalTo(0)))
    }
}
