package com.fightpandemics.di.component

import com.fightpandemics.di.module.HomeActivityModule
import com.fightpandemics.di.scope.ActivityScope
import com.fightpandemics.ui.activities.HomeActivity
import dagger.Component

@ActivityScope
@Component(modules = [HomeActivityModule::class],
    dependencies = [AppComponent::class])
interface HomeActivityComponent {

    fun inject(homeActivity: HomeActivity)
}