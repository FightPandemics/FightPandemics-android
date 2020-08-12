package com.fightpandemics.di.providers

import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

object HttpLoggingInterceptorProvider {

    fun provide() = HttpLoggingInterceptor(object: HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Timber.v(message)
        }
    }).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}