package com.fightpandemics.home.dagger

import androidx.lifecycle.ViewModel
import com.fightpandemics.core.utils.ViewModelKey
import com.fightpandemics.home.ui.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel
}
