package com.fightpandemics.di.component

import com.fightpandemics.core.networking.APIManager
import com.fightpandemics.di.module.NetworkModule
import com.fightpandemics.di.scope.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = [NetworkModule::class])
interface NetworkComponent {
    fun inject(apiManager: APIManager)
}