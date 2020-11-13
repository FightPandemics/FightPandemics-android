package com.fightpandemics.inbox.dagger

import com.fightpandemics.dagger.AppComponent
import com.fightpandemics.dagger.CoreComponent
import com.fightpandemics.dagger.scope.FeatureScope
import com.fightpandemics.inbox.ui.InboxFragment
import dagger.Component

/**
 * Component binding injections for the :inbox feature module.
 */
@FeatureScope
@Component(
    modules = [InboxModule::class, InboxViewModelModule::class],
    dependencies = [AppComponent::class, CoreComponent::class]
)
interface InboxComponent {

    // Factory to create instances of the InboxComponent
    @Component.Factory
    interface Factory {
        // Takes an instance of AppComponent when creating an instance of InboxComponent
        fun create(appComponent: AppComponent, coreComponent: CoreComponent): InboxComponent
    }

    fun inject(inboxFragment: InboxFragment)
}
