package com.fightpandemics.dagger.module

import com.fightpandemics.data.remote.login.LoginRemoteDataSource
import com.fightpandemics.data.remote.posts.PostsRemoteDataSource
import com.fightpandemics.data.repository.LoginRepositoryImpl
import com.fightpandemics.data.repository.PostsRepositoryImpl
import com.fightpandemics.domain.repository.LoginRepository
import com.fightpandemics.domain.repository.PostsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger module to provide data functionalities.
 */
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
