package com.fightpandemics.ui

import android.content.Context
import com.fightpandemics.dagger.scope.ActivityScope
import com.fightpandemics.data.CoroutinesDispatcherProvider
import com.fightpandemics.data.prefs.FightPandemicsPreferenceDataStore
import com.fightpandemics.data.prefs.FightPandemicsPreferenceDataStoreImpl
import com.fightpandemics.domain.OnboardingCompletedUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SplashModule {

    @ActivityScope
    @Provides
    fun provideCommonHome(): String =
        "DUPE"

    @ActivityScope
    @Provides
    fun providesOnboardingCompletedUseCase(preferenceDataStore: FightPandemicsPreferenceDataStore,
                                    dispatcherProvider: CoroutinesDispatcherProvider
    ): OnboardingCompletedUseCase = OnboardingCompletedUseCase(preferenceDataStore, dispatcherProvider)
}