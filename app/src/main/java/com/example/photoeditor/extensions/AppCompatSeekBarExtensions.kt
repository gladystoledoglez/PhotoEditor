package com.example.photoeditor.extensions

import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar

fun AppCompatSeekBar.setOnSeekBarProgressChanged(
    onProgressChanged: (bar: SeekBar?, progress: Int, p2: Boolean) -> Unit
) = setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(bar: SeekBar?, progress: Int, p2: Boolean) {
        onProgressChanged(bar, progress, p2)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        // do nothing
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        // do nothing
    }

})