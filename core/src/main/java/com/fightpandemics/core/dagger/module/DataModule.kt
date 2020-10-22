package com.fightpandemics.core.dagger.module

import com.fightpandemics.core.data.remote.login.LoginRemoteDataSource
import com.fightpandemics.core.data.remote.posts.PostsRemoteDataSource
import com.fightpandemics.core.data.repository.LoginRepositoryImpl
import com.fightpandemics.core.data.repository.PostsRepositoryImpl
import com.fightpandemics.core.domain.repository.LoginRepository
import com.fightpandemics.core.domain.repository.PostsRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

/**
 * Dagger module to provide data functionalities.
 */
@FlowPreview
@ExperimentalCoroutinesApi
@Module
class DataModule {
    
    @Singleton
    @Provides
    fun providePostsRepository(postsRemoteDataSource: PostsRemoteDataSource): PostsRepository =
        PostsRepositoryImpl(postsRemoteDataSource)

    @Singleton
    @Provides
    fun provideLoginRepository(loginRemoteDataSource: LoginRemoteDataSource): LoginRepository =
        LoginRepositoryImpl(loginRemoteDataSource)
}
