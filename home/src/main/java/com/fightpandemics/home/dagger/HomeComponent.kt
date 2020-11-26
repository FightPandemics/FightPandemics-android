package com.fightpandemics.home.dagger

import com.fightpandemics.core.dagger.CoreComponent
import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.dagger.AppComponent
import com.fightpandemics.home.ui.DeleteDialogFragment
import com.fightpandemics.home.ui.HomeFragment
import com.fightpandemics.home.ui.HomeOptionsBottomSheetFragment
import com.fightpandemics.home.ui.tabs.all.HomeAllFragment
import com.fightpandemics.home.ui.tabs.offers.HomeOfferFragment
import com.fightpandemics.home.ui.tabs.requests.HomeRequestFragment
import com.fightpandemics.postdetails.dagger.PostDetailsComponent
import dagger.Component

/**
 * Component binding injections for the :home feature module.
 */
@FeatureScope
@Component(
    modules = [HomeModule::class, HomeViewModelModule::class, HomeSubcomponentsModule::class],
    dependencies = [AppComponent::class, CoreComponent::class]
)
interface HomeComponent {

    // Factory to create instances of the HomeComponent
    @Component.Factory
    interface Factory {
        // Takes an instance of AppComponent when creating an instance of HomeComponent
        fun create(appComponent: AppComponent, coreComponent: CoreComponent): HomeComponent
    }

    fun postDetailsComponent(): PostDetailsComponent.Factory

    fun inject(homeFragment: HomeFragment)
    fun inject(homeAllFragment: HomeAllFragment)
    fun inject(homeOfferFragment: HomeOfferFragment)
    fun inject(homeRequestFragment: HomeRequestFragment)
    fun inject(homeOptionsBottomSheetFragment: HomeOptionsBottomSheetFragment)
    fun inject(deleteDialogFragment: DeleteDialogFragment)
}
