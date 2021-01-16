package com.fightpandemics.ui.splash

import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.core.data.CoroutinesDispatcherProvider
import com.fightpandemics.core.data.prefs.FightPandemicsPreferenceDataStore
import com.fightpandemics.domain.OnBoardCompleteActionUseCase
import com.fightpandemics.domain.OnBoardCompletedUseCase
import dagger.Module
import dagger.Provides

@Module
class SplashModule {

    @ActivityScope
    @Provides
    fun providesOnBoardCompletedUseCase(
        preferenceDataStore: FightPandemicsPreferenceDataStore,
        dispatcherProvider: CoroutinesDispatcherProvider,
    ): OnBoardCompletedUseCase = OnBoardCompletedUseCase(preferenceDataStore, dispatcherProvider)

    @ActivityScope
    @Provides
    fun providesOnBoardCompleteActionUseCase(
        preferenceDataStore: FightPandemicsPreferenceDataStore,
        dispatcherProvider: CoroutinesDispatcherProvider,
    ): OnBoardCompleteActionUseCase = OnBoardCompleteActionUseCase(preferenceDataStore, dispatcherProvider)
}
