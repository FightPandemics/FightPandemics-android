package com.fightpandemics.filter.dagger

import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.domain.repository.LocationRepository
import com.fightpandemics.filter.domain.LocationDetailsUseCase
import com.fightpandemics.filter.domain.LocationPredictionsUseCase
import com.fightpandemics.filter.domain.UserLocationUseCase
import dagger.Module
import dagger.Provides

/**
 * created by Osaigbovo Odiase
 *
 * Dagger module providing stuff for [:filter] module.
 */
@Module
class FilterModule {

    @Provides
    fun provideUserLocationUseCase(
        locationRepository: LocationRepository,
        dispatcherProvider: CoroutinesDispatcherProvider,
    ): UserLocationUseCase = UserLocationUseCase(locationRepository, dispatcherProvider)

    @Provides
    fun provideLocationPredictionsUseCase(
        locationRepository: LocationRepository,
        dispatcherProvider: CoroutinesDispatcherProvider,
    ): LocationPredictionsUseCase =
        LocationPredictionsUseCase(locationRepository, dispatcherProvider)

    @Provides
    fun provideLocationDetailsUseCase(
        locationRepository: LocationRepository,
        dispatcherProvider: CoroutinesDispatcherProvider,
    ): LocationDetailsUseCase = LocationDetailsUseCase(locationRepository, dispatcherProvider)
}
