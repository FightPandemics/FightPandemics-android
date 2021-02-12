package com.fightpandemics.core.dagger.module

import com.fightpandemics.core.BuildConfig
import com.fightpandemics.core.data.api.FightPandemicsAPI
import com.fightpandemics.core.data.interceptors.AccessTokenAuthenticator
import com.fightpandemics.core.data.interceptors.AuthenticationInterceptor
import com.fightpandemics.core.data.interceptors.ErrorHandlerInterceptor
import com.fightpandemics.core.data.interceptors.OfflineResponseCacheInterceptor
import com.fightpandemics.core.data.interceptors.ResponseCacheInterceptor
import com.fightpandemics.core.data.local.AuthTokenLocalDataSource
import com.fightpandemics.core.data.remote.RetrofitService
import com.fightpandemics.core.domain.repository.LocationRepository
import com.fightpandemics.core.domain.repository.LoginRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * Dagger module to provide networking functionalities.
 */
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitService(moshi: Moshi, okHttpClient: Lazy<OkHttpClient>): FightPandemicsAPI =
        RetrofitService.createService(moshi, okHttpClient, FightPandemicsAPI::class.java)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        accessTokenAuthenticator: AccessTokenAuthenticator,
        authenticationInterceptor: AuthenticationInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        responseCacheInterceptor: ResponseCacheInterceptor,
        offlineResponseCacheInterceptor: OfflineResponseCacheInterceptor,
        errorHandlerInterceptor: ErrorHandlerInterceptor,
    ): OkHttpClient {
        val okHttpClient = OkHttpClient
            .Builder()
            // .authenticator(accessTokenAuthenticator)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(authenticationInterceptor)


        if (BuildConfig.DEBUG) {
            okHttpClient
                .addNetworkInterceptor(responseCacheInterceptor)
                .addInterceptor(offlineResponseCacheInterceptor)
                .addInterceptor(errorHandlerInterceptor)
        }
        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Singleton
    @Provides
    fun provideAccessTokenAuthenticator(
        authTokenLocalDataSource: AuthTokenLocalDataSource,
        loginRepository: LoginRepository
    ): AccessTokenAuthenticator = AccessTokenAuthenticator(authTokenLocalDataSource, loginRepository)

    @Singleton
    @Provides
    fun provideAuthenticationInterceptor(
        authTokenLocalDataSource: AuthTokenLocalDataSource
    ): AuthenticationInterceptor =
        AuthenticationInterceptor(authTokenLocalDataSource)

    @Singleton
    @Provides
    fun provideResponseCacheInterceptor(): ResponseCacheInterceptor =
        ResponseCacheInterceptor()

    @Singleton
    @Provides
    fun provideOfflineResponseCacheInterceptor(): OfflineResponseCacheInterceptor =
        OfflineResponseCacheInterceptor()

    @Singleton
    @Provides
    fun provideErrorHandlerInterceptor(): ErrorHandlerInterceptor =
        ErrorHandlerInterceptor()
}
