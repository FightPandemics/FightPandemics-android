package com.fightpandemics.createpost.dagger

import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.domain.repository.PostsRepository
import com.fightpandemics.core.domain.repository.ProfileRepository
import com.fightpandemics.createpost.domain.CreatePostsUseCase
import com.fightpandemics.createpost.domain.LoadCurrentUserUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * created by Osaigbovo Odiase
 * Dagger module providing stuff for [:createpost] module.
 */
@Module
class CreatePostModule {

    @ExperimentalCoroutinesApi
    @Provides
    fun provideCreatePostUseCase(postRepository: PostsRepository, dispatcherProvider: CoroutinesDispatcherProvider): CreatePostsUseCase {
        return CreatePostsUseCase(postRepository, dispatcherProvider)
    }

    @ExperimentalCoroutinesApi
    @Provides
    fun provideLoadCurrentUserUseCase(
        profileRepository: ProfileRepository,
        dispatcherProvider: CoroutinesDispatcherProvider,
    ): LoadCurrentUserUseCase = LoadCurrentUserUseCase(profileRepository, dispatcherProvider)
}
