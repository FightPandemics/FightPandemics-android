package com.fightpandemics.ui.splash

import androidx.lifecycle.*
import com.fightpandemics.dagger.scope.ActivityScope
import com.fightpandemics.domain.OnBoardCompletedUseCase
import com.fightpandemics.result.Event
import com.fightpandemics.result.data
import javax.inject.Inject

/**
 * Logic for determining which screen to send users to on app launch.
 */
@ActivityScope
class SplashViewModel @Inject constructor(
    onBoardCompletedUseCase: OnBoardCompletedUseCase,
) : ViewModel() {

    private val onboardingCompletedResult = liveData { emit(onBoardCompletedUseCase(Unit)) }

    val launchDestination = onboardingCompletedResult.map {
        if (!it.data!!) {
            Event(LaunchDestination.ONBOARD)
        } else {
            Event(LaunchDestination.MAIN_ACTIVITY)
        }
    }
}

enum class LaunchDestination {
    ONBOARD,
    MAIN_ACTIVITY
}