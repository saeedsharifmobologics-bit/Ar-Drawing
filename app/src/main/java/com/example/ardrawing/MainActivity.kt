package com.example.ardrawing

import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.ardrawing.databinding.ActivityMainBinding
import com.example.ardrawing.fragments.SettingFragment
import me.ibrahimsn.lib.SmoothBottomBar
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Light status bar for better visibility on light backgrounds
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        // Apply system window insets to main container
        val mainContainer = findViewById<View>(R.id.main_container)
        ViewCompat.setOnApplyWindowInsetsListener(mainContainer) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Setup NavController from NavHostFragment once
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupBottomNavigation()
        setupDrawerListener()
        setupBottomBarVisibilityController()
    }

    private fun setupBottomNavigation() {
        val bottomBar = binding.bottomNavigationBar
        bottomBar.itemMenuRes = R.menu.menu

        // Initial sync: fallback to 0 if no match
        bottomBar.itemActiveIndex = when (navController.currentDestination?.id) {
            R.id.homeFragment -> 0
            R.id.categoriesFragment -> 1
            R.id.profileFragment -> 2
            R.id.favouriteFragment -> 3
            else -> 0
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val index = when (destination.id) {
                R.id.homeFragment -> 0
                R.id.categoriesFragment -> 1
                R.id.profileFragment -> 2
                R.id.favouriteFragment -> 3

                else -> bottomBar.itemActiveIndex // fallback to current bottom bar selection
            }
            if (bottomBar.itemActiveIndex != index) {
                bottomBar.itemActiveIndex = index
            }
        }

        bottomBar.onItemSelected = fun(index: Int) {
            val destinationId = when (index) {
                0 -> R.id.homeFragment
                1 -> R.id.categoriesFragment
                2 -> R.id.profileFragment
                3 -> R.id.favouriteFragment
                else -> return
            }

            if (navController.currentDestination?.id != destinationId) {
                val navOptions = NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(navController.graph.startDestinationId, false)
                    .build()
                navController.navigate(destinationId, null, navOptions)
            }
        }

    }
    private fun setupBottomBarVisibilityController() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.subscriptionFragment -> {  // 👈 this is the fragment where you want to HIDE bottom nav
                    binding.bottomNavigationBar.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationBar.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun setupDrawerListener() {
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {
                // Load SettingFragment only when drawer opens and if not already loaded
                val currentFragment = supportFragmentManager.findFragmentById(R.id.drawer_container)
                if (currentFragment == null || currentFragment !is SettingFragment) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.drawer_container, SettingFragment())
                        .commit()
                }
            }

            override fun onDrawerClosed(drawerView: View) {
            }
            override fun onDrawerStateChanged(newState: Int) {

            }
        })
    }

    // Open drawer only if it's not already open
    fun openDrawer() {
        if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    fun closeDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }
}
