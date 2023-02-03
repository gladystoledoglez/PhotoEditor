package com.example.photoeditor.presenter.viewModels

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photoeditor.domain.enums.FilterEnum
import com.example.photoeditor.domain.models.Filter

class FilterViewModel : ViewModel() {
    private var _image: MutableLiveData<Bitmap?> = MutableLiveData()
    val image: LiveData<Bitmap?> = _image

    private var _filters: MutableLiveData<List<Filter>> = MutableLiveData()
    val filters: LiveData<List<Filter>> = _filters

    init {
        System.loadLibrary("photoeditor")
    }

    fun changeImage(image: Bitmap?) {
        _image.postValue(image)
    }

    fun getFilters() {
        _filters.postValue(
            listOf(
                Filter(FilterEnum.SEPIA.res, FilterEnum.SEPIA.name),
                Filter(FilterEnum.GRAYSCALE.res, FilterEnum.GRAYSCALE.name),
                Filter(FilterEnum.NEGATIVE.res, FilterEnum.NEGATIVE.name),
                Filter(FilterEnum.CYAN.res, FilterEnum.CYAN.name),
                Filter(FilterEnum.GRAIN.res, FilterEnum.GRAIN.name)
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
}