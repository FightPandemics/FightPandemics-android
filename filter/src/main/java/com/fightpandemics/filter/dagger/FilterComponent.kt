package com.fightpandemics.filter.dagger

import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.filter.ui.FilterFragment
import dagger.Subcomponent

/**
 * created by Osaigbovo Odiase
 *
 * Component binding injections for the [:filter] module.
 */
@ActivityScope
@Subcomponent(
    modules = [FilterModule::class, FilterViewModelModule::class]
)
interface FilterComponent {

    // Factory that is used to create instances of the filterComponent subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): FilterComponent
    }

    fun inject(filterFragment: FilterFragment)
}
