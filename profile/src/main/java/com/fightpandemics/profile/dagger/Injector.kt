package com.fightpandemics.profile.dagger

import android.app.Activity
import androidx.fragment.app.Fragment
import com.fightpandemics.FightPandemicsApp
import com.fightpandemics.dagger.CoreComponentProvider
import com.fightpandemics.profile.ui.ProfileFragment

fun inject(fragment: Fragment) {
    val appComponent =
        (fragment.requireActivity().applicationContext as FightPandemicsApp).appComponent

    DaggerProfileComponent
        .factory()
        .create(appComponent, fragment.coreComponent())
        .inject(fragment as ProfileFragment)
}

fun Fragment.coreComponent() = requireActivity().coreComponent()

fun Activity.coreComponent() =
    (applicationContext as? CoreComponentProvider)?.provideCoreComponent()
        ?: throw IllegalStateException("CoreComponentProvider not implemented: $applicationContext")
