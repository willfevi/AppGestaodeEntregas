package com.example.belportas.model

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
class Permissions {
    @Composable
    fun PermissionRequestScreen(
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit
    ) {
        val context = LocalContext.current
        val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
        val smsPermissionState = rememberPermissionState(Manifest.permission.READ_SMS)
        val storagePermissionState = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
        val internetPermissionState = rememberPermissionState(Manifest.permission.INTERNET)
        val callPhonePermissionState = rememberPermissionState(Manifest.permission.CALL_PHONE)
        val fineLocationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_COARSE_LOCATION)

        PermissionRequestContent(
            permissions = listOf(
                cameraPermissionState,
                smsPermissionState,
                storagePermissionState,
                internetPermissionState,
                callPhonePermissionState,
                fineLocationPermissionState,
                coarseLocationPermissionState
            ),
            onPermissionGranted = onPermissionGranted,
            onPermissionDenied = onPermissionDenied
        )
    }

    @Composable
    fun PermissionRequestContent(
        permissions: List<PermissionState>,
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit
    ) {
        val currentPermissionIndex = remember { mutableStateOf(0) }

        if (currentPermissionIndex.value < permissions.size) {
            val permissionState = permissions[currentPermissionIndex.value]

            if (permissionState.hasPermission) {
                currentPermissionIndex.value++
            } else if (!permissionState.shouldShowRationale) {
                onPermissionDenied()
            }
        } else {
            onPermissionGranted()
        }
    }

    @Composable
    fun RequestPermission(
        permissionState: PermissionState,
        permissionName: String,
        rationaleMessage: String,
        onPermissionRequested: () -> Unit
    ) {
        if (!permissionState.hasPermission) {
            if (permissionState.shouldShowRationale) {
                AlertDialog(
                    onDismissRequest = { /* Dismiss the dialog */ },
                    title = { Text(text = "Permission Required") },
                    text = { Text(text = rationaleMessage) },
                    confirmButton = {
                        Button(
                            onClick = onPermissionRequested
                        ) {
                            Text(text = "Request $permissionName Permission")
                        }
                    }
                )
            } else {
                onPermissionRequested()
            }
        }
    }

    @Composable
    fun rememberPermissionState(permission: String): PermissionState {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            // Callback is not needed for this implementation
        }

        val permissionStatus = rememberPermissionStatus(permission)

        return PermissionState(permission, permissionLauncher, permissionStatus)
    }

    @Composable
    fun rememberPermissionStatus(permission: String): PermissionStatus {
        val context = LocalContext.current

        return when {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                PermissionStatus.Granted
            }
            shouldShowRequestPermissionRationale(context as Activity, permission) -> {
                PermissionStatus.ShouldShowRationale
            }
            else -> PermissionStatus.Initial
        }
    }

    private fun shouldShowRequestPermissionRationale(activity: Activity, permission: String): Boolean {
        return activity.shouldShowRequestPermissionRationale(permission)
    }

    class PermissionState(
        private val permission: String,
        private val permissionLauncher: ActivityResultLauncher<String>,
        private val permissionStatus: PermissionStatus
    ) {
        val hasPermission: Boolean
            get() = permissionStatus == PermissionStatus.Granted

        val shouldShowRationale: Boolean
            get() = permissionStatus == PermissionStatus.ShouldShowRationale
    }

    enum class PermissionStatus {
        Initial,
        Granted,
        Denied,
        ShouldShowRationale
    }

}