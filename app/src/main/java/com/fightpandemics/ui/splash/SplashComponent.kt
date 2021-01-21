package com.fightpandemics.ui.splash

import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.domain.OnBoardCompleteActionUseCase
import com.fightpandemics.domain.OnBoardCompletedUseCase
import com.fightpandemics.ui.onboarding.OnBoardFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [SplashModule::class, SplashViewModelModule::class])
interface SplashComponent {

    // Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): SplashComponent
    }

    fun inject(splashActivity: SplashActivity)
    fun inject(splashFragment: SplashFragment)
    fun inject(onBoardFragment: OnBoardFragment)

    fun providesOnBoardCompleteActionUseCase(): OnBoardCompleteActionUseCase
    fun providesOnBoardCompletedUseCase(): OnBoardCompletedUseCase
}
