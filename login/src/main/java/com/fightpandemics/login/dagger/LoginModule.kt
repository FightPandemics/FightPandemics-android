package com.fightpandemics.login.dagger

import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.domain.repository.LoginRepository
import com.fightpandemics.login.domain.LoginUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Dagger module providing stuff for [:login] module.
 */
@Module
class LoginModule {

    @ExperimentalCoroutinesApi
    @Provides
    fun provideLoginUseCase(
        loginRepository: LoginRepository,
        dispatcherProvider: CoroutinesDispatcherProvider
    ): LoginUseCase = LoginUseCase(loginRepository, dispatcherProvider)
}