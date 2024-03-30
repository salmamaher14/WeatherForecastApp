package com.example.weatherforecastapp
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)   // activity alarm  // setcontentview // dialog


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
        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.settingFragment, R.id.favLocationsFragment), drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navigationView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = destination.label
        }


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
    private fun handleIntent(intent: Intent?) {
        if (intent != null && intent.hasExtra("FRAGMENT_TAG")) {
            val fragmentTag = intent.getStringExtra("FRAGMENT_TAG")
            if (fragmentTag != null) {
                // Navigate to the fragment based on the provided tag
                when (fragmentTag) {
                    "HOME_FRAGMENT_TAG" -> navigateToHomeFragment()
                    // Add more cases for other fragment tags if needed
                }
            }
        }
    }

    private fun navigateToHomeFragment() {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController.navigate(R.id.homeFragment)
    }

}


