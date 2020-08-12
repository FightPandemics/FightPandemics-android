package com.fightpandemics.di.module

import com.fightpandemics.di.scope.ActivityScope
import com.fightpandemics.ui.home.all.AllContract
import com.fightpandemics.ui.home.all.AllPresenter
import dagger.Module
import dagger.Provides

@Module
class AllFragmentModule(private val view: AllContract.View) {

    @Provides
    @ActivityScope
    fun provideView() = view

    @Provides
    @ActivityScope
    fun providePresenter() = AllPresenter(view)
}