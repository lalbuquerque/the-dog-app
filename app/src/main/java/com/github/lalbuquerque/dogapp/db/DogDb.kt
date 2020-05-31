package com.github.lalbuquerque.dogapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.lalbuquerque.dogapp.db.dao.UserDao
import com.github.lalbuquerque.dogapp.db.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DogDb : RoomDatabase() {

    abstract fun userDao(): UserDao

//    abstract fun repoDao(): RepoDao
}