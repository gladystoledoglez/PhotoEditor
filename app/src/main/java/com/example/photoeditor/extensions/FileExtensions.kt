package com.example.photoeditor.extensions

import com.example.photoeditor.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun File.saveToFile(bytes: ByteArrayOutputStream) = try {
    if (this.createNewFile()) {
        FileOutputStream(this).apply {
            write(bytes.toByteArray())
            close()
        }
        R.string.image_save_success
    } else {
        R.string.image_save_error
    }
} catch (exception: Exception) {
    R.string.image_save_error
}