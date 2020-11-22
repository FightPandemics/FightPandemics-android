package com.fightpandemics.core.domain.repository

import com.fightpandemics.core.result.Result
import kotlinx.coroutines.flow.Flow

/*
* created by Osaigbovo Odiase
* */
interface LocationRepository {

    suspend fun getUserLocation(
        lat: String,
        lng: String
    ): Flow<Result<*>>?

    suspend fun getLocationPredictions(
        input: String
    ): Flow<Result<*>>?

    suspend fun getLocationDetails(
        placeId: String
    ): Flow<Result<*>>?
}