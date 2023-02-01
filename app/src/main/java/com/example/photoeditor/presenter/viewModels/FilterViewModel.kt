package com.example.photoeditor.presenter.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photoeditor.domain.models.Filter
import com.example.photoeditor.R

class FilterViewModel : ViewModel() {
    private var _image: MutableLiveData<Bitmap?> = MutableLiveData()
    val image: LiveData<Bitmap?> = _image

    private var _filters: MutableLiveData<List<Filter>> = MutableLiveData()
    val filters: LiveData<List<Filter>> = _filters

    fun changeImage(image: Bitmap?) {
        _image.postValue(image)
    }

    fun getFilters() {
        _filters.postValue(
            listOf(
                Filter(image = R.drawable.android, text = "Natural"),
                Filter(image = R.drawable.android, text = "Natural"),
                Filter(image = R.drawable.android, text = "Natural"),
                Filter(image = R.drawable.android, text = "Natural"),
                Filter(image = R.drawable.android, text = "Natural"),
                Filter(image = R.drawable.android, text = "Natural"),
                Filter(image = R.drawable.android, text = "Natural"),
                Filter(image = R.drawable.android, text = "Natural"),
                Filter(image = R.drawable.android, text = "Natural"),
                Filter(image = R.drawable.android, text = "Natural"),
                Filter(image = R.drawable.android, text = "Natural"),
                Filter(image = R.drawable.android, text = "Natural"),
                Filter(image = R.drawable.android, text = "Natural"),
            )
        )
    }
}