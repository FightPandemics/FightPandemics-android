package com.fightpandemics.di.module

import com.fightpandemics.di.scope.ActivityScope
import com.fightpandemics.ui.home.offers.OffersContract
import com.fightpandemics.ui.home.offers.OffersPresenter
import dagger.Module
import dagger.Provides

@Module
class OffersFragmentModule(private val view: OffersContract.View) {

    @Provides
    @ActivityScope
    fun provideView() = view

    @Provides
    @ActivityScope
    fun providePresenter() = OffersPresenter(view)
}