package com.fightpandemics.di.component

import com.fightpandemics.di.module.RequestsFragmentModule
import com.fightpandemics.di.scope.ActivityScope
import com.fightpandemics.home.requests.RequestsFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [RequestsFragmentModule::class],
    dependencies = [AppComponent::class]
)
interface RequestsFragmentComponent {

    fun inject(requestsFragment: RequestsFragment)
}
