package com.example.photoeditor.extensions

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun ByteArrayOutputStream.getBytesForm(bitmap: Bitmap?) = this.also {
    bitmap?.compress(Bitmap.CompressFormat.PNG, Int.PNG_HIGH_QUALITY, it)
}