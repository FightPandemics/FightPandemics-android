package com.fightpandemics.ui.base

import androidx.appcompat.app.AppCompatActivity
import com.fightpandemics.ui.App
import com.fightpandemics.di.component.AppComponent

abstract class BaseActivity : AppCompatActivity() {
    protected val applicationComponent: AppComponent
        get() = (application as App).appComponent
}