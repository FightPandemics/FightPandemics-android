package com.fightpandemics.ui

import android.app.Application
import com.fightpandemics.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@App)

            modules(
                listOf(
                dbModule,
                daoModule,
                networkModule,
                apiServiceModule,
                viewModelModule
            ))
        }
    }
}
