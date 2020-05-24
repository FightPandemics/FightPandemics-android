package com.fightpandemics.di.module

import android.content.res.Resources
import com.fightpandemics.App
import com.fightpandemics.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val app: App) {

    @ApplicationScope
    @Provides
    fun providesApplication(): App = app

    @ApplicationScope
    @Provides
    fun providesResources(): Resources = app.resources
}