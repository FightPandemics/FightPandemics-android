package com.fightpandemics.profile.dagger

import androidx.lifecycle.ViewModel
import com.fightpandemics.core.utils.ViewModelKey
import com.fightpandemics.profile.ui.profile.ProfileViewModel
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
