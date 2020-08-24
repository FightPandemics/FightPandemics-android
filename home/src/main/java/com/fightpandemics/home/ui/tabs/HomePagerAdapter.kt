package com.fightpandemics.home.ui.tabs

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fightpandemics.home.R
import com.fightpandemics.home.ui.tabs.all.HomeAllFragment
import com.fightpandemics.home.ui.tabs.offers.HomeOfferFragment
import com.fightpandemics.home.ui.tabs.requests.HomeRequestFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_all,
    R.string.tab_offers,
    R.string.tab_requests
)

class HomePagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> HomeAllFragment.newInstance()
        1 -> HomeOfferFragment.newInstance()
        2 -> HomeRequestFragment.newInstance()
        else -> throw IllegalArgumentException("position: $position cannot be greater than $count")
    }

    override fun getPageTitle(position: Int): CharSequence? =
        context.resources.getString(TAB_TITLES[position])

    override fun getCount(): Int = 3
}
