package com.fightpandemics.profile.dagger

import com.fightpandemics.dagger.CoreComponent
import com.fightpandemics.dagger.scope.FeatureScope
import com.fightpandemics.profile.ui.ProfileFragment
import dagger.Component

/**
 * Component binding injections for the :profile feature module.
 */
@FeatureScope
@Component(
    modules = [ProfileModule::class],
    dependencies = [CoreComponent::class]
)
interface ProfileComponent {

    fun inject(profileFragment: ProfileFragment)
}