package com.fightpandemics.ui.splash

import androidx.lifecycle.*
import com.fightpandemics.dagger.scope.ActivityScope
import com.fightpandemics.domain.OnboardingCompletedUseCase
import com.fightpandemics.result.Event
import javax.inject.Inject

/**
 * Logic for determining which screen to send users to on app launch.
 */
@ActivityScope
class SplashViewModel @Inject constructor(
    onboardingCompletedUseCase: OnboardingCompletedUseCase
) : ViewModel() {

    //private val onboardingCompletedResult: LiveData<Result<Flow<Boolean>>> = liveData { emit(onboardingCompletedUseCase(Unit)) }

    private val onboardingCompletedResults = onboardingCompletedUseCase.execute(Unit).asLiveData()

    val launchDestination = onboardingCompletedResults.map {
        if (!it) {
            Event(LaunchDestination.ONBOARDING)
        } else {
            Event(LaunchDestination.MAIN_ACTIVITY)
        }
    }
}

enum class LaunchDestination {
    ONBOARDING,
    MAIN_ACTIVITY
}