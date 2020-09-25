package com.fightpandemics.login.dagger

import com.fightpandemics.dagger.scope.ActivityScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Dagger module providing stuff for [:login] module.
 */
@Module
class LoginModule {

    @ActivityScope
    @Named("Login")
    @Provides
    fun provideLoginString(): String = "Test Login Dagger Implementation"
}