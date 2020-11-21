package com.fightpandemics.core.data.repository

import com.fightpandemics.core.data.remote.location.LocationRemoteDataSource
import com.fightpandemics.core.domain.repository.LocationRepository
import com.squareup.moshi.Moshi
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    val moshi: Moshi,
    private val locationRemoteDataSource: LocationRemoteDataSource
) : LocationRepository {


}