package com.fightpandemics.filter.domain

import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.model.userlocation.LocationRequest
import com.fightpandemics.core.domain.repository.LocationRepository
import com.fightpandemics.core.domain.usecase.FlowUseCase
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/*
* created by Osaigbovo Odiase
* */
@ActivityScope
class UserLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    dispatcherProvider: CoroutinesDispatcherProvider,
) : FlowUseCase<LocationRequest, Any?>(dispatcherProvider.default) {

    override suspend fun execute(parameters: LocationRequest?): Flow<Result<Any?>> {
        return locationRepository.getUserLocation(parameters!!.lat, parameters.lng)!!.map {
            when (it) {
                is Result.Loading -> it
                is Result.Success -> it
                is Result.Error -> it
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }
}
