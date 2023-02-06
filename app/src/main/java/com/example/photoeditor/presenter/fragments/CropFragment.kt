package com.example.photoeditor.presenter.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import com.example.photoeditor.R
import com.example.photoeditor.databinding.FragmentCropBinding
import com.example.photoeditor.extensions.getBitmap
import com.example.photoeditor.extensions.showAllMenuItems
import com.example.photoeditor.presenter.viewModels.CropViewModel

class CropFragment : BaseFragment() {
    private lateinit var binding: FragmentCropBinding
    private val viewModel: CropViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCropBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.changeImage(arguments?.getBitmap(MainFragment.CROP_IMAGE))
        with(viewModel) {
            image.observe(viewLifecycleOwner) { binding.cropImageView.setImageBitmap(it) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.showAllMenuItems()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.actionRotate -> {
            binding.cropImageView.rotateImage(degrees = 90)
            true
        }
        R.id.actionSave -> {
            binding.cropImageView.getCroppedImage().apply {
                viewModel.changeImage(image = this)
                saveImage(TAG, bitmap = this)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        val TAG = CropFragment::class.simpleName.orEmpty()
    }
}