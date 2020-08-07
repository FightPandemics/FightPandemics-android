package com.fightpandemics.data.sources

interface RemoteDataSource<RequestType, ResponseType> {

    suspend fun getRemoteData(request: RequestType): ResponseType?
}