package com.fightpandemics.core.data.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

class AuthenticationInterceptor(private val token: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val builder: Request.Builder = original
            .newBuilder()
            .header("Authorization", token)
        Timber.e(token)
        val request: Request = builder.build()
        return chain.proceed(request)
    }
}