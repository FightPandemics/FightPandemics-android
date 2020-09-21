package com.fightpandemics.ui

import com.fightpandemics.dagger.scope.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class SplashModule {

    @ActivityScope
    @Provides
    fun provideCommonHome(): String =
        "DUPE"

}