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
import com.gladystoledoglez.photoeditor.domain.enums.Filters
import com.gladystoledoglez.photoeditor.domain.models.FilterModel
import com.gladystoledoglez.photoeditor.extensions.toFilterDrawable
import com.gladystoledoglez.photoeditor.extensions.toScale
import com.gladystoledoglez.photoeditor.ml.LiteModelCartoonganFp161
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage

class FilterViewModel : ViewModel() {
    private var _image: MutableLiveData<Bitmap?> = MutableLiveData()
    val image: LiveData<Bitmap?> = _image
    val bitmap: Bitmap? get() = _image.value

    private var _cartoonedImage: MutableLiveData<Bitmap?> = MutableLiveData()
    val cartoonedImage: LiveData<Bitmap?> = _cartoonedImage

    private var _isProcessing: MutableLiveData<Boolean?> = MutableLiveData(false)
    val isProcessing: LiveData<Boolean?> = _isProcessing

    private var _filters: MutableLiveData<List<FilterModel>> = MutableLiveData()
    val filters: LiveData<List<FilterModel>> = _filters

    init {
        System.loadLibrary("photoeditor")
    }

    private suspend fun getCartoonedBitmap(
        cartoonedModel: LiteModelCartoonganFp161?, bitmap: Bitmap?
    ) = flow { emit(cartoonedModel?.process(TensorImage.fromBitmap(bitmap))) }
        .flowOn(Dispatchers.IO)
        .onStart { _isProcessing.postValue(true) }
        .onCompletion { _isProcessing.postValue(false) }
        .map { it?.cartoonizedImageAsTensorImage?.toScale(bitmap?.width, bitmap?.height) }
        .firstOrNull()

    private suspend fun getFilterDrawableBy(
        cartoonedModel: LiteModelCartoonganFp161?, res: Resources, ordinal: Int
    ): Drawable? {
        return when (ordinal) {
            Filters.SEPIA.ordinal -> bitmap.toFilterDrawable(res, getSepiaFilter())
            Filters.GRAYSCALE.ordinal -> bitmap.toFilterDrawable(res, getGrayScaleFilter())
            Filters.NEGATIVE.ordinal -> bitmap.toFilterDrawable(res, getNegativeFilter())
            Filters.CYAN.ordinal -> bitmap.toFilterDrawable(res, getCyanFilter())
            Filters.GRAIN.ordinal -> bitmap.toFilterDrawable(res, getGrainFilter())
            else -> getCartoonedBitmap(cartoonedModel, bitmap)?.toDrawable(res)
        }
    }

    fun changeImage(image: Bitmap?) {
        _image.postValue(image)
    }

    fun restoreImage() {
        _image.postValue(image.value)
    }

    fun loadFilters(cartoonedModel: LiteModelCartoonganFp161?, resources: Resources) {
        viewModelScope.launch {
            val filters = Filters.values().map {
                val drawable = getFilterDrawableBy(cartoonedModel, resources, it.ordinal)
                FilterModel(it.name, drawable)
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
        viewModelScope.launch {
            _cartoonedImage.postValue(getCartoonedBitmap(cartoonedModel, bitmap))
        }
    }
}