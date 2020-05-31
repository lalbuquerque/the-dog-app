package com.github.lalbuquerque.dogapp.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.github.lalbuquerque.dogapp.db.DogDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule() {

    @Singleton
    @Provides
    fun provideDb(app: Application): DogDb {
        return Room.databaseBuilder(app, DogDb::class.java, "dog.db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
}