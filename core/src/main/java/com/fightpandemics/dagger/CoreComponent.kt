package com.fightpandemics.dagger

import android.content.Context
import android.content.SharedPreferences
import com.fightpandemics.dagger.module.ContextModule
import com.fightpandemics.dagger.module.CoreModule
import com.fightpandemics.dagger.module.DatabaseModule
import com.fightpandemics.dagger.module.NetworkModule
import com.fightpandemics.dagger.module.SharedPreferencesModule
import com.fightpandemics.dagger.module.ViewModelBuilderModule
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
        CoreModule::class,
        ContextModule::class,
        DatabaseModule::class,
        NetworkModule::class,
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
            sharedPreferencesModule: SharedPreferencesModule
        ): CoreComponent
    }

    // The return type of functions inside the component interface is what can be provided from the container
    fun provideContext(): Context
    fun provideSharedPreferences(): SharedPreferences
    // fun provideRetrofitService(): RequestInterface
    // fun provideRepository(): Repository
}
