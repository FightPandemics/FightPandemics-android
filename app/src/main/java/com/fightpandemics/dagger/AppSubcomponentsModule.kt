package com.fightpandemics.dagger

import com.fightpandemics.ui.SplashComponent
import dagger.Module

// Subcomponents are children of the Component this module is included in.
@Module(subcomponents = [SplashComponent::class])
class AppSubcomponentsModule
