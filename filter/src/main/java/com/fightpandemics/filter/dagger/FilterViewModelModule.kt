package com.fightpandemics.filter.dagger

import androidx.lifecycle.ViewModel
import com.fightpandemics.filter.ui.FilterViewModel
import com.fightpandemics.core.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/*
* created by Osaigbovo Odiase
* */
@Module
abstract class FilterViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FilterViewModel::class)
    abstract fun bindFilterViewModel(filterViewModel: FilterViewModel): ViewModel
}