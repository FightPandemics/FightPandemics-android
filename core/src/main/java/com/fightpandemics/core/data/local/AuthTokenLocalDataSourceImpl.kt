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

    override fun getUserId(): String? = preferenceStorage.userId

    override fun setUserId(userId: String?) {
        preferenceStorage.userId = userId
    }

    override fun getUserFirstName(): String? = preferenceStorage.userFirstName

    override fun setUserFirstName(userFirstName: String?) {
        preferenceStorage.userFirstName = userFirstName
    }

    override fun getUserLastName(): String? = preferenceStorage.userLastName

    override fun setUserLastName(userLastName: String?) {
        preferenceStorage.userLastName = userLastName
    }

    override fun getUserEmail(): String? = preferenceStorage.userEmail

    override fun setUserEmail(userEmail: String?) {
        preferenceStorage.userEmail = userEmail
    }

    override fun getUserOrganisation(): List<String>? = preferenceStorage.userOrganisations

    override fun setUserOrganisation(userOrganisation: List<String>?) {
        preferenceStorage.userOrganisations = userOrganisation
    }
}
