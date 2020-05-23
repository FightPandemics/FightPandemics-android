package com.fightpandemics.di.component

import com.fightpandemics.App
import com.fightpandemics.di.module.AppModule
import com.fightpandemics.di.scope.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(application: App)
}