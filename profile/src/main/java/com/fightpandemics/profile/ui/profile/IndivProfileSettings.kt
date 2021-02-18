package com.fightpandemics.profile.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.BuildConfig
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import com.fightpandemics.profile.util.capitalizeFirstLetter
import com.fightpandemics.utils.webviewer.WebViewerActivity
import kotlinx.android.synthetic.main.profile_fragment_content.*
import kotlinx.android.synthetic.main.settings_signed_in.updatePublicProfileContainer
import kotlinx.android.synthetic.main.settings_signed_in.updateAccountInfoContainer
import kotlinx.android.synthetic.main.settings_signed_in.setupNotificationSettingsContainer
import kotlinx.android.synthetic.main.settings_signed_in.signoutContainer
import kotlinx.android.synthetic.main.settings_signed_in.toolbar as signInToolbar
import kotlinx.android.synthetic.main.settings_signed_in.aboutUsContainer as signInAboutUsContainer
import kotlinx.android.synthetic.main.settings_signed_in.privacyPolicyContainer as signInPrivacyPolicyContainer
import kotlinx.android.synthetic.main.settings_signed_in.supportContainer as signInSupportContainer
import kotlinx.android.synthetic.main.settings_signed_in.feedbackContainer as signInFeedbackContainer
import kotlinx.android.synthetic.main.settings_signed_out.toolbar as signOutToolbar
import kotlinx.android.synthetic.main.settings_signed_out.myAccountContainer
import kotlinx.android.synthetic.main.settings_signed_out.aboutUsContainer as signOutAboutUsContainer
import kotlinx.android.synthetic.main.settings_signed_out.privacyPolicyContainer as signOutPrivacyPolicyContainer
import kotlinx.android.synthetic.main.settings_signed_out.supportContainer as signOutSupportContainer
import kotlinx.android.synthetic.main.settings_signed_out.feedbackContainer as signOutFeedbackContainer

import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

object URLs {
    const val ABOUT_US = "https://fightpandemics.com/about-us"
    const val PRIVACY_POLICY = "https://fightpandemics.com/privacy-policy"
    const val SUPPORT = "https://fightpandemics.com/faq"
}

class IndivProfileSettings : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @ExperimentalCoroutinesApi
    private val profileViewModel: ProfileViewModel by activityViewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }
    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        if (isUserLoggedIn()) {
            bindSignInListeners()
        }
        else {
            bindSignOutListeners()
        }
    }

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = if (isUserLoggedIn()) {
        inflater.inflate(R.layout.settings_signed_in, container, false)
    } else {
        inflater.inflate(R.layout.settings_signed_out, container, false)
    }

    @ExperimentalCoroutinesApi
    private fun bindSignOutListeners() {
        // Signed out buttons
        signOutToolbar?.setOnClickListener { activity?.onBackPressed() }
        myAccountContainer.setOnClickListener {
            findNavController().navigate(com.fightpandemics.R.id.action_indivProfileSettings_to_nav_splash_onboard)
        }
        signOutAboutUsContainer.setOnClickListener { openWebView(URLs.ABOUT_US) }
        signOutPrivacyPolicyContainer.setOnClickListener { openWebView(URLs.PRIVACY_POLICY) }
        signOutSupportContainer.setOnClickListener { openWebView(URLs.SUPPORT) }
        // TODO
        // signOutFeedbackContainer.setOnClickListener {
        //     findNavController().navigate(com.fightpandemics.R.id.action_indivProfileSettings_to_feedbackFragment)
        // }
    }

    @ExperimentalCoroutinesApi
    private fun bindSignInListeners() {
        // Signed in buttons
        // signInToolbar?.setOnClickListener { activity?.onBackPressed() }
        updatePublicProfileContainer.setOnClickListener {
            findNavController().navigate(com.fightpandemics.R.id.action_indivProfileSettings_to_editProfileFragment)
        }
        updateAccountInfoContainer.setOnClickListener {
            findNavController().navigate(com.fightpandemics.R.id.action_indivProfileSettings_to_editAccountFragment)
        }
        // TODO
        // setupNotificationSettingsContainer.setOnClickListener {
        //     findNavController().navigate(com.fightpandemics.R.id.action_indivProfileSettings_to_setupNotificationSettingsFragment)
        // }
        signInAboutUsContainer.setOnClickListener { openWebView(URLs.ABOUT_US)}
        signInPrivacyPolicyContainer.setOnClickListener { openWebView(URLs.PRIVACY_POLICY)}
        signInSupportContainer.setOnClickListener { openWebView(URLs.SUPPORT)}
        // TODO
        // signInFeedbackContainer.setOnClickListener {
        //     findNavController().navigate(com.fightpandemics.R.id.action_indivProfileSettings_to_feedback)
        // }
        // TODO
        // signoutContainer.setOnClickListener {
        //     findNavController().navigate(com.fightpandemics.R.id.action_indivProfileSettings_to_signoutFragment)
        // }
    }

    private fun openWebView(url: String?) {
        val intent = Intent(requireContext(), WebViewerActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }

    @ExperimentalCoroutinesApi
    private fun isUserLoggedIn(): Boolean {
        return profileViewModel.individualProfile.value?.id != null
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}