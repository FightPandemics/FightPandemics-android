package com.fightpandemics.domain

import com.fightpandemics.data.CoroutinesDispatcherProvider
import com.fightpandemics.data.prefs.FightPandemicsPreferenceDataStore
import com.fightpandemics.result.Result
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Records whether onboarding has been completed.
 */
class OnBoardCompleteActionUseCase @Inject constructor(
    private val preferenceDataStore: FightPandemicsPreferenceDataStore,
    private val dispatcherProvider: CoroutinesDispatcherProvider,
) : UseCase<Boolean, Unit>(dispatcherProvider.io) {

    override fun execute(completed: Boolean) {
        preferenceDataStore.onboardingCompleted = completed
    }
}