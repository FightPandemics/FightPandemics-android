package com.fightpandemics.di.module

import android.content.Context
import com.fightpandemics.di.context.ApplicationContext
import com.fightpandemics.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ContextModule(private val context: Context) {

    @Provides
    @ApplicationScope
    @ApplicationContext
    fun providesContext() = context
}