package com.fightpandemics.home.dagger

import com.fightpandemics.dagger.AppComponent
import com.fightpandemics.dagger.CoreComponent
import com.fightpandemics.dagger.scope.FeatureScope
import com.fightpandemics.data.CoroutinesDispatcherProvider
import com.fightpandemics.domain.repository.PostsRepository
import com.fightpandemics.home.domain.LoadPostsUseCase
import com.fightpandemics.home.ui.HomeFragment
import com.fightpandemics.home.ui.tabs.all.HomeAllFragment
import com.fightpandemics.home.ui.tabs.offers.HomeOfferFragment
import com.fightpandemics.home.ui.tabs.requests.HomeRequestFragment
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
    fun inject(homeAllFragment: HomeAllFragment)
    fun inject(homeOfferFragment: HomeOfferFragment)
    fun inject(homeRequestFragment: HomeRequestFragment)
}
