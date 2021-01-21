package com.fightpandemics.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fightpandemics.R

/**
 * A 'Trampoline' activity for sending users to an appropriate screen on launch.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
}
