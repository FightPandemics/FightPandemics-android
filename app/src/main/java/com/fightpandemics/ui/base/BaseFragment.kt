package com.fightpandemics.ui.base

import androidx.fragment.app.Fragment
import com.fightpandemics.ui.App
import com.fightpandemics.di.component.AppComponent

abstract class BaseFragment : Fragment() {
    protected val applicationComponent: AppComponent
        get() = (activity?.application as App).appComponent
}