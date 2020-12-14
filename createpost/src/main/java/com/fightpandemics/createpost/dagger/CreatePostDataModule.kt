package com.fightpandemics.createpost.dagger

import com.fightpandemics.createpost.domain.repository.CreatePostRepositoryImpl
import com.fightpandemics.createpost.data.remote.PostRemoteDataSource
import com.fightpandemics.createpost.domain.repository.CreatePostRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CreatePostDataModule {

    @Provides
    fun provideCreatePostRepository(postRemoteDataSource: PostRemoteDataSource): CreatePostRepository =
        CreatePostRepositoryImpl(
            postRemoteDataSource
        )
}