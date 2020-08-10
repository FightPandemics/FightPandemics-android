package com.fightpandemics.di.providers

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor

object HttpLoggingInterceptorProvider {

    fun provide() = HttpLoggingInterceptor(object: HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Log.v("HTTPLogger", message)
        }
    }).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}