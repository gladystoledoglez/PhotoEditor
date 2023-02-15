package com.gladystoledoglez.photoeditor.extensions

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle

fun Bundle.putBitmap(key: String, bitmap: Bitmap?) {
    putParcelable(key, bitmap)
}

fun Bundle.getBitmap(key: String) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    getParcelable(key, Bitmap::class.java)
} else {
    getParcelable(key)
}