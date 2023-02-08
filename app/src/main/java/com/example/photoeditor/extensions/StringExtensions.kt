package com.example.photoeditor.extensions

import android.os.Build
import android.os.Environment
import com.example.photoeditor.BuildConfig
import java.io.File

val String.Companion.EMPTY: String get() = ""

val String.Companion.PNG_MIME_TYPE: String get() = "image/png"

val String.Companion.PNG_FILE_EXT: String get() = "png"

val String.Companion.FILE_PROVIDER: String get() = ".provider"

val String.Companion.FILE_AUTHORITY: String get() = "${BuildConfig.APPLICATION_ID}$FILE_PROVIDER"

fun String.toFile(): File {

    val parentDir = if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    } else {
        File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES)
    }

    val filesDir = File(parentDir, String.EMPTY)

    if (!filesDir.exists()) {
        filesDir.mkdir()
    }

    return File(filesDir, this)
}