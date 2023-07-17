package com.example.belportas.presentation.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.belportas.model.data.TaskViewModel
import com.example.belportas.ui.theme.BelPortasTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalGetImage
class MainActivity : AppCompatActivity() {
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            BelPortasApp(navController, taskViewModel)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalGetImage
@Composable
fun BelPortasApp(navController: NavHostController, taskViewModel: TaskViewModel) {
    BelPortasTheme {
        NavHost(navController, startDestination = "login") {
            composable("login") {
                LoginScreen(
                    onNavigateToSignUp = {
                        navController.navigate("signup")
                    },
                    onNavigateToResetPassword = {
                        navController.navigate("resetPassword")
                    },
                    onLoginSuccess = {
                        navController.navigate("taskList")
                    }
                )
            }
            composable("signup") {
                SingUp(
                    onNavigateToLogin = {
                        navController.navigate("login")
                    },
                    onNavigateBack = {
                        navController.popBackStack("login", inclusive = false)
                    }
                )
            }
            composable("resetPassword") {
                ResetPasswordScreen(
                    onNavigateToLogin = {
                        navController.navigate("login")
                    }
                )
            }
            composable("taskList") {
                val tasks = taskViewModel.tasks.value ?: emptyList() // Obter a lista de tarefas do TaskViewModel
                TaskListScreen(
                    navController = navController,
                    taskViewModel = taskViewModel,
                    onNavigateToBarcode = {
                        navController.navigate("barcodeScreen")
                    },
                    onNavigateToFile = {
                        navController.navigate("filescreen")
                    }
                )
            }
            composable("barcodeScreen") {
                BarcodeScreen {
                    navController.popBackStack()
                }
            }
            composable("filescreen") {
                FileScreen(
                    navController = navController,
                    onNavigateToBarcode = {
                        navController.navigate("barcodeScreen")
                    },
                    onNavigateAddTaskScreen = {
                        navController.navigate("addtaskscreen")
                    },
                    onNavigateBack={
                        navController.navigate("taskList")
                    },
                    taskViewModel = taskViewModel
                )
            }

            composable("addtaskscreen") {
                AddTaskScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    taskViewModel = taskViewModel
                )
            }
        }
    }
}
