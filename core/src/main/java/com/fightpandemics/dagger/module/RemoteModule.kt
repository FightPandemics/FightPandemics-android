package com.fightpandemics.dagger.module

import com.fightpandemics.data.api.FightPandemicsAPI
import com.fightpandemics.data.remote.PostsRemoteDataSource
import com.fightpandemics.data.remote.PostsRemoteDataSourceImpl
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
}