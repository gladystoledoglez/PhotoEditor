package com.gladystoledoglez.photoeditor.presenter.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import androidx.core.view.drawToBitmap
import androidx.fragment.app.viewModels
import com.example.photoeditor.domain.enums.FilterEnum
import com.gladystoledoglez.photoeditor.R
import com.gladystoledoglez.photoeditor.databinding.FragmentFilterBinding
import com.gladystoledoglez.photoeditor.domain.models.Filter
import com.gladystoledoglez.photoeditor.extensions.getBitmap
import com.gladystoledoglez.photoeditor.extensions.showMenuItem
import com.gladystoledoglez.photoeditor.extensions.toLiteModelCartoonganFp161
import com.gladystoledoglez.photoeditor.ml.LiteModelCartoonganFp161
import com.gladystoledoglez.photoeditor.presenter.adapters.FilterAdapter
import com.gladystoledoglez.photoeditor.presenter.viewModels.FilterViewModel

class FilterFragment : BaseFragment() {
    private lateinit var binding: FragmentFilterBinding
    private var model: LiteModelCartoonganFp161? = null
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
        model = context?.toLiteModelCartoonganFp161()
        val bitmap = arguments?.getBitmap(MainFragment.FILTER_IMAGE)
        with(viewModel) {
            image.observe(viewLifecycleOwner) {
                setFilteredPhoto(it)
                loadFilters(resources)
            }
            cartoonedImage.observe(viewLifecycleOwner) { setFilteredPhoto(it) }
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
        super.onDestroy()
        model?.close()
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
                FilterEnum.CARTOONED.name -> viewModel.setCartoonedImage(model)
            }
        }
    }

    private fun setFilteredPhoto(bitmap: Bitmap?) {
        binding.ivFilteredPhoto.setImageBitmap(bitmap)
    }

    companion object {
        val TAG = FilterFragment::class.simpleName.orEmpty()
    }
}