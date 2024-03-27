package com.example.weatherforecastapp.model

class Constants {
    companion object {
        const val API_KEY ="7780468f128cefbd740526072b8638e2"

        const val longitudeThreshold = 0.001 // Adjust threshold as needed
        const val latitudeThreshold = 0.001
        val REQUEST_LOCATION_CODE = 3456
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }
}