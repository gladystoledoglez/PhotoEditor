package com.example.photoeditor.presenter.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.drawToBitmap
import androidx.fragment.app.viewModels
import com.example.photoeditor.R
import com.example.photoeditor.databinding.FragmentFilterBinding
import com.example.photoeditor.domain.enums.FilterEnum
import com.example.photoeditor.domain.models.Filter
import com.example.photoeditor.extensions.getBitmap
import com.example.photoeditor.extensions.showMenuItem
import com.example.photoeditor.presenter.adapters.FilterAdapter
import com.example.photoeditor.presenter.viewModels.FilterViewModel

class FilterFragment : BaseFragment() {
    private lateinit var binding: FragmentFilterBinding
    private val viewModel: FilterViewModel by viewModels()
    private val adapter: FilterAdapter by lazy { FilterAdapter(::onClickListener) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        binding.rvFilters.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.changeImage(arguments?.getBitmap(MainFragment.FILTER_IMAGE))
        with(viewModel) {
            image.observe(viewLifecycleOwner) { binding.ivFilteredPhoto.setImageBitmap(it) }
            filters.observe(viewLifecycleOwner) { adapter.submitList(it) }
            getFilters()
        }
    }

    private fun onClickListener(filter: Filter) {
        with(binding.ivFilteredPhoto) {
            when (filter.text) {
                FilterEnum.SEPIA.name -> colorFilter = viewModel.getSepiaFilter()
                FilterEnum.GRAYSCALE.name -> colorFilter = viewModel.getGrayScaleFilter()
                FilterEnum.NEGATIVE.name -> colorFilter = viewModel.getNegativeFilter()
                FilterEnum.CYAN.name -> colorFilter = viewModel.getCyanFilter()
                FilterEnum.GRAIN.name -> colorFilter = viewModel.getGrainFilter()
            }
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

    companion object {
        val TAG = FilterFragment::class.simpleName.orEmpty()
    }
}