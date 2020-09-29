package com.fightpandemics.ui.splash

import com.fightpandemics.dagger.scope.ActivityScope
import com.fightpandemics.data.CoroutinesDispatcherProvider
import com.fightpandemics.data.prefs.FightPandemicsPreferenceDataStore
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