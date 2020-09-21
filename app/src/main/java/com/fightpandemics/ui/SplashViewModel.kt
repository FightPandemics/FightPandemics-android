package com.fightpandemics.ui

import androidx.lifecycle.ViewModel
import com.fightpandemics.dagger.scope.ActivityScope
import com.fightpandemics.data.CoroutinesDispatcherProvider
import com.fightpandemics.domain.OnboardingCompletedUseCase
import javax.inject.Inject

/**
 * Logic for determining which screen to send users to on app launch.
 */
@ActivityScope
class SplashViewModel @Inject constructor(
    onboardingCompletedUseCase: OnboardingCompletedUseCase/*,
    private val dispatcherProvider: CoroutinesDispatcherProvider*/
) : ViewModel() {

}

enum class LaunchDestination {
    ONBOARDING,
    MAIN_ACTIVITY
}