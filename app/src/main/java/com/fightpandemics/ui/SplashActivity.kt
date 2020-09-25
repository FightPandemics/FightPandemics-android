package com.fightpandemics.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import com.fightpandemics.R
import com.fightpandemics.result.EventObserver
import com.fightpandemics.utils.ViewModelFactory
import timber.log.Timber
import javax.inject.Inject

/**
 * A 'Trampoline' activity for sending users to an appropriate screen on launch.
 */
class SplashActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var commonHome: String

    private val splashViewModel by viewModels<SplashViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject(this)
        super.onCreate(savedInstanceState)

        Timber.e(commonHome.toString())

        launch()
    }

    private fun goToOnBoardingActivity() {
        startActivity(Intent(this, OnBoardingActivity::class.java))
        finish()
    }

    private fun launch(){
        splashViewModel.launchDestination.observe(this, EventObserver { destination ->
            when (destination) {
                LaunchDestination.MAIN_ACTIVITY -> startActivity(Intent(this, MainActivity::class.java))
                LaunchDestination.ONBOARDING -> startActivity(Intent(this, OnBoardingActivity::class.java))
            }/*.checkAllMatched*/
            finish()
        })
    }
}

/**
 * Helper to force a when statement to assert all options are matched in a when statement.
 *
 * By default, Kotlin doesn't care if all branches are handled in a when statement. However, if you
 * use the when statement as an expression (with a value) it will force all cases to be handled.
 *
 * This helper is to make a lightweight way to say you meant to match all of them.
 *
 * Usage:
 *
 * ```
 * when(sealedObject) {
 *     is OneType -> //
 *     is AnotherType -> //
 * }.checkAllMatched
 */
val <T> T.checkAllMatched: T
    get() = this