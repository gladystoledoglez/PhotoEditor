package com.example.photoeditor

import android.graphics.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LightViewModel : ViewModel() {

    private var _image: MutableLiveData<Bitmap?> = MutableLiveData()
    val image: LiveData<Bitmap?> = _image

    fun changeImage(image: Bitmap?) {
        _image.postValue(image)
    }

    fun adjustBrightness(progress: Int): LightingColorFilter {
        val mul = 0XFFFFFF
        val hex = progress.toHexScale()
        val initialHex = "0X$hex$hex$hex"
        val add = Integer.decode(initialHex)
        return LightingColorFilter(mul, add)
    }

    fun adjustContrast(progress: Int, brightness: Float = 1f): ColorMatrixColorFilter {
        val zero = 0f
        val one = 1f
        val oneHundred = 100f
        val contrast = progress.toFloat() / oneHundred
        val matrix = floatArrayOf(
            contrast, zero, zero, zero, brightness,
            zero, contrast, zero, zero, brightness,
            zero, zero, contrast, zero, brightness,
            zero, zero, zero, one, zero
        )
        return ColorMatrixColorFilter(ColorMatrix(matrix))
    }
}