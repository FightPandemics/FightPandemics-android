package com.fightpandemics.filter.dagger

import com.fightpandemics.filter.ui.FilterFragment

/*
* created by Osaigbovo Odiase
* */
fun inject(fragment: FilterFragment) {
    (fragment.requireActivity().applicationContext as FilterComponentProvider)
        .provideFilterComponent()
        .inject(fragment)
}
