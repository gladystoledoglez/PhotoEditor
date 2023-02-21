package com.gladystoledoglez.photoeditor.presenter.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.gladystoledoglez.photoeditor.MainActivity
import com.gladystoledoglez.photoeditor.R
import com.gladystoledoglez.photoeditor.databinding.FragmentFilterBinding
import com.gladystoledoglez.photoeditor.domain.enums.Filters
import com.gladystoledoglez.photoeditor.domain.models.FilterModel
import com.gladystoledoglez.photoeditor.extensions.getBitmap
import com.gladystoledoglez.photoeditor.extensions.orFalse
import com.gladystoledoglez.photoeditor.extensions.showMenuItem
import com.gladystoledoglez.photoeditor.presenter.adapters.FilterAdapter
import com.gladystoledoglez.photoeditor.presenter.viewModels.FilterViewModel

class FilterFragment : BaseFragment() {
    private lateinit var binding: FragmentFilterBinding
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
        getMainActivity()?.initCartoonedModels()
        with(viewModel) {
            image.observe(viewLifecycleOwner) {
                setFilteredPhoto(it)
                loadFilters(getMainActivity()?.getCartoonedFilterModel(), resources)
            }
            cartoonedImage.observe(viewLifecycleOwner) { setFilteredPhoto(it) }
            isProcessing.observe(viewLifecycleOwner) { setProcessing(it.orFalse()) }
            filters.observe(viewLifecycleOwner) { adapter.submitList(it) }
            changeImage(arguments?.getBitmap(MainFragment.IMAGE))
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

    private fun onClickListener(filter: FilterModel) {
        viewModel.restoreImage()
        with(binding.ivFilteredPhoto) {
            clearColorFilter()
            when (filter.text) {
                Filters.SEPIA.name -> colorFilter = viewModel.getSepiaFilter()
                Filters.GRAYSCALE.name -> colorFilter = viewModel.getGrayScaleFilter()
                Filters.NEGATIVE.name -> colorFilter = viewModel.getNegativeFilter()
                Filters.CYAN.name -> colorFilter = viewModel.getCyanFilter()
                Filters.GRAIN.name -> colorFilter = viewModel.getGrainFilter()
                Filters.CARTOONED.name -> viewModel.setCartoonedImage(getMainActivity()?.getCartoonedImageModel())
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
        }
    }

    private fun getMainActivity() = activity as? MainActivity?

    companion object {
        val TAG = FilterFragment::class.simpleName.orEmpty()
    }
}