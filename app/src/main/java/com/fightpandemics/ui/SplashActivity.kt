package com.fightpandemics.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import com.fightpandemics.R
import com.fightpandemics.utils.ViewModelFactory
import timber.log.Timber
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var commonHome: String

    private val splashViewModel by viewModels<SplashViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject(this)
        super.onCreate(savedInstanceState)

        Timber.e(commonHome.toString())

        goToOnBoardingActivity()
    }

    private fun goToOnBoardingActivity() {
        startActivity(Intent(this, OnBoardingActivity::class.java))
        finish()
    }

    private fun launch(){
        /*
        viewModel.launchDestination.observe(this, EventObserver { destination ->
            when (destination) {
                MAIN_ACTIVITY -> startActivity(Intent(this, MainActivity::class.java))
                ONBOARDING -> startActivity(Intent(this, OnboardingActivity::class.java))
            }.checkAllMatched
            finish()
        })*/
    }
}