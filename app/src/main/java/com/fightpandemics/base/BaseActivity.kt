package com.fightpandemics.base

import androidx.appcompat.app.AppCompatActivity
import com.fightpandemics.App
import com.fightpandemics.di.component.AppComponent

abstract class BaseActivity : AppCompatActivity() {
    protected val applicationComponent: AppComponent
        get() = (application as App).appComponent
}
