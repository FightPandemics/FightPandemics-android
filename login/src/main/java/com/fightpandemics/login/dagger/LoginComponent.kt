package com.fightpandemics.login.dagger

import com.fightpandemics.dagger.AppComponent
import com.fightpandemics.dagger.CoreComponent
import com.fightpandemics.dagger.scope.FeatureScope
import com.fightpandemics.login.ui.LoginFragment
import dagger.Component

/**
 * Component binding injections for the :login feature module.
 */
@FeatureScope
@Component(
    modules = [LoginModule::class, LoginViewModelModule::class],
    dependencies = [AppComponent::class, CoreComponent::class]
)
interface LoginComponent {

    // Factory to create instances of the LoginComponent
    @Component.Factory
    interface Factory {
        // Takes an instance of AppComponent when creating an instance of LoginComponent
        fun create(appComponent: AppComponent, coreComponent: CoreComponent): LoginComponent
    }

    fun inject(loginFragment: LoginFragment)
}
