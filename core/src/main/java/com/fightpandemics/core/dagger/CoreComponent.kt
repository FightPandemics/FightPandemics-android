package com.fightpandemics.core.dagger

import com.fightpandemics.core.dagger.module.ContextModule
import com.fightpandemics.core.dagger.module.DataModule
import com.fightpandemics.core.dagger.module.DatabaseModule
import com.fightpandemics.core.dagger.module.NetworkModule
import com.fightpandemics.core.dagger.module.RemoteModule
import com.fightpandemics.core.dagger.module.SharedPreferencesModule
import com.fightpandemics.core.dagger.module.ViewModelBuilderModule
import com.fightpandemics.core.data.api.FightPandemicsAPI
import com.fightpandemics.core.data.prefs.PreferenceStorage
import com.fightpandemics.core.domain.repository.LocationRepository
import com.fightpandemics.core.domain.repository.LoginRepository
import com.fightpandemics.core.domain.repository.PostsRepository
import com.fightpandemics.core.domain.repository.ProfileRepository
import dagger.Component
import javax.inject.Singleton

/*
*
* created by Osaigbovo Odiase
*
* @Singleton ensures the same copy of all the dependencies is injected to anything that needs it.
* @Component makes Dagger create a graph of dependencies.
* The "modules" attribute tells Dagger what Modules to include when building the graph
* */
@Singleton
@Component(
    modules = [
        ContextModule::class,
        DataModule::class,
        DatabaseModule::class,
        NetworkModule::class,
        RemoteModule::class,
        SharedPreferencesModule::class,
        ViewModelBuilderModule::class
    ]
)
interface CoreComponent {

    // Factory that is used to create instances of this subcomponent
    @Component.Factory
    interface Factory {
        fun create(
            contextModule: ContextModule,
            sharedPreferencesModule: SharedPreferencesModule,
        ): CoreComponent
    }

    // The return type of functions inside the component interface is what can be provided from the container
    // fun provideContext(): Context
    fun providesPreferenceStorage(): PreferenceStorage

    fun providePostsRepository(): PostsRepository
    fun provideLoginRepository(): LoginRepository
    fun provideLocationRepository(): LocationRepository
    fun provideFightPandemicsAPI(): FightPandemicsAPI
    fun providesProfileRepository(): ProfileRepository
}
