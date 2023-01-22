package com.example.photoeditor

import android.graphics.Bitmap
import android.graphics.LightingColorFilter
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

}