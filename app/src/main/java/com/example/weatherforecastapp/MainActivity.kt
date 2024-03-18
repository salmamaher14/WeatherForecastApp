package com.example.weatherforecastapp
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        drawerLayout = findViewById(R.id.drawerLayoutId)
        navigationView = findViewById(R.id.navigationViewId)

        // Set up action bar
        val actionBar = supportActionBar
        actionBar?.apply {
            setHomeAsUpIndicator(R.drawable.menu_icon)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        // Set up navigation controller
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(navigationView, navController)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}


    /*
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
     */
