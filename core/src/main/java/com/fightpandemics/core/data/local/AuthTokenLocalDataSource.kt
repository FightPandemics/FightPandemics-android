package com.fightpandemics.core.data.local

interface AuthTokenLocalDataSource {
    fun getToken(): String?
    fun setToken(token: String?)
}
