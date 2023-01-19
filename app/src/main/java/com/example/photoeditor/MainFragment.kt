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

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val resultLauncher = getActivityResultRegister()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(cropImage) { _, bundle ->
            viewModel.changeImage(bundle.getBitmap(cropImage))
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
            btnColor.setOnClickListener { initializeFeatureFragment(btnColor.text.toString()) }
            btnFilters.setOnClickListener { initializeFeatureFragment(btnFilters.text.toString()) }
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

    private fun initializeFeatureFragment(btnText: String) {
        val bundle = Bundle().apply { putString(featureName, btnText) }
        findNavController().navigate(R.id.action_mainFragment_to_featureFragment, bundle)
    }

    private fun initializeCropFragment(bitmap: Bitmap?) {
        val bundle = Bundle().apply { putBitmap(cropImage, bitmap) }
        findNavController().navigate(R.id.action_mainFragment_to_cropFragment, bundle)
    }

    private fun initializeLightFragment(bitmap: Bitmap?) {
        val bundle = Bundle().apply { putBitmap(lightImage, bitmap) }
        findNavController().navigate(R.id.action_mainFragment_to_lightFragment, bundle)
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
        const val featureName = "feature"
        const val cropImage = "image"
        const val lightImage = "image"
    }
}