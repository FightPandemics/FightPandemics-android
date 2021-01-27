package com.fightpandemics.home.dagger

import androidx.lifecycle.ViewModel
import com.fightpandemics.core.utils.ViewModelKey
import com.fightpandemics.home.ui.PostDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PostDetailViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PostDetailsViewModel::class)
    abstract fun bindHomeViewModel(postDetailsViewModel: PostDetailsViewModel): ViewModel
}