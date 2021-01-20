package com.fightpandemics.search.dagger

import androidx.lifecycle.ViewModel
import com.fightpandemics.core.utils.ViewModelKey
import com.fightpandemics.search.ui.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SearchViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel
}
