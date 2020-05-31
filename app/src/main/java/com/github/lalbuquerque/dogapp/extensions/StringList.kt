package com.github.lalbuquerque.dogapp.extensions

import com.github.lalbuquerque.dogapp.api.vo.DogImage

fun List<String>.transformIntoDogImageList(): List<DogImage> {
    val newList = mutableListOf<DogImage>()
    this.forEach { newList.add(DogImage(it)) }
    return newList
}