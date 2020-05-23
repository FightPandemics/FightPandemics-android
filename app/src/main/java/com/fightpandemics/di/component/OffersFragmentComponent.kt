package com.fightpandemics.di.component

import com.fightpandemics.di.module.OffersFragmentModule
import com.fightpandemics.di.scope.ActivityScope
import com.fightpandemics.home.offers.OffersFragment
import dagger.Component

@ActivityScope
@Component(modules = [OffersFragmentModule::class],
    dependencies = [AppComponent::class])
interface OffersFragmentComponent {

    fun inject(allFragment: OffersFragment)
}