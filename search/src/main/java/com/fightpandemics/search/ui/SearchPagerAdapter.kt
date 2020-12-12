package com.fightpandemics.search.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fightpandemics.search.ui.tabs.organizations.SearchOrganizationFragment
import com.fightpandemics.search.ui.tabs.people.SearchPeopleFragment
import com.fightpandemics.search.ui.tabs.posts.SearchPostsFragment

class SearchPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when(position){
        // todo remove parameters from here
        0 -> SearchPostsFragment.newInstance("dummy1", "dummy2")
        1 -> SearchOrganizationFragment.newInstance("dummy1", "dummy2")
        2 -> SearchPeopleFragment.newInstance("dummy1", "dummy2")
        else -> throw IllegalArgumentException("position: $position cannot be greater than $itemCount")
    }
}