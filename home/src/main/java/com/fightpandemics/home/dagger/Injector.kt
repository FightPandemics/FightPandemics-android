package com.fightpandemics.home.dagger

import android.app.Activity
import androidx.fragment.app.Fragment
import com.fightpandemics.FightPandemicsApp
import com.fightpandemics.dagger.CoreComponentProvider
import com.fightpandemics.home.ui.HomeFragment
import com.fightpandemics.home.ui.tabs.all.HomeAllFragment

fun inject(homeFragment: HomeFragment) {
    val appComponent =
        (homeFragment.requireActivity().applicationContext as FightPandemicsApp).appComponent

    DaggerHomeComponent
        .factory()
        .create(appComponent, homeFragment.coreComponent())
        .inject(homeFragment)
}

fun inject(homeAllFragment: HomeAllFragment) {
    val appComponent =
        (homeAllFragment.requireActivity().applicationContext as FightPandemicsApp).appComponent

    DaggerHomeComponent
        .factory()
        .create(appComponent, homeAllFragment.coreComponent())
        .inject(homeAllFragment)
}

fun Fragment.coreComponent() = requireActivity().coreComponent()

fun Activity.coreComponent() =
    (applicationContext as? CoreComponentProvider)?.provideCoreComponent()
        ?: throw IllegalStateException("CoreComponentProvider not implemented: $applicationContext")
