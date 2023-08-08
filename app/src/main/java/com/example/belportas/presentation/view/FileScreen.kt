package com.example.belportas.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.belportas.R
import com.example.belportas.model.TaskViewModel
import com.example.belportas.model.getFilePickerLauncher

@Composable
fun FileScreen(
    navController: NavHostController,
    onNavigateToBarcode: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateAddTaskScreen: () -> Unit,
    taskViewModel: TaskViewModel
) {
    val context = LocalContext.current
    val showFileError = remember { mutableStateOf(false) }
    val filePickerLauncher = getFilePickerLauncher(context, taskViewModel, showFileError)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier =Modifier.fillMaxWidth(0.50f)){ Image(
                        painter = painterResource(id = R.drawable.icon_belportas_topbar),
                        contentDescription = "App top bar logo "
                    )}
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    onClick = {
                        try {
                            showFileError.value = false
                            filePickerLauncher.launch("text/xml")
                        } catch (e :Exception) {
                            println(e.message)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(100.dp)
                        .padding(vertical = 8.dp)
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_archive),
                        contentDescription = "archive button icon",
                        modifier = Modifier.size(35.dp)
                    )
                    Text(text = "Abrir gerenciador de arquivos:", Modifier.padding(start = 10.dp))
                }


                Button(
                    onClick = { onNavigateToBarcode() },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(100.dp)
                        .padding(vertical = 8.dp)
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_scan),
                        contentDescription = "scan button icon",
                        modifier = Modifier.size(35.dp)
                    )
                    Text(text = "Scanear NFe:", Modifier.padding(start = 10.dp))
                }

                Button(
                    onClick = { onNavigateAddTaskScreen() },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(100.dp)
                        .padding(vertical = 8.dp)
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_typing),
                        contentDescription = "typing button icon",
                        modifier = Modifier.size(35.dp)
                    )
                    Text(text = "Digitar entrega", Modifier.padding(start = 10.dp))
                }
            }
        }
    }
}
