package com.gladystoledoglez.photoeditor.presenter.viewModels

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gladystoledoglez.photoeditor.domain.enums.FilterEnum
import com.gladystoledoglez.photoeditor.domain.models.Filter
import com.gladystoledoglez.photoeditor.extensions.toFilterDrawable
import com.gladystoledoglez.photoeditor.ml.LiteModelCartoonganFp161
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage

class FilterViewModel : ViewModel() {
    private var _image: MutableLiveData<Bitmap?> = MutableLiveData()
    val image: LiveData<Bitmap?> = _image

    private var _cartoonedImage: MutableLiveData<Bitmap?> = MutableLiveData()
    val cartoonedImage: LiveData<Bitmap?> = _cartoonedImage

    private var _isProcessing: MutableLiveData<Boolean?> = MutableLiveData(false)
    val isProcessing: LiveData<Boolean?> = _isProcessing

    private var _filters: MutableLiveData<List<Filter>> = MutableLiveData()
    val filters: LiveData<List<Filter>> = _filters

    init {
        System.loadLibrary("photoeditor")
    }

    private suspend fun getCartoonedBitmapFrom(cartoonedModel: LiteModelCartoonganFp161?): Bitmap? {
        return flowOf(cartoonedModel?.process(TensorImage.fromBitmap(_image.value)))
            .flowOn(Dispatchers.IO)
            .onStart { _isProcessing.postValue(true) }
            .onCompletion { _isProcessing.postValue(false) }
            .map { it?.cartoonizedImageAsTensorImage?.bitmap }
            .firstOrNull()
    }

    private suspend fun getFilterDrawableBy(
        cartoonedModel: LiteModelCartoonganFp161?,
        res: Resources,
        ordinal: Int
    ): Drawable? =
        when (ordinal) {
            FilterEnum.SEPIA.ordinal -> _image.value.toFilterDrawable(res, getSepiaFilter())
            FilterEnum.GRAYSCALE.ordinal -> _image.value.toFilterDrawable(res, getGrayScaleFilter())
            FilterEnum.NEGATIVE.ordinal -> _image.value.toFilterDrawable(res, getNegativeFilter())
            FilterEnum.CYAN.ordinal -> _image.value.toFilterDrawable(res, getCyanFilter())
            FilterEnum.GRAIN.ordinal -> _image.value.toFilterDrawable(res, getGrainFilter())
            else -> getCartoonedBitmapFrom(cartoonedModel)?.toDrawable(res)
        }

    fun changeImage(image: Bitmap?) {
        _image.postValue(image)
    }

    fun restoreImage() {
        _image.postValue(image.value)
    }

    fun loadFilters(cartoonedModel: LiteModelCartoonganFp161?, resources: Resources) {
        viewModelScope.launch {
            val filters = FilterEnum.values().map {
                Filter(it.name, getFilterDrawableBy(cartoonedModel, resources, it.ordinal))
            }
            _filters.postValue(filters)
        }
    }

    fun getSepiaFilter(): ColorMatrixColorFilter {
        val firstColorMatrix = ColorMatrix()
        val secondColorMatrix = ColorMatrix()
        firstColorMatrix.setSaturation(0f)
        secondColorMatrix.setScale(1f, .95f, .82f, 1.0f)
        firstColorMatrix.setConcat(secondColorMatrix, firstColorMatrix)

        return ColorMatrixColorFilter(firstColorMatrix)
    }

    fun getGrayScaleFilter(): ColorMatrixColorFilter {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)

        return ColorMatrixColorFilter(matrix)
    }

    fun getNegativeFilter(): ColorMatrixColorFilter {
        val lessOne = -1f
        val zero = 0f
        val one = 1f
        val brightness = 255f
        val matrix = floatArrayOf(
            lessOne, zero, zero, zero, brightness,
            zero, lessOne, zero, zero, brightness,
            zero, zero, lessOne, zero, brightness,
            zero, zero, zero, one, zero
        )
        return ColorMatrixColorFilter(ColorMatrix(matrix))
    }

    external fun getCyanFilter(): ColorMatrixColorFilter

    external fun getGrainFilter(): ColorMatrixColorFilter

    fun setCartoonedImage(cartoonedModel: LiteModelCartoonganFp161?) {
        viewModelScope.launch { _cartoonedImage.postValue(getCartoonedBitmapFrom(cartoonedModel)) }
    }
}