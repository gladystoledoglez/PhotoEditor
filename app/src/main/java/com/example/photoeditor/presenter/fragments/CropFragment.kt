package com.example.photoeditor.presenter.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.example.photoeditor.databinding.FragmentCropBinding
import com.example.photoeditor.extensions.getBitmap
import com.example.photoeditor.extensions.putBitmap
import com.example.photoeditor.presenter.viewModels.CropViewModel

class CropFragment : Fragment() {
    private lateinit var binding: FragmentCropBinding
    private val viewModel: CropViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCropBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.changeImage(arguments?.getBitmap(MainFragment.CROP_IMAGE))
        with(viewModel) {
            image.observe(viewLifecycleOwner) { binding.cropImageView.setImageBitmap(it) }
        }
        with(binding) {
            btnRotate.setOnClickListener { cropImageView.rotateImage(degrees = 90) }
            btnSave.setOnClickListener { saveCroppedImage(cropImageView.getCroppedImage()) }
        }
    }

    private fun saveCroppedImage(bitmap: Bitmap?) {
        viewModel.changeImage(bitmap)
        val bundle = Bundle().apply { putBitmap(MainFragment.CROP_IMAGE, bitmap) }
        setFragmentResult(MainFragment.CROP_IMAGE, bundle)
        activity?.onBackPressedDispatcher?.onBackPressed()
    }
}