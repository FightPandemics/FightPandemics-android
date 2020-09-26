package com.fightpandemics.ui.splash

import com.fightpandemics.dagger.scope.ActivityScope
import com.fightpandemics.ui.onboarding.OnboardFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [SplashModule::class, SplashViewModelModule::class])
interface SplashComponent {

    // Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): SplashComponent
    }

    // this function tells Dagger that SplashActivity wants to access the graph and requests injection.
    // Dagger needs to satisfy all the dependencies that SplashActivity requires
    fun inject(splashActivity: SplashActivity)
    fun inject(splashFragment: SplashFragment)
    fun inject(onboardFragment: OnboardFragment)
}