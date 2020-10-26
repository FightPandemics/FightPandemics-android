package com.fightpandemics.core.data.local

interface AuthTokenLocalDataSource {
    fun getToken(): String?
    fun setToken(token: String?)
    fun getUserId(): String?
    fun setUserId(userId: String?)
}
