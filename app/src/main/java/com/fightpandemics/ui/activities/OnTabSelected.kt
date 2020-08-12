package com.fightpandemics.ui.activities

import android.widget.TextView
import com.fightpandemics.utils.applyStyle
import com.google.android.material.tabs.TabLayout

class OnTabSelected : TabLayout.OnTabSelectedListener {

    override fun onTabReselected(tab: TabLayout.Tab) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
        getTextView(tab)?.applyStyle(false)
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        getTextView(tab)?.applyStyle(true)
    }

    private fun getTextView(tab: TabLayout.Tab): TextView? = tab.customView as? TextView

}