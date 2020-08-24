package com.fightpandemics.data.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

/**
 * Interceptor to cache data and maintain it for a minute.
 * If the same network request is sent within a minute,
 * the response is retrieved from cache.
 */
class ResponseCacheInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        val cacheControl: String? = originalResponse.header("Cache-Control")

        return if (cacheControl == null || cacheControl.contains("no-store") ||
            cacheControl.contains("no-cache") || cacheControl.contains("must-revalidate") ||
            cacheControl.contains("max-age=0")
        ) {
            Timber.i("Response cache applied")
            originalResponse.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=" + 60)
                .build()
        } else {
            Timber.i("Response cache not applied")
            originalResponse
        }
    }
}
