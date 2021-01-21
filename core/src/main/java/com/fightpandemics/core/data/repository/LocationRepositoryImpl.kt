package com.fightpandemics.core.data.repository

import com.fightpandemics.core.data.model.login.ErrorResponse
import com.fightpandemics.core.data.model.userlocation.Location
import com.fightpandemics.core.data.prefs.PreferenceStorage
import com.fightpandemics.core.data.remote.location.LocationRemoteDataSource
import com.fightpandemics.core.domain.repository.LocationRepository
import com.fightpandemics.core.result.Result
import com.fightpandemics.core.utils.parseErrorJsonResponse
import com.squareup.moshi.Moshi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import java.util.*
import javax.inject.Inject

/*
* created by Osaigbovo Odiase
* */
@ExperimentalCoroutinesApi
class LocationRepositoryImpl @Inject constructor(
    val moshi: Moshi,
    private val locationRemoteDataSource: LocationRemoteDataSource,
    private val preferenceStorage: PreferenceStorage
) : LocationRepository {

    override suspend fun getUserLocation(lat: Double, lng: Double): Flow<Result<*>> {
        return channelFlow {
            channel.offer(Result.Loading)
            val userLocationResponse = locationRemoteDataSource.getUserLocation(lat, lng)
            when {
                userLocationResponse.isSuccessful && userLocationResponse.code() == 200 -> {
                    val userCurrentLocation: Location = userLocationResponse.body()!!.location
                    channel.offer(Result.Success(userCurrentLocation))
                }
                userLocationResponse.code() == 401 -> {
                    val myError = userLocationResponse
                        .parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
            }
            awaitClose { }
        }
    }

    override suspend fun getLocationPredictions(
        input: String
    ): Flow<Result<*>> {
        val sessiontoken = getUUID()
        return channelFlow {
            channel.offer(Result.Loading)
            val userLocationPredictions =
                locationRemoteDataSource.getLocationPredictions(input, sessiontoken!!)
            when {
                userLocationPredictions.isSuccessful && userLocationPredictions.code() == 200 -> {
                    val locationPrediction =
                        userLocationPredictions
                            .body()!!
                            .predictions
                            .map { it -> it.description }
                    channel.offer(Result.Success(locationPrediction))
                }
                userLocationPredictions.code() == 401 -> {
                    val myError = userLocationPredictions
                        .parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
            }
            awaitClose { }
        }
    }

    override suspend fun getLocationDetails(
        placeId: String
    ): Flow<Result<*>>? {
        val sessiontoken = getUUID()
        return channelFlow {
            val userLocationDetails =
                locationRemoteDataSource.getLocationDetails(placeId, sessiontoken)
            when {
                userLocationDetails.isSuccessful && userLocationDetails.code() == 200 -> {
                    val locationDetails =
                        userLocationDetails
                            .body()!!
                            .location

                    channel.offer(Result.Success(locationDetails))
                }
                userLocationDetails.code() == 401 -> {
                    val myError = userLocationDetails
                        .parseErrorJsonResponse<ErrorResponse>(moshi)
                    channel.offer(Result.Error(Exception(myError?.message)))
                }
            }
            awaitClose { }
        }
    }

    private fun getUUID(): String? {
        return if (preferenceStorage.uuid.isNullOrEmpty()) {
            UUID.randomUUID().apply { preferenceStorage.uuid = this.toString() }
            preferenceStorage.uuid
        } else {
            preferenceStorage.uuid
        }
    }
}
