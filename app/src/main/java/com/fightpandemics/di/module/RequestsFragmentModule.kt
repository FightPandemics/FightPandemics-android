package com.fightpandemics.di.module

import com.fightpandemics.di.scope.ActivityScope
import com.fightpandemics.home.requests.RequestsContract
import com.fightpandemics.home.requests.RequestsPresenter
import dagger.Module
import dagger.Provides

@Module
class RequestsFragmentModule(private val view: RequestsContract.View) {

    @Provides
    @ActivityScope
    fun provideView() = view

    @Provides
    @ActivityScope
    fun providePresenter() = RequestsPresenter(view)
}
