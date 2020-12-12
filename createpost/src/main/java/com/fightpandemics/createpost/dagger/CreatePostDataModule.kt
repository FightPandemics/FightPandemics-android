package com.fightpandemics.createpost.dagger

import com.fightpandemics.createpost.data.CreatePostRepositoryImpl
import com.fightpandemics.createpost.data.remote.PostRemoteDataSource
import com.fightpandemics.createpost.domain.repository.CreatePostRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CreatePostDataModule {

    @Provides
    @Singleton
    fun providePostsRepository(postRemoteDataSource: PostRemoteDataSource): CreatePostRepository =
        CreatePostRepositoryImpl(postRemoteDataSource)
}