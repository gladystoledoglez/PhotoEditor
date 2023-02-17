package com.gladystoledoglez.photoeditor.presenter.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.gladystoledoglez.photoeditor.domain.models.FilterModel
import com.gladystoledoglez.photoeditor.presenter.viewHolders.FilterViewHolder

class FilterAdapter(
    private val onClickListener: (item: FilterModel) -> Unit
) : ListAdapter<FilterModel, FilterViewHolder>(FilterModel.DIFF_UTIL_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FilterViewHolder.newInstance(parent, onClickListener)

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}