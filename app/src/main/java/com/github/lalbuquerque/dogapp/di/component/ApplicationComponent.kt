package com.github.lalbuquerque.dogapp.di.component

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.github.lalbuquerque.dogapp.DogApplication
import com.github.lalbuquerque.dogapp.ui.login.LoginActivity
import com.github.lalbuquerque.dogapp.di.module.ApplicationModule
import com.github.lalbuquerque.dogapp.di.module.DataSourceModule
import com.github.lalbuquerque.dogapp.di.module.DatabaseModule
import com.github.lalbuquerque.dogapp.di.module.NetworkModule
import com.github.lalbuquerque.dogapp.ui.dogfeed.DogFeedActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
    ApplicationModule::class,
    NetworkModule::class,
    DatabaseModule::class,
    DataSourceModule::class))
interface ApplicationComponent {

    fun app(): Application
    fun context(): Context
    fun preferences(): SharedPreferences

    fun inject(application: DogApplication)
    fun inject(loginActivity: LoginActivity)
    fun inject(dogFeedActivity: DogFeedActivity)
}
