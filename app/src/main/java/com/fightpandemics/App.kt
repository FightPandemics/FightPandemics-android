package com.fightpandemics

import android.app.Application
import com.fightpandemics.di.component.AppComponent
import com.fightpandemics.di.component.DaggerAppComponent
import com.fightpandemics.di.module.ContextModule

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}