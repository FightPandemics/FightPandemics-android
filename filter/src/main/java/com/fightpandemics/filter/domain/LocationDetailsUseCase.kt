package com.fightpandemics.filter.domain

import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.domain.repository.LocationRepository
import com.fightpandemics.core.domain.usecase.FlowUseCase
import com.fightpandemics.core.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/*
* created by Osaigbovo Odiase
* */
class LocationDetailsUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    dispatcherProvider: CoroutinesDispatcherProvider,
) : FlowUseCase<String?, Any?>(dispatcherProvider.default) {

    override suspend fun execute(parameters: String?): Flow<Result<Any?>> {
        return locationRepository.getLocationDetails(parameters!!)!!.map {
            when (it) {
                is Result.Success -> it
                is Result.Error -> it
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }
}
