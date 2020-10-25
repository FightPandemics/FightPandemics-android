package com.fightpandemics.core.data.interceptors

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AccessTokenAuthenticator : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        TODO("Not yet implemented")
    }
}