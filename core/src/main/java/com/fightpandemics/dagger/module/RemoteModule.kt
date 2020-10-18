package com.fightpandemics.dagger.module

import com.fightpandemics.data.api.FightPandemicsAPI
import com.fightpandemics.data.remote.posts.PostsRemoteDataSource
import com.fightpandemics.data.remote.posts.PostsRemoteDataSourceImpl
import com.fightpandemics.data.remote.login.LoginRemoteDataSource
import com.fightpandemics.data.remote.login.LoginRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger module to provide remote access to api endpoints.
 */
@Module
class RemoteModule {

    @Singleton
    @Provides
    fun providePostsRemoteDataSource(fightPandemicsAPI: FightPandemicsAPI): PostsRemoteDataSource =
        PostsRemoteDataSourceImpl(fightPandemicsAPI)

    @Singleton
    @Provides
    fun provideLoginRemoteDataSource(fightPandemicsAPI: FightPandemicsAPI): LoginRemoteDataSource =
        LoginRemoteDataSourceImpl(fightPandemicsAPI)
}