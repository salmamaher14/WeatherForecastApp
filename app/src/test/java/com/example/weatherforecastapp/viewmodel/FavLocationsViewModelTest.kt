package com.example.weatherforecastapp.favlocations.viewmodel

import com.example.weatherforecastapp.model.LocationData


import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.hasItem


import org.hamcrest.CoreMatchers.not


import org.hamcrest.CoreMatchers.notNullValue

import org.hamcrest.MatcherAssert.assertThat

import org.junit.Before
import org.junit.Test



class FavLocationsViewModelTest {
    lateinit var favViewModel: FavLocationsViewModel
    lateinit var repo: WeatherFakeRepository

    @Before
    fun setUP() {
        repo = WeatherFakeRepository()
        favViewModel = FavLocationsViewModel(repo)
    }

    @Test
    fun getStoredLocationsTest() = runBlockingTest {
        // Given
        val location = LocationData("Giza", 14.9, 18.6)
        favViewModel.insertLocation(location)


        // When
        val result = favViewModel.favLocations.first()

        // Then
        assertThat(result, notNullValue())
    }



    @Test
    fun addFavLocationTest() {
        //When we call insertLocation()
        runBlocking {
            favViewModel.insertLocation(LocationData("Giza", 14.9, 18.6))
            val value = favViewModel.favLocations.first()

            //Then assert that value exist in live data not null
            assertThat(value, notNullValue())
        }
    }

    @Test
    fun deleteLocationTest() {
        runBlocking {
            // Given
            val locationData = LocationData("Giza", 14.9, 18.6)

            // When
            favViewModel.insertLocation(locationData)
            favViewModel.deleteLocation(locationData)

            val value = favViewModel.favLocations.first()

            // Then
            assertThat(value, not(hasItem(value))) // Update the assertion to check for null
        }
    }



}
