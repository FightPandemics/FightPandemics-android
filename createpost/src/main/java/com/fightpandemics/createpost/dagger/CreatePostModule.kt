package com.fightpandemics.createpost.dagger

import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.createpost.domain.CreatePostsUseCase
import com.fightpandemics.createpost.domain.repository.CreatePostRepository
import dagger.Module
import dagger.Provides

/**
 * Dagger module providing stuff for [:createpost] module.
 */
@Module
class CreatePostModule {

    @Provides
    fun provideCreatePostUseCase(createPostRepository: CreatePostRepository, dispatcherProvider: CoroutinesDispatcherProvider): CreatePostsUseCase {
        return CreatePostsUseCase(createPostRepository, dispatcherProvider)
    }
}