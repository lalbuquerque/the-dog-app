package com.github.lalbuquerque.dogapp.di.module

import com.github.lalbuquerque.dogapp.api.DogApi
import com.github.lalbuquerque.dogapp.db.DogDb
import com.github.lalbuquerque.dogapp.db.dao.UserDao
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideDogApi(retrofitBuilder: Retrofit.Builder): DogApi {
        return retrofitBuilder.baseUrl("https://iddog-nrizncxqba-uc.a.run.app")
            .build()
            .create(DogApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserDao(db: DogDb): UserDao {
        return db.userDao()
    }
}