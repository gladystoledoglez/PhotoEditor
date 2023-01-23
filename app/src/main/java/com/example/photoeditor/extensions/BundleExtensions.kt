package com.example.photoeditor.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bundle.putBitmap(key: String, bitmap: Bitmap?) {
    val outputStream = ByteArrayOutputStream()
    bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    val value = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    putString(key, value)
}

fun Bundle.getBitmap(key: String): Bitmap {
    val base64EncodeStr = this.getString(key)
    val decodeByteArray = Base64.decode(base64EncodeStr, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodeByteArray, 0, decodeByteArray.size)
}