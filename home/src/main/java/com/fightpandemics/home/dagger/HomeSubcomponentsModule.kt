package com.fightpandemics.home.dagger

import com.fightpandemics.postdetails.dagger.PostDetailsComponent
import dagger.Module

@Module(
    subcomponents = [
        PostDetailsComponent::class
    ]
)
class HomeSubcomponentsModule
