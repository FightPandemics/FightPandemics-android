package com.fightpandemics.dagger

import com.fightpandemics.login.dagger.LoginComponent
import dagger.Module

// Subcomponents are children of the Component this module is included in.
@Module(subcomponents = [LoginComponent::class])
class AppSubcomponentsModule
