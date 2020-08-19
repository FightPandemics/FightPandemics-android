package com.fightpandemics.dagger

import com.fightpandemics.data.LoginDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger module providing stuff for [:login] module.
 */
@Module
class LoginModule {

    @Provides
    fun provideLoginDataSource(): LoginDataSource =
        LoginDataSource()

}