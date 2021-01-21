package com.fightpandemics.domain

import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.prefs.PreferenceStorage
import com.fightpandemics.core.domain.usecase.UseCase
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
