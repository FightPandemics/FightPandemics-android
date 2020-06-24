package com.fightpandemics.di.module


import com.fightpandemics.core.networking.APIService
import com.fightpandemics.core.networking.network.NetworkHelper
import com.fightpandemics.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {
    @Provides
    @ApplicationScope
    fun provideAPIService(): APIService {
        return NetworkHelper().newAPIService()
    }
}