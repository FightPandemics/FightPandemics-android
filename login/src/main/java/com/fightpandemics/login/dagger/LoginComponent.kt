package com.fightpandemics.login.dagger

import com.fightpandemics.dagger.scope.ActivityScope
import com.fightpandemics.login.ui.LoginActivity
import com.fightpandemics.login.ui.SignInFragment
import com.fightpandemics.login.ui.SignUpFragment
import dagger.Subcomponent
import javax.inject.Named

/**
 * Component binding injections for the [:login] module.
 */
@ActivityScope
@Subcomponent(
    modules = [LoginModule::class, LoginViewModelModule::class]
)
interface LoginComponent {

    // Factory that is used to create instances of the LoginComponent subcomponent
    @Subcomponent.Factory
    interface Factory {
        // Takes an instance of CoreComponent when creating an instance of LoginComponent
        fun create(): LoginComponent
    }

    @Named("Login") fun provideLoginString(): String

    fun inject(loginActivity: LoginActivity)
    fun inject(signInFragment: SignInFragment)
    fun inject(SignUpFragment: SignUpFragment)
}