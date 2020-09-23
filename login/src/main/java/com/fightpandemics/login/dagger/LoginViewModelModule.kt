package com.fightpandemics.login.dagger

import androidx.lifecycle.ViewModel
import com.fightpandemics.login.ui.LoginViewModel
import com.fightpandemics.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LoginViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(homeViewModel: LoginViewModel): ViewModel
}
