package com.fightpandemics.profile.dagger

import androidx.lifecycle.ViewModel
import com.fightpandemics.profile.ui.ProfileViewModel
import com.fightpandemics.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProfileViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel
}
