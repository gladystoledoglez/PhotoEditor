package com.gladystoledoglez.photoeditor.extensions

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ColorMatrixColorFilter
import androidx.core.graphics.drawable.toDrawable
import com.gladystoledoglez.photoeditor.R
import java.io.File

fun Bitmap?.toFilterDrawable(resources: Resources, filter: ColorMatrixColorFilter) =
    this?.toDrawable(resources)?.apply { this.colorFilter = filter }

fun Bitmap?.saveFile(fileName: String?, parentDir: File?): Int {
    return fileName?.toFileFrom(parentDir)?.writeBitmap(bitmap = this) ?: R.string.image_save_error
}