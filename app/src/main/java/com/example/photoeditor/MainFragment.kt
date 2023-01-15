package com.example.photoeditor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.photoeditor.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> loadImage(result.data?.dataString)
                else -> Toast.makeText(context, R.string.image_load_error, Toast.LENGTH_SHORT)
                    .show()
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
            btnCrop.setOnClickListener { initializeFeatureFragment(btnCrop.text.toString()) }
            btnLight.setOnClickListener { initializeFeatureFragment(btnLight.text.toString()) }
            btnColor.setOnClickListener { initializeFeatureFragment(btnColor.text.toString()) }
            btnFilters.setOnClickListener { initializeFeatureFragment(btnFilters.text.toString()) }
        }
    }

    private fun openAndroidGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private fun loadImage(image: String?) {
        Glide.with(this)
            .load(image)
            .into(binding.ivGalleryPhoto)
    }

    private fun initializeFeatureFragment(btnText: String) {
        val bundle = Bundle().apply { putString(featureName, btnText) }
        findNavController().navigate(R.id.action_mainFragment_to_featureFragment, bundle)
    }

    companion object {
        const val featureName = "feature"
    }
}