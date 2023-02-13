package com.example.photoeditor.domain.enums

import androidx.annotation.DrawableRes
import com.example.photoeditor.R

enum class FilterEnum(@DrawableRes val res: Int) {
    SEPIA(R.drawable.android),
    GRAYSCALE(R.drawable.android),
    NEGATIVE(R.drawable.android),
    CYAN(R.drawable.android),
    GRAIN(R.drawable.android),
    CARTOONED(R.drawable.android)
}