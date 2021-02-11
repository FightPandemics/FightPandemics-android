package com.fightpandemics.core.data.local

interface AuthTokenLocalDataSource {
    fun getToken(): String?
    fun setToken(token: String?)

    fun getUserId(): String?
    fun setUserId(userId: String?)

    fun getUserFirstName(): String?
    fun setUserFirstName(userFirstName: String?)

    fun getUserLastName(): String?
    fun setUserLastName(userLastName: String?)

    fun getUserEmail(): String?
    fun setUserEmail(userEmail: String?)

    fun getUserOrganisation(): List<String>?
    fun setUserOrganisation(userOrganisation: List<String>?)
}
