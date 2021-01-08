package com.fightpandemics.profile.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.GlideApp
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import com.fightpandemics.profile.util.capitalizeFirstLetter
import com.fightpandemics.utils.webviewer.WebViewerActivity
import kotlinx.android.synthetic.main.profile_fragment_content.*
import kotlinx.android.synthetic.main.profile_toolbar.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject


class ProfileFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @ExperimentalCoroutinesApi
    private val profileViewModel: ProfileViewModel by activityViewModels() { viewModelFactory }


    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindLoading(true)
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        bindListeners()
        profileViewModel.getIndividualProfile()
    }

    private fun bindListeners() {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    Timber.d("Settings")
                    true
                }

                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }

        button3?.setOnClickListener {
            findNavController().navigate(com.fightpandemics.R.id.action_profileFragment_to_editProfileFragment)
        }
        profileViewModel.individualProfile.observe(viewLifecycleOwner) { profile ->
            getIndividualProfileListener(profile)
        }
    }

    private fun getIndividualProfileListener(profile: ProfileViewModel.IndividualProfileViewState) {
        when {
            profile.isLoading -> {
                bindLoading(true)
            }
            profile.error == null -> {
                bindLoading(false)
                initTextViews(profile)
                loadUserImage(profile, profile.imgUrl)
                initSocialListeners(profile)
            }
            profile.error != null -> {
                bindLoading(false)
                // @feryel please fill this
            }
        }
    }

    private fun loadUserImage(
        profile: ProfileViewModel.IndividualProfileViewState,
        imgUrl: String?
    ) {
        if (profile.imgUrl == null || imgUrl.toString().isBlank()) {
            user_avatar.setInitials(
                profile?.firstName?.substring(0, 1)
                    ?.toUpperCase() + profile?.lastName?.split(" ")?.last()
                    ?.substring(0, 1)?.toUpperCase()
            )
            user_avatar.invalidate()
        } else {
            GlideApp
                .with(requireContext())
                .load(profile.imgUrl)
                .centerCrop()
                .into(user_avatar)
        }
    }

    private fun initTextViews(profile: ProfileViewModel.IndividualProfileViewState) {
        user_full_name.text =
            profile.firstName?.capitalizeFirstLetter() + " " + profile.lastName?.capitalizeFirstLetter()
        user_location.text = profile.location
        bio.text = profile.bio
    }

    private fun initSocialListeners(profile: ProfileViewModel.IndividualProfileViewState) {
        facebook.setOnClickListener { openWebView(profile.facebook) }
        linkedin.setOnClickListener { openWebView(profile.linkedin) }
        twitter.setOnClickListener { openWebView(profile.twitter) }
        github.setOnClickListener { openWebView(profile.github) }
        link.setOnClickListener { openWebView(profile.website) }
    }

    private fun openWebView(url: String?) {
        val intent = Intent(requireContext(), WebViewerActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }

    private fun bindLoading(isLoading: Boolean) {
        if (isLoading) {
            content.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        } else {
            content.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

}
