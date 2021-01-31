package com.fightpandemics.profile.dagger

import com.fightpandemics.core.dagger.CoreComponent
import com.fightpandemics.core.dagger.scope.FeatureScope
import com.fightpandemics.dagger.AppComponent
import com.fightpandemics.profile.ui.ChangeEmailFragment
import com.fightpandemics.profile.ui.ChangeGoalFragment
import com.fightpandemics.profile.ui.ChangeLocationFragment
import com.fightpandemics.profile.ui.profile.EditAccountFragment
import com.fightpandemics.profile.ui.profile.EditProfileAboutFragment
import com.fightpandemics.profile.ui.profile.EditProfileFragment
import com.fightpandemics.profile.ui.profile.EditProfileNameFragment
import com.fightpandemics.profile.ui.profile.EditProfileSocialFragment
import com.fightpandemics.profile.ui.profile.IndivProfileSettings
import com.fightpandemics.profile.ui.profile.ProfileFragment
import dagger.Component

/**
 * Component binding injections for the :profile feature module.
 */
@FeatureScope
@Component(
    modules = [ProfileModule::class, ProfileViewModelModule::class],
    dependencies = [AppComponent::class, CoreComponent::class]
)
interface ProfileComponent {

    // Factory to create instances of the ProfileComponent
    @Component.Factory
    interface Factory {
        // Takes an instance of AppComponent when creating an instance of ProfileComponent
        fun create(appComponent: AppComponent, coreComponent: CoreComponent): ProfileComponent
    }

    fun inject(profileFragment: ProfileFragment)
    fun inject(editProfileFragment: EditProfileFragment)
    fun inject(editProfileNameFragment: EditProfileNameFragment)
    fun inject(editProfileAboutFragment: EditProfileAboutFragment)
    fun inject(editProfileSocialFragment: EditProfileSocialFragment)
    fun inject(individualProfileSettings: IndivProfileSettings)
    fun inject(editAccountFragment: EditAccountFragment)
    fun inject(changeGoalFragment: ChangeGoalFragment)
    fun inject(changeEmailFragment: ChangeEmailFragment)
    fun inject(changeLocationFragment: ChangeLocationFragment)
}
