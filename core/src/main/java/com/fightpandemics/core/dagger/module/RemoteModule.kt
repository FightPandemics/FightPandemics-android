package com.fightpandemics.core.dagger.module

import com.fightpandemics.core.data.api.FightPandemicsAPI
import com.fightpandemics.core.data.remote.location.LocationRemoteDataSource
import com.fightpandemics.core.data.remote.location.LocationRemoteDataSourceImpl
import com.fightpandemics.core.data.remote.login.LoginRemoteDataSource
import com.fightpandemics.core.data.remote.login.LoginRemoteDataSourceImpl
import com.fightpandemics.core.data.remote.posts.PostsRemoteDataSource
import com.fightpandemics.core.data.remote.posts.PostsRemoteDataSourceImpl
import com.fightpandemics.core.data.remote.profile.ProfileRemoteDataSource
import com.fightpandemics.core.data.remote.profile.ProfileRemoteDataSourceImpl
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

    @Singleton
    @Provides
    fun provideLocationRemoteDataSource(
        fightPandemicsAPI: FightPandemicsAPI
    ): LocationRemoteDataSource = LocationRemoteDataSourceImpl(fightPandemicsAPI)

    @Singleton
    @Provides
    fun provideProfileRemoteDataSource(fightPandemicsAPI: FightPandemicsAPI): ProfileRemoteDataSource =
        ProfileRemoteDataSourceImpl(fightPandemicsAPI)
}
