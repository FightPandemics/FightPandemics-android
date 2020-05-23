package com.fightpandemics.di.component

import android.content.Context
import com.fightpandemics.App
import com.fightpandemics.di.context.ApplicationContext
import com.fightpandemics.di.module.ContextModule
import com.fightpandemics.di.scope.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = [ContextModule::class])
interface AppComponent {

    @ApplicationContext
    fun getContext(): Context

    fun inject(application: App)
}