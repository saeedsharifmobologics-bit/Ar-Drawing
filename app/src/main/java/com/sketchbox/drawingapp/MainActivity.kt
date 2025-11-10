package com.sketchbox.drawingapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.TransformExperimental
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.sketchbox.drawingapp.databinding.ActivityMainBinding
import com.sketchbox.drawingapp.fragments.CameraPreviewFragment
import com.sketchbox.drawingapp.fragments.SettingFragment
import com.sketchbox.drawingapp.utils.CameraPreviewUtils.bottomBarClicked
import com.sketchbox.drawingapp.utils.CommonUtils.showLeaveCameraDialog

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Light status bar for better visibility on light backgrounds
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
            true
        // Apply system window insets to main container
        val mainContainer = findViewById<View>(R.id.main_container)
        ViewCompat.setOnApplyWindowInsetsListener(mainContainer) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup NavController
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupBottomNavigation()
        setupDrawerListener()
        setupBottomBarVisibilityController()

    }


    @OptIn(TransformExperimental::class)
    private fun setupBottomNavigation() {
        val bottomBar = binding.bottomNavigationBar
        bottomBar.itemMenuRes = R.menu.menu

        // Sync bottom bar index with current destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val index = when (destination.id) {
                R.id.homeFragment -> 0
                R.id.categoriesFragment -> 1
                R.id.profileFragment -> 2
                R.id.favouriteFragment -> 3
                R.id.viewCategoryFragment -> 1
                else -> bottomBar.itemActiveIndex
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

            // Ignore if already on same destination
            if (navController.currentDestination?.id == destinationId) return

            //If coming from CameraPreviewFragment, show confirmation dialog
            if (navController.currentDestination?.id == R.id.cameraPreviewFragment) {
                bottomBarClicked = true
                val cameraFragment =
                    navHostFragment.childFragmentManager.fragments.firstOrNull { it is CameraPreviewFragment } as? CameraPreviewFragment
                if (cameraFragment?.recordingState == CameraPreviewFragment.RecordingState.RECORDING) {
                    Toast.makeText(this, "Please Stop Recording First ", Toast.LENGTH_SHORT).show()
                    bottomBar.itemActiveIndex = 0

                } else {

                    showLeaveCameraDialog(this) { success ->
                        if (success) {

                            val navOptions = NavOptions.Builder()
                                .setLaunchSingleTop(true)
                                .setPopUpTo(navController.graph.startDestinationId, false)
                                .build()
                            navController.navigate(destinationId, null, navOptions)

                            // Change index ONLY after confirmed navigation
                            bottomBar.itemActiveIndex = index
                        } else {
                            bottomBar.itemActiveIndex = 0
                        }

                    }
                }

            } else {
                val navOptions = NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(navController.graph.startDestinationId, false)
                    .build()
                navController.navigate(destinationId, null, navOptions)
                bottomBar.itemActiveIndex = index
            }
        }
    }

    private fun setupBottomBarVisibilityController() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.subscriptionFragment -> binding.bottomNavigationBar.visibility = View.GONE
                else -> binding.bottomNavigationBar.visibility = View.VISIBLE
            }
        }
    }

    private fun setupDrawerListener() {
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {
                val currentFragment =
                    supportFragmentManager.findFragmentById(R.id.drawer_container)
                if (currentFragment == null || currentFragment !is SettingFragment) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.drawer_container, SettingFragment())
                        .commit()
                }
            }

            override fun onDrawerClosed(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {}
        })

        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

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
