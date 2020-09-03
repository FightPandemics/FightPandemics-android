package com.fightpandemics.dagger.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger module to provide Context object.
 */
@Module
class ContextModule(private val application: Application) {

    @Singleton
    @Provides
    fun provideContext(): Context = application
}
