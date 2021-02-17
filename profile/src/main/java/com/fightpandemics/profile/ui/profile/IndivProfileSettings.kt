package com.fightpandemics.profile.ui.profile

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
import kotlinx.android.synthetic.main.settings_signed_in.aboutUsContainer as signInAboutUsContainer
import kotlinx.android.synthetic.main.settings_signed_in.privacyPolicyContainer as signInPrivacyPolicyContainer
import kotlinx.android.synthetic.main.settings_signed_in.supportContainer as signInSupportContainer
import kotlinx.android.synthetic.main.settings_signed_out.myAccountContainer
import kotlinx.android.synthetic.main.settings_signed_out.aboutUsContainer as signOutAboutUsContainer
import kotlinx.android.synthetic.main.settings_signed_out.privacyPolicyContainer as signOutPrivacyPolicyContainer
import kotlinx.android.synthetic.main.settings_signed_out.supportContainer as signOutSupportContainer

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
        profileViewModel.getIndividualProfile()
    }

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (isUserLoggedIn()) {
            inflater.inflate(R.layout.settings_signed_in, container, false)
        } else {
            inflater.inflate(R.layout.settings_signed_out, container, false)
        }
    }

    @ExperimentalCoroutinesApi
    private fun isUserLoggedIn(): Boolean {
        return profileViewModel.individualProfile.value?.id != null
    }

    private fun bindListeners() {
        myAccountContainer.setOnClickListener {
            findNavController().navigate(com.fightpandemics.R.id.action_indivProfileSettings_to_nav_splash_onboard)
        }

        signInAboutUsContainer.setOnClickListener { openWebView(URLs.ABOUT_US)}
        signOutAboutUsContainer.setOnClickListener { openWebView(URLs.ABOUT_US)}
        signInPrivacyPolicyContainer.setOnClickListener { openWebView(URLs.PRIVACY_POLICY)}
        signOutPrivacyPolicyContainer.setOnClickListener { openWebView(URLs.PRIVACY_POLICY)}
        signInSupportContainer.setOnClickListener { openWebView(URLs.SUPPORT)}
        signOutSupportContainer.setOnClickListener { openWebView(URLs.SUPPORT)}

//        toolbar.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.settings -> {
//                    Timber.d("bindListeners: Settings")
//                    findNavController()
//                    .navigate(R.id.action_profileFragment_to_indivProfileSettings)
//                    true
//                }
//
//                else -> {
//                    super.onOptionsItemSelected(it)
//                }
//            }
//        }

//        profileViewModel.individualProfile.observe(viewLifecycleOwner) { profile ->
//            getIndividualProfileListener(profile)
//        }

//        updateAccountInfoContainer.setOnClickListener {
//            findNavController().navigate(com.fightpandemics.R.id.action_indivProfileSettings_to_editAccountFragment)
//        }
//        toolbar.setNavigationOnClickListener {
//            findNavController().navigateUp()
//        }
    }

    private fun openWebView(url: String?) {
        val intent = Intent(requireContext(), WebViewerActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }

   companion object {
       fun newInstance() = IndivProfileSettings()
   }
}