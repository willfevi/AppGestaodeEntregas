package com.example.belportas.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class ConfirmImage {

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    private var currentPhotoPath: String? = null

    fun startCameraIntent(activity: Activity) {
        Log.d("CameraDebug", "startCameraIntent function entered!")
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            val photoFile: File = createImageFile(activity)
            val photoURI = FileProvider.getUriForFile(activity, "${activity.packageName}.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }
    fun simpleCameraIntent(activity: Activity) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, onImageCaptured: (Bitmap?) -> Unit) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = BitmapFactory.decodeFile(currentPhotoPath)
            onImageCaptured(imageBitmap)
        }
    }


    private fun createImageFile(context: Context): File {
        val filename = "TEMP_IMG.jpg"
        val storageDir = context.filesDir
        return File(storageDir, filename).apply {
            currentPhotoPath = absolutePath
        }
    }

    fun confirmAndSaveImage(context: Context, taskId: Long): String? {
        val bitmap: Bitmap = BitmapFactory.decodeFile(currentPhotoPath)
        return saveImageToInternalStorage(context, bitmap, taskId)
    }

    private fun saveImageToInternalStorage(context: Context, bitmap: Bitmap, taskId: Long): String? {
        return try {
            val filename = "IMG_$taskId.jpg"
            val storageDir = File(context.filesDir, "Bel imagens")
            storageDir.mkdirs() // Cria o diretório se não existir
            val imageFile = File(storageDir, filename)
            val fos = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            filename
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    fun loadImageFromInternalStorage(context: Context, filename: String): Bitmap? {
        return try {
            val fis = context.openFileInput(filename)
            BitmapFactory.decodeStream(fis)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

