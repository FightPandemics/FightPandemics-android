package com.fightpandemics.profile.dagger

import com.fightpandemics.dagger.AppComponent
import com.fightpandemics.dagger.CoreComponent
import com.fightpandemics.dagger.scope.FeatureScope
import com.fightpandemics.profile.ui.ProfileFragment
import dagger.Component

/**
 * Component binding injections for the :profile feature module.
 */
@FeatureScope
@Component(
    modules = [ProfileModule::class, ProfileViewModelModule::class],
    dependencies = [AppComponent::class, CoreComponent::class]
)
interface ProfileComponent {

    // Factory to create instances of the ProfileComponent
    @Component.Factory
    interface Factory {
        // Takes an instance of AppComponent when creating an instance of ProfileComponent
        fun create(appComponent: AppComponent, coreComponent: CoreComponent): ProfileComponent
    }

    fun inject(profileFragment: ProfileFragment)
}
