package com.example.photoeditor

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.photoeditor.databinding.FragmentMainBinding
import com.example.photoeditor.extensions.getBitmap
import com.example.photoeditor.extensions.navigate

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val resultLauncher = getActivityResultRegister()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(CROP_IMAGE) { _, bundle ->
            viewModel.changeImage(bundle.getBitmap(CROP_IMAGE))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            btnLoad.setOnClickListener { openAndroidGallery() }
            btnCrop.setOnClickListener { initializeCropFragment(viewModel.image.value) }
            btnLight.setOnClickListener { initializeLightFragment(viewModel.image.value) }
            btnColor.setOnClickListener { initializeColorFragment(viewModel.image.value) }
            btnFilters.setOnClickListener { initializeFilterFragment(viewModel.image.value) }
        }
        with(viewModel) {
            image.observe(viewLifecycleOwner) {
                Glide.with(this@MainFragment).load(it).into(binding.ivGalleryPhoto)
            }
        }
    }

    private fun openAndroidGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private fun loadImage(imageUri: Uri?) {
        val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)
        viewModel.changeImage(bitmap)
    }

    private fun initializeCropFragment(bitmap: Bitmap?) {
        findNavController().navigate(R.id.action_mainFragment_to_cropFragment, CROP_IMAGE, bitmap)
    }

    private fun initializeLightFragment(bitmap: Bitmap?) {
        findNavController().navigate(R.id.action_mainFragment_to_lightFragment, LIGHT_IMAGE, bitmap)
    }

    private fun initializeColorFragment(bitmap: Bitmap?) {
        findNavController().navigate(R.id.action_mainFragment_to_colorFragment, COLOR_IMAGE, bitmap)
    }

    private fun initializeFilterFragment(bitmap: Bitmap?) {
        findNavController().navigate(
            R.id.action_mainFragment_to_filterFragment,
            FILTER_IMAGE,
            bitmap
        )
    }

    private fun getActivityResultRegister(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> loadImage(result.data?.data)
                else -> Toast.makeText(context, R.string.image_load_error, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {
        const val FEATURE_NAME = "feature"
        const val CROP_IMAGE = "image"
        const val LIGHT_IMAGE = "image"
        const val COLOR_IMAGE = "image"
        const val FILTER_IMAGE = "image"
    }
}