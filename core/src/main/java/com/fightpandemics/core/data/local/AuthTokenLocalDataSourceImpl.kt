package com.fightpandemics.core.data.local

import com.fightpandemics.core.data.prefs.PreferenceStorage
import javax.inject.Inject

class AuthTokenLocalDataSourceImpl @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : AuthTokenLocalDataSource {

    override fun getToken(): String? = preferenceStorage.token

    override fun setToken(token: String?) {
        preferenceStorage.token = token
    }

    override fun getUserId(): String? {
        return preferenceStorage.userId
    }

    override fun setUserId(userId: String?) {
        preferenceStorage.userId = userId
    }
}
