package com.fightpandemics

import android.app.Application
import android.content.Context
import com.fightpandemics.dagger.AppComponent
import com.fightpandemics.dagger.CoreComponent
import com.fightpandemics.dagger.CoreComponentProvider
import com.fightpandemics.dagger.DaggerAppComponent
import com.fightpandemics.dagger.DaggerCoreComponent
import com.fightpandemics.dagger.module.ContextModule
import com.fightpandemics.dagger.module.SharedPreferencesModule
import timber.log.Timber

open class FightPandemicsApp : Application(), CoreComponentProvider {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        Timber.w("Creating our Application")
    }

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    private fun initializeComponent(): AppComponent {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        return DaggerAppComponent
            .factory()
            .create(applicationContext, provideCoreComponent())
    }

    override fun provideCoreComponent(): CoreComponent {
        return DaggerCoreComponent
            .factory()
            .create(
                ContextModule(this),
                SharedPreferencesModule(this, "name")
            ) // todo 3 - make name a const
    }

    companion object {
        @JvmStatic
        fun appComponent(context: Context) =
            (context.applicationContext as FightPandemicsApp).appComponent

        @JvmStatic
        fun coreComponent(context: Context) =
            (context.applicationContext as FightPandemicsApp).provideCoreComponent()
    }
}
