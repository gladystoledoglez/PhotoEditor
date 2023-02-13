package com.example.photoeditor.extensions

import com.example.photoeditor.BuildConfig
import java.io.File

val String.Companion.EMPTY: String get() = ""

val String.Companion.PNG_MIME_TYPE: String get() = "image/png"

val String.Companion.PNG_FILE_EXT: String get() = "png"

val String.Companion.FILE_PROVIDER: String get() = ".provider"

val String.Companion.FILE_AUTHORITY: String get() = "${BuildConfig.APPLICATION_ID}$FILE_PROVIDER"

fun String.toFileFrom(parentDir: File?): File {

    val filesDir = File(parentDir, String.EMPTY)

    if (!filesDir.exists()) {
        filesDir.mkdir()
    }

    return File(filesDir, this)
}