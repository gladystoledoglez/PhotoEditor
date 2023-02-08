package com.example.photoeditor.extensions

import android.graphics.Bitmap

fun Bitmap?.saveFile(fileName: String): Int {
    return fileName.toFile().writeBitmap(bitmap = this)
}