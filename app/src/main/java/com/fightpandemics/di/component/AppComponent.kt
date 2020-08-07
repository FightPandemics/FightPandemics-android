package com.fightpandemics.di.component

import com.fightpandemics.ui.App
import com.fightpandemics.di.module.AppModule
import com.fightpandemics.di.scope.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(application: App)
}