package com.fightpandemics.data.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

/**
 * Interceptor to display response message
 */
class ErrorHandlerInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)
        when (response.code) {
            200 -> Timber.i("200 - Found")
            404 -> Timber.i("404 - Not Found")
            500 -> Timber.i("500 - Server Broken")
            504 -> Timber.i("500 - Server Broken")
            else -> Timber.i("Network Unknown Error")
        }
        return response
    }
}
