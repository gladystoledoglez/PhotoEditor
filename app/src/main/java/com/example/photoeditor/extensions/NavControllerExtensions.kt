package com.example.photoeditor.extensions

import android.graphics.Bitmap
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController

fun NavController.navigate(@IdRes resId: Int, key: String, bitmap: Bitmap?) {
    navigate(resId, Bundle().apply { putBitmap(key, bitmap) })
}