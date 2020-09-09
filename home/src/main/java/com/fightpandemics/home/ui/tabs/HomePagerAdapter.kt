package com.fightpandemics.home.ui.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fightpandemics.home.ui.tabs.all.HomeAllFragment
import com.fightpandemics.home.ui.tabs.offers.HomeOfferFragment
import com.fightpandemics.home.ui.tabs.requests.HomeRequestFragment

class HomePagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle){

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> HomeAllFragment.newInstance()
        1 -> HomeOfferFragment.newInstance()
        2 -> HomeRequestFragment.newInstance()
        else -> throw IllegalArgumentException("position: $position cannot be greater than $itemCount")
    }

    override fun getItemCount(): Int = 3
}
