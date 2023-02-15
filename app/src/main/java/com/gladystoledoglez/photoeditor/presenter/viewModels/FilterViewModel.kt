package com.gladystoledoglez.photoeditor.presenter.viewModels

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photoeditor.domain.enums.FilterEnum
import com.gladystoledoglez.photoeditor.R
import com.gladystoledoglez.photoeditor.domain.models.Filter
import com.gladystoledoglez.photoeditor.extensions.toFilterDrawable
import com.gladystoledoglez.photoeditor.ml.LiteModelCartoonganFp161
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage

class FilterViewModel : ViewModel() {
    private var _image: MutableLiveData<Bitmap?> = MutableLiveData()
    val image: LiveData<Bitmap?> = _image

    private var _cartoonedImage: MutableLiveData<Bitmap?> = MutableLiveData()
    val cartoonedImage: LiveData<Bitmap?> = _cartoonedImage

    private var _filters: MutableLiveData<List<Filter>> = MutableLiveData()
    val filters: LiveData<List<Filter>> = _filters

    init {
        System.loadLibrary("photoeditor")
    }

    fun changeImage(image: Bitmap?) {
        _image.postValue(image)
    }

    fun restoreImage() {
        _image.postValue(image.value)
    }

    fun loadFilters(resources: Resources) {

        val bitmap = _image.value
        val sepiaDrawable = bitmap.toFilterDrawable(resources, getSepiaFilter())
        val grayScaleDrawable = bitmap.toFilterDrawable(resources, getGrayScaleFilter())
        val negativeDrawable = bitmap.toFilterDrawable(resources, getNegativeFilter())
        val cyanDrawable = bitmap.toFilterDrawable(resources, getCyanFilter())
        val grainDrawable = bitmap.toFilterDrawable(resources, getGrainFilter())
        val cartoonedDrawable = resources.let {
            ResourcesCompat.getDrawable(it, R.drawable.cartooned_image, it.newTheme())
        }

        _filters.postValue(
            listOf(
                Filter(sepiaDrawable, FilterEnum.SEPIA.name),
                Filter(grayScaleDrawable, FilterEnum.GRAYSCALE.name),
                Filter(negativeDrawable, FilterEnum.NEGATIVE.name),
                Filter(cyanDrawable, FilterEnum.CYAN.name),
                Filter(grainDrawable, FilterEnum.GRAIN.name),
                Filter(cartoonedDrawable, FilterEnum.CARTOONED.name)
            )
        )
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

    fun setCartoonedImage(model: LiteModelCartoonganFp161?) {
        viewModelScope.launch {
            flowOf(model?.process(TensorImage.fromBitmap(_image.value)))
                .flowOn(Dispatchers.IO)
                .map { it?.cartoonizedImageAsTensorImage?.bitmap }
                .collect { _cartoonedImage.postValue(it) }
        }
    }
}