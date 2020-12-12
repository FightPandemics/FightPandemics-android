package com.fightpandemics.createpost.dagger

import com.fightpandemics.createpost.data.api.NetworkApi
import com.fightpandemics.createpost.data.remote.PostRemoteDataSource
import com.fightpandemics.createpost.data.remote.PostRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CreatePostRemoteModule {

    @Singleton
    @Provides
    fun providePostsRemoteDataSource(networkApi: NetworkApi): PostRemoteDataSource =
        PostRemoteDataSourceImpl(networkApi)
}