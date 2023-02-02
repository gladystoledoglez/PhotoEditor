package com.example.photoeditor.presenter.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.photoeditor.databinding.ItemFilterBinding
import com.example.photoeditor.domain.models.Filter

class FilterViewHolder(
    private val binding: ItemFilterBinding,
    private val onClickListener: (item: Filter) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Filter) {
        with(binding) {
            tvFilter.text = item.text
            ivFilter.setImageResource(item.image)
            root.setOnClickListener { onClickListener(item) }
        }
    }

    companion object {
        fun newInstance(parent: ViewGroup, onClickListener: (item: Filter) -> Unit) = FilterViewHolder(
            ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickListener
        )
    }
}