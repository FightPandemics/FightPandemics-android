package com.fightpandemics.dagger

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.fightpandemics.core.dagger.CoreComponent
import com.fightpandemics.core.dagger.scope.AppScope
import com.fightpandemics.filter.dagger.FilterComponent
import com.fightpandemics.login.dagger.LoginComponent
import com.fightpandemics.ui.splash.SplashComponent
import dagger.BindsInstance
import dagger.Component
import dagger.Lazy

@AppScope
@Component(
    modules = [AppModule::class, AppSubcomponentsModule::class],
    dependencies = [CoreComponent::class]
)
interface AppComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // with @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context, coreComponent: CoreComponent): AppComponent
    }

    fun splashComponent(): SplashComponent.Factory
    fun loginComponent(): LoginComponent.Factory
    fun filterComponent(): FilterComponent.Factory

    fun inject(application: Application)
}
