package com.fightpandemics.dagger

import com.fightpandemics.dagger.module.*
import com.fightpandemics.data.api.FightPandemicsAPI
import com.fightpandemics.data.prefs.PreferenceStorage
import com.fightpandemics.data.remote.PostsRemoteDataSource
import com.fightpandemics.domain.repository.PostsRepository
import dagger.Component
import javax.inject.Singleton

/*
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
    //fun provideContext(): Context
    fun provideString(): String
    fun providesPreferenceStorage(): PreferenceStorage


    fun providePostsRepository(): PostsRepository
    fun provideFightPandemicsAPI(): FightPandemicsAPI

}
