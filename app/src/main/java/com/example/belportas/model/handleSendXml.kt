@file:Suppress("DEPRECATION")
package com.example.belportas.model

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ExperimentalGetImage
import com.example.belportas.presentation.view.MainActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
@ExperimentalGetImage
@ExperimentalPermissionsApi
fun handleSendXml(context: Context, intent: Intent?, taskViewModel: TaskViewModel) {
    intent?.let {
        when (it.action) {
            Intent.ACTION_SEND -> handleSingleFileSharing(context, it, taskViewModel)
            Intent.ACTION_SEND_MULTIPLE -> handleMultipleFileSharing(context, it, taskViewModel)
        }
    }
}
@ExperimentalGetImage
@ExperimentalPermissionsApi
private fun handleSingleFileSharing(context: Context, intent: Intent, taskViewModel: TaskViewModel) {
    if (intent.type == "text/xml") {
        val xmlFile: Uri? = intent.getParcelableExtra(Intent.EXTRA_STREAM)
        xmlFile?.let {
            processXmlFile(context, it, taskViewModel)
            navigateToMainActivity(context, intent)
        } ?: showToast(context, "Nenhum arquivo XML recebido.")
    }
}
@ExperimentalGetImage
@ExperimentalPermissionsApi
private fun handleMultipleFileSharing(context: Context, intent: Intent, taskViewModel: TaskViewModel) {
    if (intent.type == "text/xml") {
        val xmlFiles: ArrayList<Uri>? = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
        xmlFiles?.let { uris ->
            uris.forEach { processXmlFile(context, it, taskViewModel) }
            navigateToMainActivity(context, intent)
        } ?: showToast(context, "Nenhum arquivo XML recebido.")
    }
}
private fun processXmlFile(context: Context, uri: Uri, taskViewModel: TaskViewModel) {
    try {
        val xml = context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.bufferedReader().use { reader ->
                reader.readText()
            }
        }
        xml?.let {
            val task = XmlConfig().parseXml(it)
            task?.let { parsedTask ->
                taskViewModel.addTaskExternal(parsedTask)
            } ?: showToast(context, "Falha ao converter XML para objeto Tarefa.")
        } ?: showToast(context, "Falha ao ler arquivo XML.")
    } catch (e: Exception) {
        Log.e("TaskSharing", "Erro ao manipular arquivo XML: ${e.message}")
        showToast(context, "Erro ao manipular arquivo XML.")
    }
}
@ExperimentalGetImage
@ExperimentalPermissionsApi
private fun navigateToMainActivity(context: Context, intent: Intent) {
    val mainActivityIntent = Intent(context, MainActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        putExtras(intent)
    }
    context.startActivity(mainActivityIntent)
}
private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
