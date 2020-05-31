package com.github.lalbuquerque.dogapp.db.entity

import androidx.room.Entity

@Entity(tableName = "user", primaryKeys = ["email"])
data class UserEntity(val email: String, val token: String? = "")