package com.fightpandemics.login.ui

import android.app.Dialog
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.AuthCallback
import com.auth0.android.result.Credentials
import timber.log.Timber

class Auth0CallBack(
    val failureCallBack: () -> Unit,
    val successCallBack: (Credentials) -> Unit
) : AuthCallback {
    override fun onFailure(dialog: Dialog) {
        Timber.e("LoginCallBack.onFailure : Unexpected error")
        failureCallBack.invoke()
    }

    override fun onFailure(exception: AuthenticationException) {
        Timber.e(exception)
        failureCallBack.invoke()
    }

    override fun onSuccess(credentials: Credentials) {
        Timber.e("${credentials.accessToken}")
        Timber.e("${credentials.refreshToken}")

        successCallBack.invoke(credentials)
    }
}
