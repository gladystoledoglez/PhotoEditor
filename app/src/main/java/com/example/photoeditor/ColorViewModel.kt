package com.example.photoeditor

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ColorViewModel : ViewModel() {

    private var _image: MutableLiveData<Bitmap?> = MutableLiveData()
    val image: LiveData<Bitmap?> = _image

    fun changeImage(image: Bitmap?) {
        _image.postValue(image)
    }

    fun adjustSaturation(progress: Int): ColorMatrixColorFilter {
        val zero = 0f
        val one = 1f
        val oneHundred = 100f
        val matrix = floatArrayOf(
            one, zero, zero, zero, one,
            zero, one, zero, zero, one,
            zero, zero, one, zero, one,
            zero, zero, zero, one, zero
        )
        val saturation = progress.toFloat() / oneHundred
        val colorMatrix = ColorMatrix(matrix).apply { setSaturation(saturation) }
        return ColorMatrixColorFilter(colorMatrix)
    }
}