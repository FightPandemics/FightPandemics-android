package com.fightpandemics.core.data.interceptors

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

// Authenticator is only called when there is a 401 HTTP unauthorized error,
class AccessTokenAuthenticator : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? = when {
        response.retryCount > 4 -> null
        else -> null
    }

    private val Response.retryCount: Int
        get() {
            var currentResponse = priorResponse
            var result = 0
            while (currentResponse != null) {
                result++
                currentResponse = currentResponse.priorResponse
            }
            return result
        }
}
