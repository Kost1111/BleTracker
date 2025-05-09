package com.bletracker

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.bleTracker.core.PermissionManager.Companion.permissionManager
import com.bletracker.presentation.MainScreen
import com.bletracker.ui.theme.BleTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupPermissions()
    }

    private fun setupPermissions() {
        permissionManager {
            context = this@MainActivity
            permissionsToRequest = arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            onPermissionGranted {
                setContent {
                    BleTrackerTheme {
                        val navController = rememberNavController()
                        MainScreen(navController)
                    }
                }
            }

            onPermissionDenied {
                showMessage()
            }

            doRequest()
        }
    }

    private fun showMessage() {
        toast()
    }

    private fun toast() {
        Toast.makeText(
            this@MainActivity,
            "Some required permissions are denied",
            Toast.LENGTH_SHORT
        ).show()
    }
}
