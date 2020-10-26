package com.fightpandemics.login.dagger

import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.login.domain.LoginUseCase
import com.fightpandemics.login.ui.SignInEmailFragment
import com.fightpandemics.login.ui.SignInFragment
import com.fightpandemics.login.ui.SignUpFragment
import dagger.Subcomponent

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

    fun inject(signInFragment: SignInFragment)
    fun inject(signInEmailFragment: SignInEmailFragment)
    fun inject(signUpFragment: SignUpFragment)
    //fun inject(SignUpFragment: SignUpEmailFragment)
    //fun inject(CompeteProfileFragment: CompeteProfileFragment)

    fun providesLoginUseCase(): LoginUseCase
}