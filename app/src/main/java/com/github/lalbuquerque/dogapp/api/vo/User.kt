package com.github.lalbuquerque.dogapp.api.vo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User (
        @field:SerializedName("email")
        @Expose(serialize = true, deserialize = true)
        val email: String,

        @field:SerializedName("token")
        @Expose(serialize = false, deserialize = true)
        val token: String? = "")