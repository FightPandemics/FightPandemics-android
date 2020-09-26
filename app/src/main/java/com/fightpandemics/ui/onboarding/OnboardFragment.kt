package com.fightpandemics.ui.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.fightpandemics.FightPandemicsApp
import com.fightpandemics.R
import com.fightpandemics.ui.MainActivity
import com.fightpandemics.ui.splash.SplashFragment
import com.fightpandemics.utils.ViewModelFactory
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_onboard.view.*
import timber.log.Timber
import javax.inject.Inject

class OnboardFragment : Fragment() {

    @Inject
    lateinit var commonHome: String

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        FightPandemicsApp
            .appComponent(requireActivity().applicationContext as FightPandemicsApp)
            .splashComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_onboard, container, false)

//        val fragmentList = arrayListOf<Fragment>(
//            /*FirstScreen(),
//            SecondScreen(),
//            ThirdScreen()*/
//        )
//
//        val adapter = OnBoardingAdapter(
//            fragmentList,
//            requireActivity().supportFragmentManager,
//            lifecycle
//        )
//
//        view.viewPager.adapter = adapter

        rootView.findViewById<TextView>(R.id.tv_skip).setOnClickListener {
            findNavController().navigate(R.id.action_onboardFragment_to_mainActivity)
                .apply { requireActivity().finish() }
        }
        rootView.findViewById<MaterialButton>(R.id.bt_join_now).setOnClickListener {
            findNavController().navigate(R.id.action_onboardFragment_to_signUpFragment)
        }

        return rootView
    }
}