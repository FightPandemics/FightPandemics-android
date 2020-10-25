package com.fightpandemics.core.data.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class AuthenticationInterceptor /*@Inject constructor(
    private val authTokenManager: IAuthTokenManager,
    private val deviceTokenManager: IDeviceTokenManager
)*/ : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        val requestBuilder: Request.Builder = request
            .newBuilder()
            //.header("Authorization", token)

       /* requestBuilder.addHeader(
            "xx",
            "Bearer ${authTokenManager.getToken()}"
        )*/
//        requestBuilder.addHeader(
//            "xx",
//            "Bearer ${deviceTokenManager.getDeviceToken()}"
//        )

        val response: Response?
        try {
            response = chain.proceed(requestBuilder.build())
        } catch (e: Exception) {
            println("<-- HTTP FAILED: $e")
            throw e
        }

        return response
    }
}