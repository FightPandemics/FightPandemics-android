package com.fightpandemics.home.dagger

import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.domain.repository.PostsRepository
import com.fightpandemics.home.domain.DeletePostUsecase
import com.fightpandemics.home.domain.LoadPostsUseCase
import dagger.Module
import dagger.Provides

/**
 * created by Osaigbovo Odiase
 *
 * Dagger module providing stuff for [:Home].
 */
@Module
class HomeModule {

    @Provides
    fun provideLoadPostsUseCase(
        postsRepository: PostsRepository,
        dispatcherProvider: CoroutinesDispatcherProvider,
    ): LoadPostsUseCase = LoadPostsUseCase(postsRepository, dispatcherProvider)

    @Provides
    fun provideDeletePostUsecase(
        postsRepository: PostsRepository,
        dispatcherProvider: CoroutinesDispatcherProvider,
    ): DeletePostUsecase = DeletePostUsecase(postsRepository, dispatcherProvider)
}
