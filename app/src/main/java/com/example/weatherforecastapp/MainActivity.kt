package com.example.weatherforecastapp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecastapp.network.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var remoteDataSource: WeatherRemoteDataSourceImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        remoteDataSource = WeatherRemoteDataSourceImpl.getInstance()

        lifecycleScope.launch(Dispatchers.IO) {
            try {

                val response1 = remoteDataSource.getCurrentWeatherOverNetwork(44.34, 10.99)
                val response2=remoteDataSource.getForecastWeatherOverNetwork(44.34, 10.99)
                withContext(Dispatchers.Main) {
                    Log.i("MainActivity", "Weather Response: $response1")
                    // Assuming response is not null, you can access its properties
                    response1.collect { cityName ->
                        Log.i("MainActivity", "City Name: $cityName")
                    }

                    response2.collect{ item->
                        Log.i("MainActivity", "first item:${item.list.get(0)} ")
                    }
                }
            } catch (th: Throwable) {
                Log.e("MainActivity", "Error fetching weather: ${th.message}", th)
            }
        }
    }
}
