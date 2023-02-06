package com.example.photoeditor.presenter.fragments

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.photoeditor.extensions.putBitmap

abstract class BaseFragment : Fragment() {

    protected fun saveImage(origin: String, bitmap: Bitmap?) {
        val bundle = Bundle().apply { putBitmap(origin, bitmap) }
        setFragmentResult(origin, bundle)
        activity?.onBackPressedDispatcher?.onBackPressed()
    }
}