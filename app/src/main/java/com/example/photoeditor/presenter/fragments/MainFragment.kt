package com.example.photoeditor.presenter.fragments

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.photoeditor.R
import com.example.photoeditor.databinding.FragmentMainBinding
import com.example.photoeditor.extensions.*
import com.example.photoeditor.presenter.viewModels.MainViewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val resultLauncher = getActivityResultRegister()
    private val permissionLauncher = getActivityRequestPermission()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(CropFragment.TAG) { _, bundle ->
            viewModel.changeFileName(CropFragment.TAG)
            viewModel.changeImage(bundle.getBitmap(CropFragment.TAG))
        }
        setFragmentResultListener(LightFragment.TAG) { _, bundle ->
            viewModel.changeFileName(LightFragment.TAG)
            viewModel.changeImage(bundle.getBitmap(LightFragment.TAG))
        }
        setFragmentResultListener(ColorFragment.TAG) { _, bundle ->
            viewModel.changeFileName(ColorFragment.TAG)
            viewModel.changeImage(bundle.getBitmap(ColorFragment.TAG))
        }
        setFragmentResultListener(FilterFragment.TAG) { _, bundle ->
            viewModel.changeFileName(FilterFragment.TAG)
            viewModel.changeImage(bundle.getBitmap(FilterFragment.TAG))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        permissionSetup()
        with(binding) {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.showMenuItems(intArrayOf(R.id.actionLoad, R.id.actionSave, R.id.actionShare))
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.actionLoad -> {
            openAndroidGallery()
            true
        }
        R.id.actionSave -> {
            viewModel.apply {
                val imageResString = image.value.saveFile(fileName.value.orEmpty())
                Toast.makeText(context, imageResString, Toast.LENGTH_SHORT).show()
            }
            true
        }
        R.id.actionShare -> {
            viewModel.apply {
                val imageFileName = fileName.value.orEmpty()
                image.value.saveFile(imageFileName)
                shareImage(imageFileName)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun openAndroidGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private fun loadImage(imageUri: Uri?) {
        val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)
        viewModel.changeFileName(TAG)
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

    private fun getActivityRequestPermission(): ActivityResultLauncher<String> {
        return registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d(TAG, getString(R.string.permission_granted_message))
            } else {
                Log.d(TAG, getString(R.string.permission_request_message))
            }
        }
    }

    private fun permissionSetup() {
        val permission = ContextCompat.checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(WRITE_EXTERNAL_STORAGE)
        } else {
            Log.d(TAG, getString(R.string.permission_granted_message))
        }
    }

    private fun shareImage(fileName: String) {
        val file = fileName.toFile()
        val uri = FileProvider.getUriForFile(requireContext(), String.FILE_AUTHORITY, file)
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = String.PNG_MIME_TYPE
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val shareChooserTitle = getString(R.string.choose_share_option_message)
        startActivity(Intent.createChooser(shareIntent, shareChooserTitle))
    }

    companion object {
        const val FEATURE_NAME = "feature"
        const val CROP_IMAGE = "image"
        const val LIGHT_IMAGE = "image"
        const val COLOR_IMAGE = "image"
        const val FILTER_IMAGE = "image"
        val TAG = MainFragment::class.simpleName
    }
}