package com.fightpandemics.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.fightpandemics.dagger.scope.ActivityScope
import com.fightpandemics.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

/**
 * Module where classes needed for app launch are defined.
 */
@Module
abstract class SplashViewModelModule {

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [SplashViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel
}