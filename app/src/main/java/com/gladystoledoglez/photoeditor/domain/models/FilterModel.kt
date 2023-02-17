package com.gladystoledoglez.photoeditor.domain.models

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.DiffUtil

data class FilterModel(val text: String, val drawable: Drawable?) {

    companion object {
        val DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<FilterModel>() {
            override fun areItemsTheSame(oldItem: FilterModel, newItem: FilterModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FilterModel, newItem: FilterModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}