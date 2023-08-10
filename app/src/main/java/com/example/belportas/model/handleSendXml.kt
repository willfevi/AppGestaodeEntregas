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
        val action = it.action
        val type = it.type
        if (Intent.ACTION_SEND == action && type != null && "text/xml" == type) {
            val xmlFile: Uri? = it.getParcelableExtra(Intent.EXTRA_STREAM)
            val xmlParser = XmlConfig()

            xmlFile?.let { uri ->
                try {
                    val xml = context.contentResolver.openInputStream(uri)?.bufferedReader().use { reader -> reader?.readText() }
                    if (xml != null) {
                        val task = xmlParser.parseXml(xml)
                        if (task != null) {
                            taskViewModel.addTaskExternal(task)
                            Toast.makeText(context, "Tarefa adicionada com sucesso à lista.", Toast.LENGTH_SHORT).show()

                            val mainActivityIntent = Intent(context, MainActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                putExtras(it)
                            }
                            context.startActivity(mainActivityIntent)
                        } else {
                            Toast.makeText(context, "Falha ao converter XML para objeto Tarefa.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Falha ao ler arquivo XML.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("TaskSharing", "Erro ao manipular arquivo XML: ${e.message}")
                    Toast.makeText(context, "Erro ao manipular arquivo XML.", Toast.LENGTH_SHORT).show()
                }
            } ?: Toast.makeText(context, "Nenhum arquivo XML recebido.", Toast.LENGTH_SHORT).show()
        }
        if (Intent.ACTION_SEND_MULTIPLE == action && type != null && "text/xml" == type) {
            val xmlFiles: ArrayList<Uri>? = it.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
            val xmlParser = XmlConfig()
            xmlFiles?.let { uris ->
                for (uri in uris) {
                    try {
                        val xml = context.contentResolver.openInputStream(uri)?.bufferedReader().use { reader -> reader?.readText() }
                        if (xml != null) {
                            val task = xmlParser.parseXml(xml)
                            if (task != null) {
                                taskViewModel.addTaskExternal(task)
                                Toast.makeText(context, "Tarefa adicionada com sucesso à lista.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Falha ao converter XML para objeto Tarefa.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Falha ao ler arquivo XML.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("TaskSharing", "Erro ao manipular arquivo XML: ${e.message}")
                        Toast.makeText(context, "Erro ao manipular arquivo XML.", Toast.LENGTH_SHORT).show()
                    }
                }
                val mainActivityIntent = Intent(context, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    putExtras(it)
                }
                context.startActivity(mainActivityIntent)
            } ?: Toast.makeText(context, "Nenhum arquivo XML recebido.", Toast.LENGTH_SHORT).show()
        }
    }
}

