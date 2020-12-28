package com.fightpandemics.createpost.dagger

import com.fightpandemics.createpost.domain.repository.CreatePostRepositoryImpl
import com.fightpandemics.createpost.data.remote.PostRemoteDataSource
import com.fightpandemics.createpost.domain.repository.CreatePostRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CreatePostDataModule {

    @Provides
    fun provideCreatePostRepository(moshi: Moshi, postRemoteDataSource: PostRemoteDataSource): CreatePostRepository =
        CreatePostRepositoryImpl(
            moshi,
            postRemoteDataSource
        )
}