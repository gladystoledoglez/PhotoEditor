package com.example.photoeditor.extensions

import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.model.Model

fun Model.Options.Builder.buildOptions(): Model.Options =
    if (CompatibilityList().isDelegateSupportedOnThisDevice) {
        setDevice(Model.Device.GPU)
    } else {
        setNumThreads(4)
    }.build()