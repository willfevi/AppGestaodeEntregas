package com.example.belportas.presentation.view

import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.belportas.R
import com.example.belportas.model.BarcodeAnalyser
import java.util.concurrent.Executors

@ExperimentalGetImage
@Stable
@Composable
fun BarcodeScreen(
    onDismiss: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier =Modifier.fillMaxWidth(0.50f)){ Image(
                        painter = painterResource(id = R.drawable.icon_belportas_topbar),
                        contentDescription = "App top bar logo "
                    )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = {padding ->
            Box(modifier = Modifier
                .fillMaxSize(1f)
                .padding(padding)) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        val cameraExecutor = Executors.newSingleThreadExecutor()
                        val previewView = PreviewView(context).also {
                            it.scaleType = PreviewView.ScaleType.FILL_CENTER
                            it.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }

                        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                        cameraProviderFuture.addListener({
                            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                            cameraProvider.unbindAll()

                            val preview = Preview.Builder()
                                .build()
                                .also {
                                    it.setSurfaceProvider(previewView.surfaceProvider)
                                }

                            val imageCapture = ImageCapture.Builder()
                                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY) // set if needed
                                .build()

                            val imageAnalyzer = ImageAnalysis.Builder()
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST) // set if needed
                                .build()
                                .also {
                                    it.setAnalyzer(cameraExecutor, BarcodeAnalyser { barcodeValue ->
                                        Toast.makeText(context, "Barcode found: $barcodeValue", Toast.LENGTH_SHORT).show()
                                    })
                                }

                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                            try {
                                cameraProvider.bindToLifecycle(
                                    context as ComponentActivity, cameraSelector, preview, imageCapture, imageAnalyzer
                                )
                            } catch (exc: Exception) {
                                Log.e("DEBUG", "Use case binding failed", exc)
                            }
                        }, ContextCompat.getMainExecutor(context))
                        previewView
                    }
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(180.dp)
                        .height(550.dp)
                        .border(4.dp, Color.LightGray,shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {Divider(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .height(500.dp)
                        .width(1.dp),
                    color = Color.Red,
                    thickness = 1.dp
                )
                }
            }
        }
    )
}
