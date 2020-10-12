package com.fightpandemics.domain

import com.fightpandemics.data.CoroutinesDispatcherProvider
import com.fightpandemics.data.prefs.PreferenceStorage
import com.fightpandemics.domain.usecase.UseCase
import javax.inject.Inject

/**
 * Returns whether onboarding has been completed.
 */
class OnBoardCompletedUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val dispatcherProvider: CoroutinesDispatcherProvider,
) : UseCase<Unit, Boolean>(dispatcherProvider.io) {

    override fun execute(parameters: Unit): Boolean = preferenceStorage.onboardingCompleted
}
