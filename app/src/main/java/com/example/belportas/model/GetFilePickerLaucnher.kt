package com.example.belportas.model
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun getFilePickerLauncher(
    context: Context,
    taskViewModel: TaskViewModel,
    showFileError: MutableState<Boolean>
): ActivityResultLauncher<String> {
    val xmlConfig = XmlConfig()

    return rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            try {
                context.contentResolver.openInputStream(it)?.use { inputStream ->
                    val xmlString = inputStream.bufferedReader().use { reader -> reader.readText() }
                    xmlString.let { xml ->
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
}
