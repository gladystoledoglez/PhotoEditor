package com.gladystoledoglez.photoeditor.extensions

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.gladystoledoglez.photoeditor.ml.LiteModelCartoonganFp161
import org.tensorflow.lite.support.model.Model
import java.io.File

fun Context.toLiteModelCartoonedFp161(): LiteModelCartoonganFp161 {
    return LiteModelCartoonganFp161.newInstance(this, Model.Options.Builder().buildOptions())
}

fun Context.getUriFromFile(file: File?): Uri? {
    return file?.let { FileProvider.getUriForFile(this, String.FILE_AUTHORITY, it) }
}