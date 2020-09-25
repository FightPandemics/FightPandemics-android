package com.fightpandemics.login.ui

import android.os.Bundle
import android.util.Log
import com.fightpandemics.dagger.CoreComponentProvider
import com.fightpandemics.login.dagger.LoginComponent
import com.fightpandemics.login.dagger.LoginComponentProvider
import com.fightpandemics.ui.BaseActivity
import com.fightpandemics.utils.ViewModelFactory
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class LoginActivity : BaseActivity() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    @Inject
    @Named("Core")
    lateinit var stringCore: String
    @Inject
    @Named("Login")
    lateinit var stringLogin: String

    // Reference to the Login graph
    lateinit var loginComponent: LoginComponent

    override fun onResume() {
        super.onResume()
        replaceFragment(SignUpFragment.newInstance(), false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        loginComponent = (applicationContext as LoginComponentProvider)
            .provideLoginComponent()

        loginComponent.inject(this)

        super.onCreate(savedInstanceState)

        Timber.e(stringCore)
        Timber.e(stringLogin)
    }
}