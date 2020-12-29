package com.fightpandemics.createpost.dagger

import com.fightpandemics.core.BuildConfig
import com.fightpandemics.createpost.data.api.CreatePostRetrofitService
import com.fightpandemics.createpost.data.api.NetworkApi
import com.fightpandemics.createpost.data.interceptor.CookieHeaderInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
class CreatePostNetworkModule {

    @Provides
    fun provideRetrofitService(moshi: Moshi, okHttpClient: Lazy<OkHttpClient>): NetworkApi =
        CreatePostRetrofitService.createService(moshi, okHttpClient, NetworkApi::class.java)

    @Provides
    fun provideMoshi(): Moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    fun provideCookieHeaderInterceptor(): CookieHeaderInterceptor =
        CookieHeaderInterceptor()

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        cookieHeaderInterceptor: CookieHeaderInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(cookieHeaderInterceptor)
        return okHttpClient.build()
    }
}