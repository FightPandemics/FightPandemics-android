package com.fightpandemics.di.component

import com.fightpandemics.di.module.HomeActivityMvpModule
import com.fightpandemics.di.scope.ActivityScope
import com.fightpandemics.home.HomeActivity
import dagger.Component

@ActivityScope
@Component(modules = [HomeActivityMvpModule::class],
    dependencies = [AppComponent::class])
interface HomeActivityComponent {

    fun injectHomeActivity(homeActivity: HomeActivity)
}