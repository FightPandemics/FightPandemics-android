package com.fightpandemics.di.component

import android.content.Context
import com.fightpandemics.di.context.ActivityContext
import com.fightpandemics.di.module.HomeActivityContextModule
import com.fightpandemics.di.module.HomeActivityMvpModule
import com.fightpandemics.di.scope.ActivityScope
import com.fightpandemics.home.HomeActivity
import dagger.Component

@ActivityScope
@Component(modules = [HomeActivityContextModule::class, HomeActivityMvpModule::class],
    dependencies = [AppComponent::class])
interface HomeActivityComponent {

    @ActivityContext
    fun getContext(): Context

    fun injectHomeActivity(homeActivity: HomeActivity)
}