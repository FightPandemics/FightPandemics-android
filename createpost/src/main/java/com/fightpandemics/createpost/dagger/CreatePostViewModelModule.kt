package com.fightpandemics.createpost.dagger

import androidx.lifecycle.ViewModel
import com.fightpandemics.core.utils.ViewModelKey
import com.fightpandemics.createpost.ui.CreatePostViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/*
* created by Osaigbovo Odiase
* */
@Module
internal abstract class CreatePostViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CreatePostViewModel::class)
    abstract fun bindCreatePostViewModel(createPostViewModel: CreatePostViewModel): ViewModel
}