package com.example.belportas.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.belportas.data.LocationService
import com.example.belportas.model.TaskViewModel

@Composable
fun PermissionRequestScreen(
    taskViewModel: TaskViewModel,
    locationService: LocationService,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current

    val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_SMS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,

        )

    val fineLocationGranted = Manifest.permission.ACCESS_FINE_LOCATION.let {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    val allPermissionsGranted = permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsResult ->
        if (permissionsResult.values.all { it }) {
            locationService.startLocationUpdates { /* Aqui vai o código para executar quando a localização for atualizada */ }
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    val backgroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            locationService.startLocationUpdates { /* Aqui vai o código para executar quando a localização for atualizada */ }
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    LaunchedEffect(allPermissionsGranted) {
        if (!allPermissionsGranted) {
            if (fineLocationGranted) {
                backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                launcher.launch(permissions)
            }
        } else {
            taskViewModel.initializeLocation()
            onPermissionGranted()
        }
    }
}