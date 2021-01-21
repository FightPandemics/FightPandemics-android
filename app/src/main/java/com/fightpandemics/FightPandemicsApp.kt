package com.fightpandemics

import android.app.Application
import android.content.Context
import com.fightpandemics.core.dagger.CoreComponent
import com.fightpandemics.core.dagger.CoreComponentProvider
import com.fightpandemics.core.dagger.DaggerCoreComponent
import com.fightpandemics.core.dagger.module.ContextModule
import com.fightpandemics.core.dagger.module.SharedPreferencesModule
import com.fightpandemics.createpost.dagger.CreatePostComponent
import com.fightpandemics.createpost.dagger.CreatePostComponentProvider
import com.fightpandemics.dagger.AppComponent
import com.fightpandemics.dagger.DaggerAppComponent
import com.fightpandemics.filter.dagger.FilterComponent
import com.fightpandemics.filter.dagger.FilterComponentProvider
import com.fightpandemics.login.dagger.LoginComponent
import com.fightpandemics.login.dagger.LoginComponentProvider
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

open class FightPandemicsApp :
    Application(),
    CoreComponentProvider,
    LoginComponentProvider,
    FilterComponentProvider,
    CreatePostComponentProvider {

    override fun onCreate() {
        // ThreeTenBP for times and dates, called before super to be available for objects
        AndroidThreeTen.init(this)

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
                SharedPreferencesModule(this)
            )
    }

    override fun provideLoginComponent(): LoginComponent {
        return appComponent.loginComponent().create()
    }

    override fun provideFilterComponent(): FilterComponent {
        return appComponent.filterComponent().create()
    }

    override fun provideCreatePostComponent(): CreatePostComponent {
        return appComponent.createPostComponent().create()
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
