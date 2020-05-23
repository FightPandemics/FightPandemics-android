package com.fightpandemics.di.module

import android.app.Activity
import android.content.Context
import com.fightpandemics.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class HomeActivityContextModule(private val act: Activity) {

    @Provides
    @ActivityScope
    fun providesHomeActivity() = act

    @Provides
    @ActivityScope
    fun providesContext(): Context = act
}