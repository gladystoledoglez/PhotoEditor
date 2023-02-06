package com.example.photoeditor.extensions

import android.view.Menu
import androidx.core.view.children
import androidx.core.view.iterator

fun Menu.showMenuItems(isVisible: Boolean) {
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