package com.fightpandemics.login.dagger

import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.local.AuthTokenLocalDataSource
import com.fightpandemics.core.data.local.AuthTokenLocalDataSourceImpl
import com.fightpandemics.core.data.prefs.PreferenceStorage
import com.fightpandemics.core.domain.repository.LoginRepository
import com.fightpandemics.login.domain.CompleteProfileUseCase
import com.fightpandemics.login.domain.LoginUseCase
import com.fightpandemics.login.domain.SignUPUseCase
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

    @ExperimentalCoroutinesApi
    @Provides
    fun provideSignUpUseCase(
        loginRepository: LoginRepository,
        dispatcherProvider: CoroutinesDispatcherProvider
    ): SignUPUseCase = SignUPUseCase(loginRepository, dispatcherProvider)

    @ExperimentalCoroutinesApi
    @Provides
    fun provideCompleteProfileUseCase(
        loginRepository: LoginRepository,
        dispatcherProvider: CoroutinesDispatcherProvider
    ): CompleteProfileUseCase = CompleteProfileUseCase(loginRepository, dispatcherProvider)

    @Provides
    fun provideAuthTokenLocalDataSource(
        preferenceStorage: PreferenceStorage,
    ): AuthTokenLocalDataSource = AuthTokenLocalDataSourceImpl(preferenceStorage)

}