package com.gladystoledoglez.photoeditor.presenter.viewModels

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gladystoledoglez.photoeditor.extensions.toValue

class ColorViewModel : ViewModel() {

    private var _image: MutableLiveData<Bitmap?> = MutableLiveData()
    val image: LiveData<Bitmap?> = _image

    fun changeImage(image: Bitmap?) {
        _image.postValue(image)
    }

    fun adjustSaturation(progress: Int): ColorMatrixColorFilter {
        val zero = 0f
        val one = 1f
        val matrix = floatArrayOf(
            one, zero, zero, zero, one,
            zero, one, zero, zero, one,
            zero, zero, one, zero, one,
            zero, zero, zero, one, zero
        )
        val saturation = progress.toValue()
        val colorMatrix = ColorMatrix(matrix).apply { setSaturation(saturation) }
        return ColorMatrixColorFilter(colorMatrix)
    }
}