package com.fightpandemics.domain

import com.fightpandemics.data.CoroutinesDispatcherProvider
import com.fightpandemics.data.prefs.FightPandemicsPreferenceDataStore
import javax.inject.Inject

/**
 * Returns whether onboarding has been completed.
 */
class OnBoardCompletedUseCase @Inject constructor(
    private val preferenceDataStore: FightPandemicsPreferenceDataStore,
    private val dispatcherProvider: CoroutinesDispatcherProvider,
) : UseCase<Unit, Boolean>(dispatcherProvider.io) {

    override fun execute(parameters: Unit): Boolean = preferenceDataStore.onboardingCompleted
}
