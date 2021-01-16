package com.fightpandemics.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.fightpandemics.R
import com.fightpandemics.core.result.EventObserver
import com.fightpandemics.core.utils.ViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class OnBoardFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val onBoardViewModel by viewModels<OnBoardViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_onboard, container, false)

        val onboardViewPager = rootView.findViewById<ViewPager2>(R.id.onboardViewPager)
        val tabLayout = rootView.findViewById<TabLayout>(R.id.tabLayout)

        val onBoardingAdapters = OnBoardAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        onboardViewPager.adapter = onBoardingAdapters
        TabLayoutMediator(tabLayout, onboardViewPager) { tab, position -> }.attach()

        rootView.findViewById<MaterialButton>(R.id.bt_sign_in).setOnClickListener {
            onBoardViewModel.signInClick()
            onBoardViewModel.navigateToSignIn.observe(
                viewLifecycleOwner,
                EventObserver {
                    this.run {
                        findNavController().navigate(R.id.action_onboardFragment_to_signInFragment)
                    }
                }
            )
        }

        rootView.findViewById<MaterialButton>(R.id.bt_join_now).setOnClickListener {
            onBoardViewModel.signUpClick()
            onBoardViewModel.navigateToSignUp.observe(
                viewLifecycleOwner,
                EventObserver {
                    this.run {
                        findNavController().navigate(R.id.action_onboardFragment_to_signUpFragment)
                    }
                }
            )
        }

        rootView.findViewById<TextView>(R.id.tv_skip).setOnClickListener {
            onBoardViewModel.skipToHelpBoardClick()
            onBoardViewModel.navigateToMainActivity.observe(
                viewLifecycleOwner,
                EventObserver {
                    this.run {
                        findNavController().navigate(R.id.action_onboardFragment_to_mainActivity)
                            .apply { requireActivity().finish() }
                    }
                }
            )
        }
        return rootView
    }
}
