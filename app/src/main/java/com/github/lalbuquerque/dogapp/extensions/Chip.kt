package com.github.lalbuquerque.dogapp.extensions

import com.robertlevonyan.views.chip.Chip

fun Chip.enableAndSelectable() {
    this.isEnabled = true
    this.selectable = true
}