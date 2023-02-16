package com.gladystoledoglez.photoeditor.presenter.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.photoeditor.domain.enums.FilterEnum
import com.gladystoledoglez.photoeditor.R
import com.gladystoledoglez.photoeditor.databinding.FragmentFilterBinding
import com.gladystoledoglez.photoeditor.domain.models.Filter
import com.gladystoledoglez.photoeditor.extensions.getBitmap
import com.gladystoledoglez.photoeditor.extensions.orFalse
import com.gladystoledoglez.photoeditor.extensions.showMenuItem
import com.gladystoledoglez.photoeditor.extensions.toLiteModelCartoonedFp161
import com.gladystoledoglez.photoeditor.ml.LiteModelCartoonganFp161
import com.gladystoledoglez.photoeditor.presenter.adapters.FilterAdapter
import com.gladystoledoglez.photoeditor.presenter.viewModels.FilterViewModel

class FilterFragment : BaseFragment() {
    private lateinit var binding: FragmentFilterBinding
    private var cartoonedModel: LiteModelCartoonganFp161? = null
    private val viewModel: FilterViewModel by viewModels()
    private val adapter: FilterAdapter by lazy { FilterAdapter(::onClickListener) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.rvFilters.adapter = adapter
        cartoonedModel = context?.toLiteModelCartoonedFp161()
        val bitmap = arguments?.getBitmap(MainFragment.FILTER_IMAGE)
        with(viewModel) {
            image.observe(viewLifecycleOwner) {
                setFilteredPhoto(it)
                loadFilters(cartoonedModel, resources)
            }
            cartoonedImage.observe(viewLifecycleOwner) { setFilteredPhoto(it) }
            isProcessing.observe(viewLifecycleOwner) { setProcessing(it.orFalse()) }
            filters.observe(viewLifecycleOwner) { adapter.submitList(it) }
            changeImage(bitmap)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.showMenuItem(R.id.actionSave)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.actionSave -> {
            binding.ivFilteredPhoto.drawToBitmap().apply {
                viewModel.changeImage(image = this)
                sendImageFrom(TAG, image = this)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        cartoonedModel?.close()
        super.onDestroy()
    }

    private fun onClickListener(filter: Filter) {
        viewModel.restoreImage()
        with(binding.ivFilteredPhoto) {
            clearColorFilter()
            when (filter.text) {
                FilterEnum.SEPIA.name -> colorFilter = viewModel.getSepiaFilter()
                FilterEnum.GRAYSCALE.name -> colorFilter = viewModel.getGrayScaleFilter()
                FilterEnum.NEGATIVE.name -> colorFilter = viewModel.getNegativeFilter()
                FilterEnum.CYAN.name -> colorFilter = viewModel.getCyanFilter()
                FilterEnum.GRAIN.name -> colorFilter = viewModel.getGrainFilter()
                FilterEnum.CARTOONED.name -> viewModel.setCartoonedImage(cartoonedModel)
            }
        }
    }

    private fun setFilteredPhoto(bitmap: Bitmap?) {
        binding.ivFilteredPhoto.setImageBitmap(bitmap)
    }

    private fun setProcessing(isProcessing: Boolean) {
        with(binding) {
            ivFilteredPhoto.alpha = if (isProcessing) 0.1F else 1.0F
            pbProcessing.isVisible = isProcessing
            if (isProcessing.not()) {
                restartCartoonedModel()
            }
        }
    }

    private fun restartCartoonedModel() {
        cartoonedModel?.close()
        cartoonedModel = context?.toLiteModelCartoonedFp161()
    }

    companion object {
        val TAG = FilterFragment::class.simpleName.orEmpty()
    }
}