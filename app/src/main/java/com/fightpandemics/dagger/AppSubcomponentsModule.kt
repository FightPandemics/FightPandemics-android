package com.fightpandemics.dagger

import com.fightpandemics.createpost.dagger.CreatePostComponent
import com.fightpandemics.filter.dagger.FilterComponent
import com.fightpandemics.login.dagger.LoginComponent
import com.fightpandemics.ui.splash.SplashComponent
import dagger.Module

// Subcomponents are children of the Component this module is included in.
@Module(
    subcomponents = [
        SplashComponent::class,
        LoginComponent::class,
        FilterComponent::class,
        CreatePostComponent::class
    ]
)
class AppSubcomponentsModule
