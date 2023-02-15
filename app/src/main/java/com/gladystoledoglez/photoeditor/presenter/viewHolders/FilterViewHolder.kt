package com.gladystoledoglez.photoeditor.presenter.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gladystoledoglez.photoeditor.databinding.ItemFilterBinding
import com.gladystoledoglez.photoeditor.domain.models.Filter

class FilterViewHolder(
    private val binding: ItemFilterBinding,
    private val onClickListener: (item: Filter) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Filter) {
        with(binding) {
            tvFilter.text = item.text
            ivFilter.setImageDrawable(item.drawable)
            root.setOnClickListener { onClickListener(item) }
        }
    }

    companion object {
        fun newInstance(parent: ViewGroup, onClickListener: (item: Filter) -> Unit) =
            FilterViewHolder(
                ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onClickListener
            )
    }
}