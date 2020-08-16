package com.fightpandemics.dagger.module

import androidx.lifecycle.ViewModelProvider
import com.fightpandemics.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ViewModelBuilderModule {

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
