package com.fightpandemics.home.dagger

import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.domain.repository.PostsRepository
import com.fightpandemics.home.domain.LoadPostsUseCase
import dagger.Module
import dagger.Provides

/**
 * Dagger module providing stuff for [:Home].
 */
@Module
class HomeModule {

    @Provides
    fun provideLoadPostsUseCase(
        postsRepository: PostsRepository,
        dispatcherProvider: CoroutinesDispatcherProvider,
    ): LoadPostsUseCase = LoadPostsUseCase(postsRepository, dispatcherProvider)
}
