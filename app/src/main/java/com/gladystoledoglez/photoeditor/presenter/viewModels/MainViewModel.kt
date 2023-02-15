package com.gladystoledoglez.photoeditor.presenter.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gladystoledoglez.photoeditor.extensions.PNG_FILE_EXT

class MainViewModel : ViewModel() {

    private var _image: MutableLiveData<Bitmap?> = MutableLiveData()
    val image: LiveData<Bitmap?> = _image

    private var _fileName: MutableLiveData<String?> = MutableLiveData()
    val fileName: LiveData<String?> = _fileName

    fun changeImage(image: Bitmap?) {
        _image.postValue(image)
    }

    fun changeFileName(fileTagName: String?) {
        _fileName.value = "${fileTagName}Image.${String.PNG_FILE_EXT}"
    }
}