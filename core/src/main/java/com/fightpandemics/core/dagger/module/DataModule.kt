package com.fightpandemics.core.dagger.module

import com.fightpandemics.core.data.local.AuthTokenLocalDataSource
import com.fightpandemics.core.data.local.AuthTokenLocalDataSourceImpl
import com.fightpandemics.core.data.prefs.FightPandemicsPreferenceDataStore
import com.fightpandemics.core.data.prefs.PreferenceStorage
import com.fightpandemics.core.data.remote.location.LocationRemoteDataSource
import com.fightpandemics.core.data.remote.login.LoginRemoteDataSource
import com.fightpandemics.core.data.remote.posts.PostsRemoteDataSource
import com.fightpandemics.core.data.remote.profile.ProfileRemoteDataSource
import com.fightpandemics.core.data.repository.LocationRepositoryImpl
import com.fightpandemics.core.data.repository.LoginRepositoryImpl
import com.fightpandemics.core.data.repository.PostsRepositoryImpl
import com.fightpandemics.core.data.repository.ProfileRepositoryImpl
import com.fightpandemics.core.domain.repository.LocationRepository
import com.fightpandemics.core.domain.repository.LoginRepository
import com.fightpandemics.core.domain.repository.PostsRepository
import com.fightpandemics.core.domain.repository.ProfileRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

/**
 * created by Osaigbovo Odiase
 *
 * Dagger module to provide data functionalities.
 */
@FlowPreview
@ExperimentalCoroutinesApi
@Module
class DataModule {

    @Singleton
    @Provides
    fun providePostsRepository(
        preferenceStorage: PreferenceStorage,
        postsRemoteDataSource: PostsRemoteDataSource
    ): PostsRepository =
        PostsRepositoryImpl(preferenceStorage, postsRemoteDataSource)

    @Singleton
    @Provides
    fun provideLoginRepository(
        moshi: Moshi,
        loginRemoteDataSource: LoginRemoteDataSource,
        authTokenLocalDataSource: AuthTokenLocalDataSource
    ): LoginRepository =
        LoginRepositoryImpl(moshi, loginRemoteDataSource, authTokenLocalDataSource)

    @Singleton
    @Provides
    fun provideLocationRepository(
        moshi: Moshi,
        locationRemoteDataSource: LocationRemoteDataSource,
        preferenceStorage: PreferenceStorage
    ): LocationRepository =
        LocationRepositoryImpl(moshi, locationRemoteDataSource, preferenceStorage)

    @Singleton
    @Provides
    fun provideAuthTokenLocalDataSource(
        preferenceDataStore: FightPandemicsPreferenceDataStore
    ): AuthTokenLocalDataSource =
        AuthTokenLocalDataSourceImpl(preferenceDataStore)

    @Singleton
    @Provides
    fun provideProfileRepository(
        moshi: Moshi,
        preferenceStorage: PreferenceStorage,
        profileRemoteDataSource: ProfileRemoteDataSource
    ): ProfileRepository =
        ProfileRepositoryImpl(moshi, preferenceStorage, profileRemoteDataSource)
}
