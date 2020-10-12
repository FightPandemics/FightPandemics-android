package com.fightpandemics.domain

import com.fightpandemics.dagger.scope.ActivityScope
import com.fightpandemics.data.CoroutinesDispatcherProvider
import com.fightpandemics.data.prefs.PreferenceStorage
import com.fightpandemics.domain.usecase.UseCase
import javax.inject.Inject

/**
 * Records whether onboarding has been completed.
 */
@ActivityScope
class OnBoardCompleteActionUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : UseCase<Boolean, Unit>(dispatcherProvider.io) {

    override fun execute(completed: Boolean) {
        preferenceStorage.onboardingCompleted = completed
    }
}