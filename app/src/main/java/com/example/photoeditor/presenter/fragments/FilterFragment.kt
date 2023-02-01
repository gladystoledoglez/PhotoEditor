package com.example.photoeditor.presenter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.photoeditor.presenter.adapters.FilterAdapter
import com.example.photoeditor.databinding.FragmentFilterBinding
import com.example.photoeditor.extensions.getBitmap
import com.example.photoeditor.presenter.viewModels.FilterViewModel

class FilterFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    private val viewModel: FilterViewModel by viewModels()
    private val adapter: FilterAdapter by lazy { FilterAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        binding.rvFilters.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.changeImage(arguments?.getBitmap(MainFragment.FILTER_IMAGE))
        with(viewModel) {
            image.observe(viewLifecycleOwner) { binding.ivFilteredPhoto.setImageBitmap(it) }
            filters.observe(viewLifecycleOwner) { adapter.submitList(it) }
            getFilters()
        }
    }
}