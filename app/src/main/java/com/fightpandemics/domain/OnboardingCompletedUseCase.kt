package com.fightpandemics.domain

import com.fightpandemics.data.CoroutinesDispatcherProvider
import com.fightpandemics.data.prefs.PreferenceStorage
import javax.inject.Inject

/**
 * Returns whether onboarding has been completed.
 */
open class OnboardingCompletedUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : UseCase<Unit, Boolean>(dispatcherProvider.default) {

    override fun execute(parameters: Unit): Boolean = preferenceStorage.onboardingCompleted
}
