package com.example.photoeditor.extensions

import android.graphics.Bitmap
import com.example.photoeditor.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun File.writeBitmap(bitmap: Bitmap?) = try {
    this.createNewFile()
    FileOutputStream(this).apply {
        val bytes = ByteArrayOutputStream().getBytesForm(bitmap).toByteArray()
        write(bytes)
        close()
    }
    R.string.image_save_success
} catch (exception: Exception) {
    R.string.image_save_error
}