package com.github.lalbuquerque.dogapp.ui.dogfeed

data class DogFeedLoadResult(
        val success: Boolean = false,
        val error: Int? = null,
        val category: DogCategory? = null)