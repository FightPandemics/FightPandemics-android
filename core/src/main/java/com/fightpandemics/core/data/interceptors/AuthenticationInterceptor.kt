package com.fightpandemics.core.data.interceptors

import com.fightpandemics.core.data.local.AuthTokenLocalDataSource
import com.fightpandemics.core.data.model.login.CookieToken
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(
    private val authTokenLocalDataSource: AuthTokenLocalDataSource
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request
            .newBuilder()

        if (request.header("No-Authentication") == null) {
            val token = authTokenLocalDataSource.getToken()
            if (!token.isNullOrEmpty()) {
                val finalToken = "Bearer $token"
                requestBuilder
                    .addHeader("Authorization", finalToken)
                    .build()
            }
        }

        if (request.header("Login") != null) {
            val token = authTokenLocalDataSource.getToken()
            if (!token.isNullOrEmpty()) {
                val cookie = CookieToken("-", token);
                requestBuilder
                    .addHeader(cookie.getCookieName(), cookie.getCookieParameter())
                    .build()
            }
        }

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
