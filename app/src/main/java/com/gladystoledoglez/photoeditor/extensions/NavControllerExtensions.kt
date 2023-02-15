package com.gladystoledoglez.photoeditor.extensions

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.navigation.NavController
import com.gladystoledoglez.photoeditor.R

fun NavController.navigate(@IdRes resId: Int, key: String, bitmap: Bitmap?) {
    bitmap?.let {
        navigate(resId, Bundle().apply { putBitmap(key, bitmap) })
    } ?: run {
        Toast.makeText(context, R.string.image_empty_message, Toast.LENGTH_SHORT).show()
    }
}