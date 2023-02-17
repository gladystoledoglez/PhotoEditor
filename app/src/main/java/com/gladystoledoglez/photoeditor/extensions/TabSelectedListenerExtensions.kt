package com.gladystoledoglez.photoeditor.extensions

import com.google.android.material.tabs.TabLayout

fun TabLayout.onTabSelectedListener(
    onTabSelected: (tab: TabLayout.Tab?) -> Unit
) = object : TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab?) {
        onTabSelected(tab)
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

}