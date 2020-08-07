package com.fightpandemics.data.sources

import androidx.lifecycle.LiveData

abstract class MultiDataSource<LocalType, RequestType, ResponseType>
    : LocalDataSource<LocalType>, RemoteDataSource<RequestType, ResponseType> {

    private var mutableLiveData: LiveData<LocalType> = this.getLocalData()

    abstract fun mapper(remoteData: ResponseType): LocalType

    suspend fun fetch(request: RequestType) {
        val remoteData = getRemoteData(request)
        remoteData?.let {
            val localData = mapper(it)
            storeLocalData(localData)
        }
    }

    fun asLiveData() = mutableLiveData
}