package com.fightpandemics.login.dagger

import android.app.Activity
import androidx.fragment.app.Fragment
import com.fightpandemics.FightPandemicsApp
import com.fightpandemics.dagger.CoreComponentProvider
import com.fightpandemics.login.ui.LoginFragment

fun inject(fragment: Fragment) {
    val appComponent =
        (fragment.requireActivity().applicationContext as FightPandemicsApp).appComponent

    DaggerLoginComponent
        .factory()
        .create(appComponent, fragment.coreComponent())
        .inject(fragment as LoginFragment)

}

fun Fragment.coreComponent() = requireActivity().coreComponent()

fun Activity.coreComponent() =
    (applicationContext as? CoreComponentProvider)?.provideCoreComponent()
        ?: throw IllegalStateException("CoreComponentProvider not implemented: $applicationContext")
