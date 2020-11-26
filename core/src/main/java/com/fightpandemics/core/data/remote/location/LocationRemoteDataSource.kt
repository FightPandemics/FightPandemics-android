package com.fightpandemics.core.data.remote.location

import com.fightpandemics.core.data.model.userlocation.LocationResponse
import com.fightpandemics.core.data.model.userlocationdetails.LocationDetails
import com.fightpandemics.core.data.model.userlocationpredictions.LocationPrediction
import retrofit2.Response

interface LocationRemoteDataSource {

    suspend fun getUserLocation(
        lat: Double,
        lng: Double
    ): Response<LocationResponse>

    suspend fun getLocationPredictions(
        input: String,
        sessiontoken: String
    ): Response<LocationPrediction>

    suspend fun getLocationDetails(
        placeId: String,
        sessiontoken: String?
    ): Response<LocationDetails>
}
