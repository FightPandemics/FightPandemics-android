package com.fightpandemics.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

abstract class BaseActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {
//    protected val applicationComponent: AppComponent
//        get() = (application as App).appComponent
}