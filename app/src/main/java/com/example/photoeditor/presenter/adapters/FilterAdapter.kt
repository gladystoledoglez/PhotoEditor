package com.example.photoeditor.presenter.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.photoeditor.domain.models.Filter
import com.example.photoeditor.presenter.viewHolders.FilterViewHolder

class FilterAdapter : ListAdapter<Filter, FilterViewHolder>(Filter.DIFF_UTIL_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FilterViewHolder.newInstance(parent)

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}