package com.gladystoledoglez.photoeditor.presenter.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.gladystoledoglez.photoeditor.R
import com.gladystoledoglez.photoeditor.databinding.FragmentMainBinding
import com.gladystoledoglez.photoeditor.domain.enums.Tabs
import com.gladystoledoglez.photoeditor.extensions.*
import com.gladystoledoglez.photoeditor.presenter.viewModels.MainViewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val resultLauncher = getActivityResultRegister()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(CropFragment.TAG) { key, bundle -> handleResult(key, bundle) }
        setFragmentResultListener(LightFragment.TAG) { key, bundle -> handleResult(key, bundle) }
        setFragmentResultListener(ColorFragment.TAG) { key, bundle -> handleResult(key, bundle) }
        setFragmentResultListener(FilterFragment.TAG) { key, bundle -> handleResult(key, bundle) }
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
        setupFooter()
        setupObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.showMenuItem(R.id.actionLoad)
        viewModel.image.value?.let {
            menu.showMenuItems(R.id.actionSave, R.id.actionShare)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.actionLoad -> {
            openAndroidGallery()
            true
        }
        R.id.actionSave -> {
            viewModel.apply {
                val imageResString = image.value.saveFile(fileName.value, context?.filesDir)
                Toast.makeText(context, imageResString, Toast.LENGTH_SHORT).show()
            }
            true
        }
        R.id.actionShare -> {
            viewModel.apply {
                image.value.saveFile(fileName.value, context?.filesDir)
                shareImage(fileName.value)
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
        changeFileBitmap(TAG, bitmap)
    }

    private fun setupFooter() {
        with(binding.tlFooter) {
            Tabs.values().map { addTab(newTab().apply { text = it.name }) }
            addOnTabSelectedListener(onTabSelectedListener {
                findNavController().navigate(it.getFragmentId(), IMAGE, viewModel.image.value)
            })
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            image.observe(viewLifecycleOwner) {
                Glide.with(this@MainFragment).load(it).into(binding.ivGalleryPhoto)
                binding.apply {
                    ivGalleryPhoto.isVisible = true
                    tvLoadImage.isVisible = false
                }
            }
        }
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

    private fun shareImage(fileName: String?) {
        val file = fileName?.toFileFrom(context?.filesDir)
        val uri = context?.getUriFromFile(file)
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = String.PNG_MIME_TYPE
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val shareChooserTitle = getString(R.string.choose_share_option_message)
        startActivity(Intent.createChooser(shareIntent, shareChooserTitle))
    }

    private fun handleResult(key: String, bundle: Bundle) {
        changeFileBitmap(key, bundle.getBitmap(key))
    }

    private fun changeFileBitmap(fileName: String?, bitmap: Bitmap?) {
        viewModel.changeFileName(fileName)
        viewModel.changeImage(bitmap)
    }

    companion object {
        const val IMAGE = "image"
        val TAG = MainFragment::class.simpleName
    }
}