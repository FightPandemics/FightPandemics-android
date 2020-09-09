package com.fightpandemics.data.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

/**
 * Interceptor to cache data and maintain it for four weeks.
 * If the device is offline, stale (at most four weeks old)
 * response is fetched from the cache.
 */
class OfflineResponseCacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        if (/*!Application.hasNetwork()*/ false) {
            Timber.i("Offline cache applied")
            val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
            request = request.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .build()
        } else {
            Timber.i("Offline cache not applied")
        }
        return chain.proceed(request)
    }
}
