package com.gladystoledoglez.photoeditor.extensions

import org.tensorflow.lite.support.image.TensorImage

fun TensorImage?.toScale(width: Int?, height: Int?) = this?.bitmap.toScale(width, height)