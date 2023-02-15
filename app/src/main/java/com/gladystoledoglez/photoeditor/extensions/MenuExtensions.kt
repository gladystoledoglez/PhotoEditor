package com.gladystoledoglez.photoeditor.extensions

import android.view.Menu
import androidx.core.view.children
import androidx.core.view.iterator

private fun Menu.showMenuItems(isVisible: Boolean) {
    iterator().forEach { it.isVisible = isVisible }
}

fun Menu.hideAllMenuItems() {
    showMenuItems(isVisible = false)
}

fun Menu.showAllMenuItems() {
    showMenuItems(isVisible = true)
}

fun Menu.showMenuItem(menuItemId: Int) {
    children.find { it.itemId == menuItemId }?.isVisible = true
}

fun Menu.showMenuItems(vararg menuItemsIds: Int) {
    menuItemsIds.forEach { showMenuItem(it) }
}