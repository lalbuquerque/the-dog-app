package com.github.lalbuquerque.dogapp.api.vo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(@field:SerializedName("user")
                         @Expose(serialize = false, deserialize = true)
                         val user: User)