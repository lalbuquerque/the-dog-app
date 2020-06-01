package com.github.lalbuquerque.dogapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.lalbuquerque.dogapp.db.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userEntity: UserEntity)

    @Query("SELECT * FROM user LIMIT 1")
    fun get(): UserEntity

    @Query("DELETE FROM user")
    fun delete()
}