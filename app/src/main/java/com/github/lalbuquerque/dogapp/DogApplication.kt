package com.github.lalbuquerque.dogapp

import android.app.Application
import com.github.lalbuquerque.dogapp.di.component.ApplicationComponent
import com.github.lalbuquerque.dogapp.di.component.DaggerApplicationComponent
import com.github.lalbuquerque.dogapp.di.module.ApplicationModule
import com.github.lalbuquerque.dogapp.di.module.DataSourceModule
import com.github.lalbuquerque.dogapp.di.module.DatabaseModule
import com.github.lalbuquerque.dogapp.di.module.NetworkModule

class DogApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .networkModule(NetworkModule())
            .databaseModule(DatabaseModule())
            .dataSourceModule(DataSourceModule())
            .build()

        component?.inject(this)
    }

    companion object {
        var component: ApplicationComponent? = null
            private set
    }
}