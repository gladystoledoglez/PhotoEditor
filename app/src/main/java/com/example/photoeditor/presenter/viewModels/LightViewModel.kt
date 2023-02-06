package com.example.photoeditor.presenter.viewModels

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.LightingColorFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photoeditor.extensions.toHexScale
import com.example.photoeditor.extensions.toValue

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
        val contrast = progress.toValue()
        val matrix = floatArrayOf(
            contrast, zero, zero, zero, brightness,
            zero, contrast, zero, zero, brightness,
            zero, zero, contrast, zero, brightness,
            zero, zero, zero, one, zero
        )
        return ColorMatrixColorFilter(ColorMatrix(matrix))
    }
}