package com.fightpandemics.search.dagger

import com.fightpandemics.dagger.CoreComponent
import com.fightpandemics.dagger.scope.FeatureScope
import com.fightpandemics.search.ui.SearchFragment
import dagger.Component

/**
 * Component binding injections for the :search feature module.
 */
@FeatureScope
@Component(
    modules = [SearchModule::class],
    dependencies = [CoreComponent::class]
)
interface SearchComponent {

    fun inject(searchFragment: SearchFragment)
}