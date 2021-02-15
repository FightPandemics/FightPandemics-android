package com.fightpandemics.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.R
import com.fightpandemics.core.result.EventObserver
import com.fightpandemics.core.utils.ViewModelFactory
import javax.inject.Inject


class SplashFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val splashViewModel by viewModels<SplashViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            launch()
        }, 2500)
    }


    private fun launch() {
        splashViewModel.launchDestination.observe(requireActivity(), EventObserver { destination ->
            when (destination) {
                LaunchDestination.MAIN_ACTIVITY ->
                    findNavController().navigate(R.id.action_splashFragment_to_onboardFragment)
                LaunchDestination.ONBOARD ->
                    findNavController().navigate(R.id.action_splashFragment_to_onboardFragment)
            }.checkAllMatched
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
