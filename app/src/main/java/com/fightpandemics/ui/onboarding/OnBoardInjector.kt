package com.fightpandemics.ui.onboarding

import com.fightpandemics.FightPandemicsApp

fun inject(fragment: OnBoardFragment) = FightPandemicsApp
    .appComponent(fragment.requireActivity().applicationContext as FightPandemicsApp)
    .splashComponent()
    .create()
    .inject(fragment)
