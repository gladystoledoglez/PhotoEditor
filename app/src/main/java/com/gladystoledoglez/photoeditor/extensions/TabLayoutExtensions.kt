package com.gladystoledoglez.photoeditor.extensions

import com.gladystoledoglez.photoeditor.R
import com.gladystoledoglez.photoeditor.domain.enums.Tabs
import com.google.android.material.tabs.TabLayout

fun TabLayout.Tab?.getFragmentId() = when (this?.position) {
    Tabs.CROP.ordinal -> R.id.action_mainFragment_to_cropFragment
    Tabs.LIGHT.ordinal -> R.id.action_mainFragment_to_lightFragment
    Tabs.COLOR.ordinal -> R.id.action_mainFragment_to_colorFragment
    Tabs.FILTERS.ordinal -> R.id.action_mainFragment_to_filterFragment
    else -> R.id.mainFragment
}

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