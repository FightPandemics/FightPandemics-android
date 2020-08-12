package com.fightpandemics.data.repository

interface AppRepository {

    fun getLocalData()

    suspend fun storeLocalData()

    suspend fun getRemoteData()
}