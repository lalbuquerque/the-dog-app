package com.github.lalbuquerque.dogapp.api.vo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DogFeedResponse (
    @field:SerializedName("category")
    @Expose(serialize = false, deserialize = true)
    val category: String,

    @field:SerializedName("list")
    @Expose(serialize = false, deserialize = true)
    val list: List<String>)