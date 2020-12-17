package com.fightpandemics.profile.dagger

import android.app.Activity
import androidx.fragment.app.Fragment
import com.fightpandemics.FightPandemicsApp
import com.fightpandemics.core.dagger.CoreComponentProvider
import com.fightpandemics.profile.ui.profile.EditProfileFragment
import com.fightpandemics.profile.ui.profile.EditProfileNameFragment
import com.fightpandemics.profile.ui.profile.ProfileFragment

fun inject(fragment: ProfileFragment) {
    val appComponent =
        (fragment.requireActivity().applicationContext as FightPandemicsApp).appComponent

    DaggerProfileComponent
        .factory()
        .create(appComponent, fragment.coreComponent())
        .inject(fragment)
}

fun inject(editProfileFragment: EditProfileFragment) {
    val appComponent =
        (editProfileFragment.requireActivity().applicationContext as FightPandemicsApp).appComponent

    DaggerProfileComponent
        .factory()
        .create(appComponent, editProfileFragment.coreComponent())
        .inject(editProfileFragment)
}

fun inject(editProfileNameFragment: EditProfileNameFragment) {
    val appComponent =
        (editProfileNameFragment.requireActivity().applicationContext as FightPandemicsApp).appComponent

    DaggerProfileComponent
        .factory()
        .create(appComponent, editProfileNameFragment.coreComponent())
        .inject(editProfileNameFragment)
}

fun Fragment.coreComponent() = requireActivity().coreComponent()

fun Activity.coreComponent() =
    (applicationContext as? CoreComponentProvider)?.provideCoreComponent()
        ?: throw IllegalStateException("CoreComponentProvider not implemented: $applicationContext")
