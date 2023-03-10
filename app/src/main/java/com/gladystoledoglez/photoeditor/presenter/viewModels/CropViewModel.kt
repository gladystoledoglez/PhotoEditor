package com.gladystoledoglez.photoeditor.presenter.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CropViewModel : ViewModel() {

    private var _image: MutableLiveData<Bitmap?> = MutableLiveData()
    val image: LiveData<Bitmap?> = _image

    fun changeImage(image: Bitmap?) {
        _image.postValue(image)
    }
}