package com.fightpandemics.dagger

import com.fightpandemics.dagger.scope.FeatureScope
import com.fightpandemics.data.LoginDataSource
import com.fightpandemics.ui.login.LoginActivity
import dagger.Component

/**
 * Component binding injections for the [:login] module.
 */
@FeatureScope
@Component(
    modules = [LoginModule::class, LoginViewModelModule::class],
    dependencies = [CoreComponent::class]
)
interface LoginComponent {

    // Factory to create instances of the LoginComponent
    @Component.Factory
    interface Factory {
        // Takes an instance of CoreComponent when creating an instance of LoginComponent
        fun create(coreComponent: CoreComponent): LoginComponent
    }

    fun provideLoginDataSource(): LoginDataSource

    fun inject(loginActivity: LoginActivity)
    //fun inject(loginFragment: LoginFragment)
}