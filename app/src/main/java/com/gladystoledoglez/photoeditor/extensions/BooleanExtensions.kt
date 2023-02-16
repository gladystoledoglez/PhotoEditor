package com.gladystoledoglez.photoeditor.extensions

fun Boolean?.orValue(value: Boolean) = this ?: value

fun Boolean?.orFalse() = this.orValue(value = false)

fun Boolean?.orTrue() = this.orValue(value = true)