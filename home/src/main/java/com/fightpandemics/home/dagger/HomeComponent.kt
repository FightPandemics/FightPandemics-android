package com.fightpandemics.home.dagger

import com.fightpandemics.dagger.AppComponent
import com.fightpandemics.dagger.CoreComponent
import com.fightpandemics.dagger.scope.FeatureScope
import com.fightpandemics.home.ui.HomeFragment
import dagger.Component

/**
 * Component binding injections for the :home feature module.
 */
@FeatureScope
@Component(
    modules = [HomeModule::class, HomeViewModelModule::class],
    dependencies = [AppComponent::class, CoreComponent::class]
)
interface HomeComponent {

    // Factory to create instances of the HomeComponent
    @Component.Factory
    interface Factory {
        // Takes an instance of AppComponent when creating an instance of HomeComponent
        fun create(appComponent: AppComponent, coreComponent: CoreComponent): HomeComponent
    }

    fun inject(homeFragment: HomeFragment)
}
