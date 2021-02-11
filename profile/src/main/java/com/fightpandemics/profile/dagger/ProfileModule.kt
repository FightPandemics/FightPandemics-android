package com.fightpandemics.profile.dagger

import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.domain.repository.PostsRepository
import com.fightpandemics.core.domain.repository.ProfileRepository
import com.fightpandemics.profile.domain.LoadCurrentUserUseCase
import com.fightpandemics.profile.domain.LoadIndividualUserPostsUseCase
import com.fightpandemics.profile.domain.UpdateCurrentUserUseCase
import com.fightpandemics.profile.domain.UpdateIndividualAccountUseCase
import dagger.Module
import dagger.Provides

/**
 * Dagger module providing stuff for [:Profile].
 */
@Module
class ProfileModule {

    @Provides
    fun provideLoadCurrentUserUseCase(
        profileRepository: ProfileRepository,
        dispatcherProvider: CoroutinesDispatcherProvider,
    ): LoadCurrentUserUseCase = LoadCurrentUserUseCase(profileRepository, dispatcherProvider)

    @Provides
    fun provideUpdateCurrentUserUseCase(
        profileRepository: ProfileRepository,
        dispatcherProvider: CoroutinesDispatcherProvider,
    ): UpdateCurrentUserUseCase = UpdateCurrentUserUseCase(profileRepository, dispatcherProvider)

    @Provides
    fun provideUpdateCurrentUserAccountUseCase(
        profileRepository: ProfileRepository,
        dispatcherProvider: CoroutinesDispatcherProvider,
    ): UpdateIndividualAccountUseCase = UpdateIndividualAccountUseCase(profileRepository, dispatcherProvider)

    @Provides
    fun provideLoadIndividualUserPostsUseCase(
        postsRepository: PostsRepository,
        dispatcherProvider: CoroutinesDispatcherProvider,
    ): LoadIndividualUserPostsUseCase = LoadIndividualUserPostsUseCase(postsRepository, dispatcherProvider)
}
