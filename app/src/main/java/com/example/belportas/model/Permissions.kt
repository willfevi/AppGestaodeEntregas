package com.example.belportas.model

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

class Permissions {
    @Composable
    fun PermissionRequestScreen(onPermissionGranted: () -> Unit) {
        val context = LocalContext.current
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val allPermissionsGranted = permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsResult ->
            if (permissionsResult.values.all { it }) {
                onPermissionGranted()
            }
        }

        LaunchedEffect(allPermissionsGranted) {
            if (!allPermissionsGranted) {
                launcher.launch(permissions)
            } else {
                onPermissionGranted()
            }
        }
    }
}
