package com.fightpandemics.domain

import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.prefs.PreferenceStorage
import com.fightpandemics.core.domain.usecase.UseCase
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
