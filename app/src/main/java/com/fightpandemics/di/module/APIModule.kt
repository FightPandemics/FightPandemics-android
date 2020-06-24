package com.fightpandemics.di.module

import android.content.Context
import com.fightpandemics.core.networking.APIManager
import com.fightpandemics.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import rx.subscriptions.CompositeSubscription

@Module(includes = [AppModule::class])
class APIModule {
    @Provides
    @ApplicationScope
    fun providesApiManager(context: Context?): APIManager {
        return APIManager(context)
    }

    @Provides
    @ApplicationScope
    fun provideSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }
}