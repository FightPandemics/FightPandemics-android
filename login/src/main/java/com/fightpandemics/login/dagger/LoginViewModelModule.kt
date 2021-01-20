package com.fightpandemics.login.dagger

import androidx.lifecycle.ViewModel
import com.fightpandemics.core.utils.ViewModelKey
import com.fightpandemics.login.ui.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class LoginViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel
}
