package com.fightpandemics.ui

import android.app.Application
import com.fightpandemics.di.component.AppComponent
import com.fightpandemics.di.component.DaggerAppComponent
import com.fightpandemics.di.module.AppModule

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}