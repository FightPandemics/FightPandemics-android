package com.fightpandemics.di.component

import com.fightpandemics.di.module.AllFragmentModule
import com.fightpandemics.di.scope.ActivityScope
import com.fightpandemics.home.all.AllFragment
import dagger.Component

@ActivityScope
@Component(modules = [AllFragmentModule::class],
    dependencies = [AppComponent::class])
interface AllFragmentComponent {

    fun inject(allFragment: AllFragment)
}
