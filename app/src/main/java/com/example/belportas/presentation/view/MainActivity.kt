package com.example.belportas.presentation.view
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.belportas.model.TaskViewModel
import com.example.belportas.ui.theme.BelPortasTheme
import com.example.belportas.view.AddTaskScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@ExperimentalGetImage
class MainActivity : ComponentActivity() {
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
@Composable
fun PermissionScreen(
    permissionStatus: PermissionStatus,
    onRequestPermission: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)) {
            Text(text = "Your Permission Status: ", style = MaterialTheme.typography.h6)
            when(permissionStatus) {
                is PermissionStatus.Denied -> {
                    if (permissionStatus.shouldShowRationale) {
                        Text(text = "We need permission to continue this demo")
                    } else {
                        Text(text = "Looks like you permanently denied permission. Please provide in Settings")
                    }
                }
                is PermissionStatus.Granted -> {
                    Text(text = "Nice!! permission set")
                }
            }
            if (permissionStatus.shouldShowRationale) {
                Button(onClick = onRequestPermission) {
                    Text(text = "Click to request permission")
                }
            }
        }
    }
}

fun Modifier.widthInFraction(fraction: Float): Modifier = this.then(
    Modifier.layout { measurable, constraints ->
        val maxWidth = constraints.maxWidth
        val width = (maxWidth * fraction).toInt()
        val placeable = measurable.measure(
            constraints.copy(
                minWidth = width,
                maxWidth = width
            )
        )
        layout(width, placeable.height) {
            placeable.place(0, 0)
        }
    }
)

@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalGetImage
@Composable
fun BelPortasApp(navController: NavHostController, taskViewModel: TaskViewModel) {
    BelPortasTheme {
        NavHost(navController, startDestination = "login") {
            composable("login") {
                val permissionState = rememberPermissionState(permission = android.Manifest.permission.READ_SMS)
                PermissionScreen(
                    permissionStatus = permissionState.status,
                    onRequestPermission = {
                        permissionState.launchPermissionRequest()
                    }
                )

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
                BarcodeScreen(
                    onDismiss = {
                        navController.popBackStack()
                    },
                    onNavigateBack = {
                        navController.navigate("filescreen")
                    }
                )
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




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BelPortasTheme {
        Text("Preview")
    }
}
