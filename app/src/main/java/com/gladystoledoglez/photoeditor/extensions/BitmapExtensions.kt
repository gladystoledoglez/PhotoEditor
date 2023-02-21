package com.gladystoledoglez.photoeditor.extensions

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import androidx.core.graphics.drawable.toDrawable
import com.gladystoledoglez.photoeditor.R
import java.io.File

fun Bitmap?.toScale(width: Int?, height: Int?): Bitmap? = this?.let {
    val scaleWidth = width.orZero().toFloat() / it.width
    val scaleHeight = height.orZero().toFloat() / it.height
    val matrix = Matrix().apply { postScale(scaleWidth, scaleHeight) }
    val resized = Bitmap.createBitmap(it, Int.ZERO, Int.ZERO, it.width, it.height, matrix, false)
    recycle()
    return resized
}

fun Bitmap?.toFilterDrawable(resources: Resources, filter: ColorMatrixColorFilter) =
    this?.toDrawable(resources)?.apply { this.colorFilter = filter }

fun Bitmap?.toClearFilterDrawable(resources: Resources) = this?.toDrawable(resources)?.apply {
    this.clearColorFilter()
}

fun Bitmap?.saveFile(fileName: String?, parentDir: File?): Int {
    return fileName?.toFileFrom(parentDir)?.writeBitmap(bitmap = this) ?: R.string.image_save_error
}