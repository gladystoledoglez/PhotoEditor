package com.example.photoeditor.extensions

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File

fun Bitmap?.saveToFile(context: Context?) {

    val child = ""
    val fileExt = Bitmap.CompressFormat.PNG
    val fileName = "IMAGE_${System.currentTimeMillis()}.$fileExt"
    val filesDir = File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), child)

    if (!filesDir.exists()) {
        filesDir.mkdir()
    }

    val bytes = ByteArrayOutputStream()
    this?.compress(fileExt, 90, bytes)
    val imageResString = File(filesDir, fileName).saveToFile(bytes)
    Toast.makeText(context, imageResString, Toast.LENGTH_SHORT).show()
}

private fun getExternalStoragePublicDirectory(type: String) = if (isQuinceTartVersion()) {
    Environment.getExternalStoragePublicDirectory(type)
} else {
    File(Environment.getExternalStorageDirectory(), type)
}

private fun isQuinceTartVersion() = Build.VERSION.SDK_INT == Build.VERSION_CODES.Q