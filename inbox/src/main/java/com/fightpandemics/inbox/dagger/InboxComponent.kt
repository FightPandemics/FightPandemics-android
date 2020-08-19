package com.fightpandemics.inbox.dagger

import com.fightpandemics.dagger.CoreComponent
import com.fightpandemics.dagger.scope.FeatureScope
import com.fightpandemics.inbox.ui.InboxFragment
import dagger.Component

/**
 * Component binding injections for the :inbox feature module.
 */
@FeatureScope
@Component(
    modules = [InboxModule::class],
    dependencies = [CoreComponent::class]
)
interface InboxComponent {


    fun inject(inboxFragment: InboxFragment)

}