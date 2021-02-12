package com.fightpandemics.core.data.interceptors

import com.fightpandemics.core.data.api.FightPandemicsAPI
import com.fightpandemics.core.data.local.AuthTokenLocalDataSource
import com.fightpandemics.core.data.model.login.RefreshToken
import com.fightpandemics.core.data.model.login.RefreshTokenResponse
import com.fightpandemics.core.domain.repository.LoginRepository
import javax.inject.Inject
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

// Authenticator is only called when there is a 401 HTTP unauthorized error,
class AccessTokenAuthenticator @Inject constructor(
    private val authTokenLocalDataSource: AuthTokenLocalDataSource,
    private val loginRepository: LoginRepository
) : Authenticator {

    /*
    * Note that the code to retrieve the token should be synchronous.
    * This is part of the contract of Authenticator.
    * The code inside the Authenticator will run off the main thread
    * */
    override fun authenticate(route: Route?, response: Response): Request? = when {
        response.retryCount > 4 -> null
        /*
        * Checking if we hit 401 on our refresh token request, then we definitely need to log out
        * the user cause the refresh token that the call was made is expired and we need to get
        * another one on log in.
        * */
        response.networkResponse?.request?.url.toString() ==
                FightPandemicsAPI.RELEASE_API_ENDPOINT + "api/auth/refresh-token" && response.code == 401 -> {
            /*logOutAuthenticator()*/ null
        }
        response.networkResponse?.request?.url.toString() ==
                FightPandemicsAPI.RELEASE_API_ENDPOINT + "api/auth/login" -> {
            null
        }
        response.networkResponse?.request?.url.toString() ==
                FightPandemicsAPI.RELEASE_API_ENDPOINT + "api/auth/change-password" -> {
            null
        }
        else -> response.createSignedRequest()
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

    private fun Response.createSignedRequest(): Request? {
        val tokenResponse = loginRepository
            .refreshToken(RefreshToken(authTokenLocalDataSource.getToken()!!)) // use getRefreshToken

        authTokenLocalDataSource.setToken(tokenResponse?.access_token)
        // authTokenLocalDataSource.setRefreshToken(tokenResponse?.refresh_token)

        return this.request.signWithToken(tokenResponse)
    }

    private fun Request.signWithToken(refreshTokenResponse: RefreshTokenResponse?) =
        newBuilder()
            .removeHeader("Authorization")
            .addHeader("Authorization", "Bearer ${refreshTokenResponse?.access_token}")
            .build()
}
