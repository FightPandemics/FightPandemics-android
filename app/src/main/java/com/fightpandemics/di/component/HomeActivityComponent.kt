package com.fightpandemics.di.component

import com.fightpandemics.di.module.HomeActivityModule
import com.fightpandemics.di.scope.ActivityScope
import com.fightpandemics.home.HomeActivity
import dagger.Component

@ActivityScope
@Component(modules = [HomeActivityModule::class],
    dependencies = [AppComponent::class])
interface HomeActivityComponent {

    fun inject(homeActivity: HomeActivity)
}