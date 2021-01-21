package com.fightpandemics.search.dagger

import com.fightpandemics.core.dagger.CoreComponent
import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.dagger.AppComponent
import com.fightpandemics.search.ui.SearchFragment
import dagger.Component

/**
 * Component binding injections for the :search feature module.
 */
@FeatureScope
@Component(
    modules = [SearchModule::class, SearchViewModelModule::class],
    dependencies = [AppComponent::class, CoreComponent::class]
)
interface SearchComponent {

    // Factory to create instances of the SearchComponent
    @Component.Factory
    interface Factory {
        // Takes an instance of AppComponent when creating an instance of SearchComponent
        fun create(appComponent: AppComponent, coreComponent: CoreComponent): SearchComponent
    }

    fun inject(searchFragment: SearchFragment)
}
