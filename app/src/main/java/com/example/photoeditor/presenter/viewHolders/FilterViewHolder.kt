package com.example.photoeditor.presenter.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.photoeditor.domain.models.Filter
import com.example.photoeditor.databinding.ItemFilterBinding

class FilterViewHolder(
    private val binding: ItemFilterBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(filter: Filter) {
        with(binding) {
            tvFilter.text = filter.text
            ivFilter.setImageResource(filter.image)
        }
    }

    companion object {
        fun newInstance(parent: ViewGroup) = FilterViewHolder(
            ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}