package com.example.belportas.presentation.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.belportas.data.LocationService
import com.example.belportas.model.Permissions
import com.example.belportas.model.TaskViewModel
import com.example.belportas.model.handleSendXml
import com.example.belportas.ui.theme.BelPortasTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalGetImage
@ExperimentalPermissionsApi
class MainActivity : AppCompatActivity() {
    private val taskViewModel: TaskViewModel by viewModels()
    private val permissions by lazy { Permissions() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            BelPortasApp(navController, taskViewModel)
        }
        handleSendXml(this, intent, taskViewModel)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            setIntent(it)
            handleSendXml(this, it, taskViewModel)
        }
    }

    private fun closeApp(){
        this.finishAffinity()
    }
    @ExperimentalGetImage
    @Composable
    fun BelPortasApp(navController: NavHostController, taskViewModel: TaskViewModel) {
        val showDialog = remember { mutableStateOf(false) }
        val locationService by lazy { LocationService(this.applicationContext) }

        BelPortasTheme {
            NavHost(navController, startDestination = "permissions") {
                composable("login"){
                    LoginScreen(
                        onNavigateToSignUp = { navController.navigate("signup") },
                        onNavigateToResetPassword = { navController.navigate("resetPassword") },
                        onLoginSuccess = { navController.navigate("permissions") }
                    )
                }
                composable("permissions"){
                    permissions.PermissionRequestScreen(
                        taskViewModel=taskViewModel,
                        locationService = locationService,
                        onPermissionGranted = { navController.navigate("taskList") },
                        onPermissionDenied = { showDialog.value=true}
                    )
                }
                composable("taskList"){
                   TaskListScreen(
                        taskViewModel = taskViewModel,
                        onNavigateToBarcode = { navController.navigate("barcodeScreen") },
                        onNavigateToFile = { navController.navigate("filescreen") },
                        onNavigateToAddTaskScreen = { navController.navigate("addtaskscreen")}
                    )
                }
                composable("signup"){
                    SingUpScreen(
                        onNavigateToLogin = { navController.popBackStack()},
                        onNavigateBack = {navController.popBackStack()})
                }
                composable("resetPassword"){
                    ResetPasswordScreen(
                        onNavigateBack = {navController.popBackStack()})
                }
                composable("barcodeScreen"){
                    BarcodeScreen { navController.popBackStack() }
                }
                composable("filescreen"){
                    FileScreen(
                        navController = navController,
                        onNavigateToBarcode = { navController.navigate("barcodeScreen") },
                        onNavigateAddTaskScreen = { navController.navigate("addtaskscreen") },
                        onNavigateBack = { navController.navigate("taskList") },
                        taskViewModel = taskViewModel
                    )
                }
                composable("addtaskscreen"){
                    AddTaskScreen(
                        onNavigateBack = { navController.popBackStack() },
                        taskViewModel = taskViewModel
                    )
                }
            }
        }
        if (showDialog.value) {
            ConfirmDialogPermissions(
                question = "O aplicativo precisa de todas as permissões para funcionar !",
                onConfirm = {
                    closeApp()
                }
            )
        }
    }
}
