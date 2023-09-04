package com.example.belportas.model

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import com.example.belportas.data.Task
import java.io.File
import java.io.FileInputStream

class ConfirmImage {

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    private var currentPhotoPath: String? = null

    fun startCameraIntent(activity: Activity, launcher: ActivityResultLauncher<Uri>, task: Task) {
        val photoFile: File = createImageFile(activity)
        task.imagePath = photoFile.absolutePath
        val photoURI = FileProvider.getUriForFile(activity, "${activity.packageName}.fileprovider", photoFile)
        launcher.launch(photoURI)
    }
    private fun createImageFile(context: Context): File {
        val filename = "TEMP_IMG.jpg"
        val storageDir = context.filesDir
        return File(storageDir, filename).apply {
            currentPhotoPath = absolutePath
        }
    }

    @SuppressLint("InlinedApi")
    fun confirmAndSaveImage(context: Context, taskNoteNumber: String): Uri? {
        val bitmap: Bitmap = BitmapFactory.decodeFile(currentPhotoPath)
        return saveBitmapToMediaStore(context, bitmap, taskNoteNumber)
    }

    @SuppressLint("InlinedApi")
    private fun saveBitmapToMediaStore(context: Context, bitmap: Bitmap, taskNoteNumber: String): Uri? {
        val displayName = "IMG_$taskNoteNumber.jpg"
        val resolver = context.contentResolver
        val imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        var uri: Uri? = null
        try {
            uri = resolver.insert(imageCollection, contentValues)
            uri?.let {
                resolver.openOutputStream(it).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
            }
        } catch (e: Exception) {
            if (uri != null) {
                resolver.delete(uri, null, null)
            }
        }

        return uri
    }
    fun loadImageFromInternalStorage(context: Context, filename: String): Bitmap? {
        return try {
            val completePath = File(context.filesDir, filename).absolutePath
            val fis = FileInputStream(completePath)
            val bitmap = BitmapFactory.decodeStream(fis)
            fis.close()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}




