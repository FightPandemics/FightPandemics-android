package com.fightpandemics.ui.splash

import android.app.Activity
import com.fightpandemics.FightPandemicsApp

// Creation of the splash graph using the application graph and inject this activity to that Component
fun inject(activity: Activity) = FightPandemicsApp
    .appComponent(activity)
    .splashComponent()
    .create()
    .inject(activity as SplashActivity)