package com.fightpandemics.profile.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import com.fightpandemics.utils.webviewer.WebViewerActivity
import kotlinx.android.synthetic.main.profile_settings.*

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
        bindListeners()
        if (profileViewModel.isUserSignedIn()) {
            hideSignedOutViews()
        } else {
            hideSignedInViews()
        }
    }

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.profile_settings, container, false)

    @SuppressLint("SetTextI18n")
    @ExperimentalCoroutinesApi
    private fun bindListeners() {
        appVersion.text = getString(R.string.fightpandemics_app_v) + com.fightpandemics.BuildConfig.VERSION_NAME
        toolbar.setOnClickListener { activity?.onBackPressed() }
        updatePublicProfileContainer.setOnClickListener {
            findNavController().navigate(com.fightpandemics.R.id.action_indivProfileSettings_to_editProfileFragment)
        }
        updateAccountInfoContainer.setOnClickListener {
            findNavController().navigate(com.fightpandemics.R.id.action_indivProfileSettings_to_editAccountFragment)
        }
        setupNotificationSettingsContainer.setOnClickListener { } //TO DO
        myAccountContainer.setOnClickListener {
            findNavController().navigate(com.fightpandemics.R.id.action_indivProfileSettings_to_nav_splash_onboard)
        }
        aboutUsContainer.setOnClickListener { openWebView(URLs.ABOUT_US) }
        privacyPolicyContainer.setOnClickListener { openWebView(URLs.PRIVACY_POLICY) }
        supportContainer.setOnClickListener { openWebView(URLs.SUPPORT) }
        feedbackContainer.setOnClickListener { } //TO DO
        signoutContainer.setOnClickListener {
            hideSignedInViews()
            myAccountContainer.visibility = View.VISIBLE
            profileViewModel.individualProfile.value = null
        }
    }

    private fun hideSignedInViews() {
        arrayOf(
            updatePublicProfileContainer,
            updateAccountInfoContainer,
            setupNotificationSettingsContainer,
            signoutContainer,
        ).forEach {
            it.visibility = View.GONE
        }
    }

    private fun hideSignedOutViews() {
        myAccountContainer.visibility = View.GONE
    }

    private fun openWebView(url: String?) {
        val intent = Intent(requireContext(), WebViewerActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }
}
