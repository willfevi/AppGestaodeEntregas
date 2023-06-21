package com.example.belportas.presentation.view

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.belportas.R
import com.example.belportas.model.TaskViewModel
import com.example.belportas.model.XmlConfig
import java.io.InputStream

@Composable
fun FileScreen(
    navController: NavHostController,
    onNavigateToBarcode: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateAddTaskScreen: () -> Unit,
    taskViewModel: TaskViewModel
) {
    val context = LocalContext.current
    val xmlConfig = remember { XmlConfig() }
    val showFileError = remember { mutableStateOf(false) }

    val filePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            try {
                    context.contentResolver.openInputStream(it)?.use { inputStream ->
                        val xmlString = inputStream.bufferedReader().use { reader -> reader.readText() }
                        xmlString?.let { xml ->
                            val task = xmlConfig.parseXml(xml)
                            task?.let { parsedTask ->
                                taskViewModel.addTask(parsedTask)
                                showFileError.value = false
                            }
                        }
                    }
                } catch (e: Exception) {
                showFileError.value = true
                if (showFileError.value) {
                    Toast.makeText(context, "Arquivo incompatível! Por favor, selecione um arquivo XML.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val text = buildAnnotatedString {
                        val customGreen = Color(0xFF4CAF50)
                        withStyle(style = SpanStyle(color = Color.White)) {
                            append("App Bel")
                        }
                        withStyle(style = SpanStyle(color = customGreen)) {
                            append("portas")
                        }
                    }
                    Text(text = text)
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
                    try{
                        showFileError.value = false
                        filePickerLauncher.launch("text/xml")

                    }catch (e :Exception){
                        println(e.message)

                    }},
                    modifier = Modifier
                        .widthInFraction(0.8f)
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
                        .widthInFraction(0.8f)
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
                        .widthInFraction(0.8f)
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
