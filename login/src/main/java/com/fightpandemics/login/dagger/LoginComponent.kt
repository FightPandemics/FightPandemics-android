package com.fightpandemics.login.dagger

import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.login.domain.LoginUseCase
import com.fightpandemics.login.ui.signin.SignInFragment
import com.fightpandemics.login.ui.profile.CompleteProfileFragment
import com.fightpandemics.login.ui.signin.SignInEmailFragment
import com.fightpandemics.login.ui.verify.VerifyEmailFragment
import com.fightpandemics.login.ui.signup.SignUpEmailFragment
import com.fightpandemics.login.ui.signup.SignUpFragment
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
    fun inject(SignUpFragment: SignUpEmailFragment)
    fun inject(verifyEmailFragment: VerifyEmailFragment)
    fun inject(completeProfileFragment: CompleteProfileFragment)
    fun providesLoginUseCase(): LoginUseCase

}