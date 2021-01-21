package com.fightpandemics.home.dagger

import android.app.Activity
import androidx.fragment.app.Fragment
import com.fightpandemics.FightPandemicsApp
import com.fightpandemics.core.dagger.CoreComponentProvider
import com.fightpandemics.home.ui.DeleteDialogFragment
import com.fightpandemics.home.ui.HomeFragment
import com.fightpandemics.home.ui.HomeOptionsBottomSheetFragment
import com.fightpandemics.home.ui.tabs.all.HomeAllFragment
import com.fightpandemics.home.ui.tabs.offers.HomeOfferFragment
import com.fightpandemics.home.ui.tabs.requests.HomeRequestFragment

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

fun inject(homeOfferFragment: HomeOfferFragment) {
    val appComponent =
        (homeOfferFragment.requireActivity().applicationContext as FightPandemicsApp).appComponent

    DaggerHomeComponent
        .factory()
        .create(appComponent, homeOfferFragment.coreComponent())
        .inject(homeOfferFragment)
}

fun inject(homeRequestFragment: HomeRequestFragment) {
    val appComponent =
        (homeRequestFragment.requireActivity().applicationContext as FightPandemicsApp).appComponent

    DaggerHomeComponent
        .factory()
        .create(appComponent, homeRequestFragment.coreComponent())
        .inject(homeRequestFragment)
}

fun inject(homeOptionsBottomSheetFragment: HomeOptionsBottomSheetFragment) {
    val appComponent =
        (homeOptionsBottomSheetFragment.requireActivity().applicationContext as FightPandemicsApp)
            .appComponent

    DaggerHomeComponent
        .factory()
        .create(appComponent, homeOptionsBottomSheetFragment.coreComponent())
        .inject(homeOptionsBottomSheetFragment)
}

fun inject(deleteDialogFragment: DeleteDialogFragment) {
    val appComponent =
        (deleteDialogFragment.requireActivity().applicationContext as FightPandemicsApp)
            .appComponent

    DaggerHomeComponent
        .factory()
        .create(appComponent, deleteDialogFragment.coreComponent())
        .inject(deleteDialogFragment)
}

fun Fragment.coreComponent() = requireActivity().coreComponent()

fun Activity.coreComponent() =
    (applicationContext as? CoreComponentProvider)?.provideCoreComponent()
        ?: throw IllegalStateException("CoreComponentProvider not implemented: $applicationContext")
