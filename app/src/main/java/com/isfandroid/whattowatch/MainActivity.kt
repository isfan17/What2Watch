package com.isfandroid.whattowatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.isfandroid.whattowatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Retrieve NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        navController
            .addOnDestinationChangedListener { _, destination, _ ->
                // Bottom Navbar Visibility
                when (destination.id)
                {
                    R.id.homeFragment, R.id.searchFragment, R.id.watchlistFragment -> {
                        binding.bottomNav.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.bottomNav.visibility = View.GONE
                    }
                }
            }
    }
}