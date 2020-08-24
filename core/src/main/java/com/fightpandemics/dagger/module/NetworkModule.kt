package com.fightpandemics.dagger.module

import com.fightpandemics.BuildConfig
import com.fightpandemics.data.interceptors.ErrorHandlerInterceptor
import com.fightpandemics.data.interceptors.OfflineResponseCacheInterceptor
import com.fightpandemics.data.interceptors.ResponseCacheInterceptor
import com.fightpandemics.data.remote.RequestInterface
import com.fightpandemics.data.remote.RetrofitService
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
    fun provideRetrofitService(okHttpClient: Lazy<OkHttpClient>): RequestInterface =
        RetrofitService.createService(okHttpClient, RequestInterface::class.java)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        responseCacheInterceptor: ResponseCacheInterceptor,
        offlineResponseCacheInterceptor: OfflineResponseCacheInterceptor,
        errorHandlerInterceptor: ErrorHandlerInterceptor
    ): OkHttpClient {
        val clientBuilder = OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)

        if (BuildConfig.DEBUG) {
            clientBuilder
                .addNetworkInterceptor(responseCacheInterceptor)
                .addInterceptor(offlineResponseCacheInterceptor)
                .addInterceptor(errorHandlerInterceptor)
        }
        return clientBuilder.build()
    }

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
