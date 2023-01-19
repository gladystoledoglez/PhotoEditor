package com.example.photoeditor

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.example.photoeditor.databinding.FragmentCropBinding

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
        viewModel.changeImage(arguments?.getBitmap(MainFragment.cropImage))
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
        val bundle = Bundle().apply { putBitmap(MainFragment.cropImage, bitmap) }
        setFragmentResult(MainFragment.cropImage, bundle)
        activity?.onBackPressedDispatcher?.onBackPressed()
    }
}