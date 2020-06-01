package com.github.lalbuquerque.dogapp.di.module

import android.app.Application
import android.content.Context
import com.github.lalbuquerque.dogapp.di.qualifiers.ObserveOn
import com.github.lalbuquerque.dogapp.di.qualifiers.SubscribeOn
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Singleton

@Module
class ApplicationModule(var app: Application) {

    @Provides
    @Singleton
    fun provideApp(): Application = app

    @Provides
    @Singleton
    fun provideContext(): Context = app.applicationContext

    @Provides
    @SubscribeOn
    fun provideSubscribeOnScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @ObserveOn
    fun provideObserveOnScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}
