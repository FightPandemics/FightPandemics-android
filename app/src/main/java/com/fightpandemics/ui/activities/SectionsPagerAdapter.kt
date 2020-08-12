package com.fightpandemics.ui.activities

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fightpandemics.R
import com.fightpandemics.ui.home.all.AllFragment
import com.fightpandemics.ui.home.offers.OffersFragment
import com.fightpandemics.ui.home.requests.RequestsFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_all,
    R.string.tab_offers,
    R.string.tab_requests
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> AllFragment.newInstance()
        1 -> OffersFragment.newInstance()
        2 -> RequestsFragment.newInstance()

        else -> throw IllegalArgumentException("position: $position cannot be greater than $count")
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}