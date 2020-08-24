package com.fightpandemics.search.dagger

import android.app.Activity
import androidx.fragment.app.Fragment
import com.fightpandemics.FightPandemicsApp
import com.fightpandemics.dagger.CoreComponentProvider
import com.fightpandemics.search.ui.SearchFragment

fun inject(fragment: Fragment) {
    val appComponent =
        (fragment.requireActivity().applicationContext as FightPandemicsApp).appComponent

    DaggerSearchComponent
        .factory()
        .create(appComponent, fragment.coreComponent())
        .inject(fragment as SearchFragment)
}

fun Fragment.coreComponent() = requireActivity().coreComponent()

fun Activity.coreComponent() =
    (applicationContext as? CoreComponentProvider)?.provideCoreComponent()
        ?: throw IllegalStateException("CoreComponentProvider not implemented: $applicationContext")
