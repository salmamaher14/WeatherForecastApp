package com.example.weatherforecastapp.local
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.weatherforecastapp.db.LocationDao
import com.example.weatherforecastapp.db.WeatherDataBase
import com.example.weatherforecastapp.model.LocationData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException


@ExperimentalCoroutinesApi
class LocationDaoTest {

    private lateinit var locationDao: LocationDao
    private lateinit var weatherDatabase: WeatherDataBase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        weatherDatabase = Room.inMemoryDatabaseBuilder(context, WeatherDataBase::class.java).build()
        locationDao = weatherDatabase.getLocationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        weatherDatabase.close()
    }

    @Test
    fun getAllStoredLocationsTest() {
        runBlocking {
            // Create test data
            val location1 = LocationData("New York", 40.7128, -74.0060)
            val location2 = LocationData("Los Angeles", 34.0522, -118.2437)

            // Insert test data
            locationDao.insertLocation(location1)
            locationDao.insertLocation(location2)

            // Retrieve all stored locations
            val allStoredLocations = locationDao.getAllStoredLocations().first()

            // Assert that all locations are retrieved correctly
            assertThat(allStoredLocations, contains(location1, location2))
        }
    }

    @Test
    fun insertLocationTest() {
        runBlocking {
            // Create test data
            val location = LocationData("Chicago", 41.8781, -87.6298)

            // Given Insert location
            locationDao.insertLocation(location)

            // When Retrieve the locations from the database
            val allStoredLocationsFlow = locationDao.getAllStoredLocations()

            // Collect the first emitted value from the Flow
            val storedLocation = allStoredLocationsFlow.firstOrNull()?.get(0)

            // Ensure that the first emitted value is not null
            assertThat(storedLocation, notNullValue())

            // Then Assert individual properties of the stored location
            assertThat(storedLocation?.cityName,`is` (location.cityName))
            assertThat(storedLocation?.longitude,`is` (location.longitude))
            assertThat(storedLocation?.latitude,`is` (location.latitude))



        }
    }



    @Test
    fun deleteLocationTest() {
        runBlocking {
            // Create test data
            val location = LocationData("Miami", 25.7617, -80.1918)

            // Given  Insert location into database
            locationDao.insertLocation(location)

            // When ->call function to  Delete location
            val rowsAffected = locationDao.deleteLocation(location)

            // Assert that deletion is successful and returns one affected row
            assertThat(rowsAffected, equalTo(1))

            // Additional assertion: Verify that the location is no longer present in the database
            val allStoredLocationsAfterDeletion = locationDao.getAllStoredLocations().firstOrNull()
            assertThat(allStoredLocationsAfterDeletion, not(contains(location)))
        }
    }
}
