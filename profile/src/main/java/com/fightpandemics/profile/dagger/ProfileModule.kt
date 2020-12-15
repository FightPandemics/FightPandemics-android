package com.fightpandemics.profile.dagger

import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.domain.repository.ProfileRepository
import com.fightpandemics.profile.domain.LoadCurrentUserUseCase
import dagger.Module
import dagger.Provides

/**
 * Dagger module providing stuff for [:Profile].
 */
@Module
class ProfileModule{

    @Provides
    fun provideLoadCurrentUserUseCase(
        profileRepository: ProfileRepository,
        dispatcherProvider: CoroutinesDispatcherProvider,
    ): LoadCurrentUserUseCase = LoadCurrentUserUseCase(profileRepository, dispatcherProvider)

}
