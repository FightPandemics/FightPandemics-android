package com.fightpandemics.di.providers

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpClientProvider {

    fun provide(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(loggingInterceptor)

        return okHttpClientBuilder.build()
    }
}