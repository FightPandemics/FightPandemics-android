package com.fightpandemics.base

import androidx.fragment.app.Fragment
import com.fightpandemics.App
import com.fightpandemics.di.component.AppComponent

abstract class BaseFragment : Fragment() {
    protected val applicationComponent: AppComponent
        get() = (activity?.application as App).appComponent
}
