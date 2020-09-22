package com.fightpandemics.domain

import com.fightpandemics.dagger.scope.ActivityScope
import com.fightpandemics.data.CoroutinesDispatcherProvider
import com.fightpandemics.data.prefs.FightPandemicsPreferenceDataStore
import com.fightpandemics.result.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Returns whether onboarding has been completed.
 */
class OnboardingCompletedUseCase @Inject constructor(
    private val preferenceDataStore: FightPandemicsPreferenceDataStore,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : FlowUseCase<Unit, Flow<Boolean>>(dispatcherProvider.default) {

    override fun execute(parameters: Unit): Flow<Boolean> = preferenceDataStore.userOnboardingFlow
}
