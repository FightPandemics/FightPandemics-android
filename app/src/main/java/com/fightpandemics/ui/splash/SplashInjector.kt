package com.fightpandemics.ui.splash

import android.app.Activity
import androidx.fragment.app.Fragment
import com.fightpandemics.FightPandemicsApp

// Creation of the splash graph using the application graph and inject this activity to that Component
fun inject(activity: Activity) = FightPandemicsApp
    .appComponent(activity)
    .splashComponent()
    .create()
    .inject(activity as SplashActivity)

fun inject(fragment: Fragment) = FightPandemicsApp
    .appComponent(fragment.requireActivity().applicationContext as FightPandemicsApp)
    .splashComponent()
    .create()
    .inject(fragment as SplashFragment)
