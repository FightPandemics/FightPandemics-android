package com.fightpandemics.core.data.remote.location

import com.fightpandemics.core.data.api.FightPandemicsAPI
import com.fightpandemics.core.data.model.userlocation.LocationResponse
import com.fightpandemics.core.data.model.userlocationdetails.LocationDetails
import com.fightpandemics.core.data.model.userlocationpredictions.LocationPrediction
import retrofit2.Response
import javax.inject.Inject

class LocationRemoteDataSourceImpl @Inject constructor(
    private val fightPandemicsAPI: FightPandemicsAPI
) : LocationRemoteDataSource {

    override suspend fun getUserLocation(
        lat: Double,
        lng: Double
    ): Response<LocationResponse> {
        return fightPandemicsAPI.getUserLocation(lat, lng)
    }

    override suspend fun getLocationPredictions(
        input: String,
        sessiontoken: String
    ): Response<LocationPrediction> {
        return fightPandemicsAPI.getLocationPredictions(input, sessiontoken)
    }

    override suspend fun getLocationDetails(
        placeId: String,
        sessiontoken: String?
    ): Response<LocationDetails> {
        return fightPandemicsAPI.getLocationDetails(placeId, sessiontoken)
    }
}
