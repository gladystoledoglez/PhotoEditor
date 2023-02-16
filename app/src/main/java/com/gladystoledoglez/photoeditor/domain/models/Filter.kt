package com.gladystoledoglez.photoeditor.domain.models

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.DiffUtil

data class Filter(val text: String, val drawable: Drawable?) {

    companion object {
        val DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<Filter>() {
            override fun areItemsTheSame(oldItem: Filter, newItem: Filter): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Filter, newItem: Filter): Boolean {
                return oldItem == newItem
            }
        }
    }
}