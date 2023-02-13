package com.example.photoeditor.extensions

import android.graphics.Bitmap
import com.example.photoeditor.R
import java.io.File

fun Bitmap?.saveFile(fileName: String?, parentDir: File?): Int {
    return fileName?.toFileFrom(parentDir)?.writeBitmap(bitmap = this) ?: R.string.image_save_error
}