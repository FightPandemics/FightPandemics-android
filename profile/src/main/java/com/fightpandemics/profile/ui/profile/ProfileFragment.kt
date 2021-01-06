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
        profileViewModel.individualProfile.observe(viewLifecycleOwner) { profile ->
            when {
                profile.isLoading -> {
                    bindLoading(true)
                }
                profile.error == null -> {
                    bindLoading(false)
                    //show data
                    user_full_name.text =
                        profile.firstName?.capitalizeFirstLetter() + " " + profile.lastName?.capitalizeFirstLetter()
                    user_location.text = profile.location
                    // glide user_avatar

                    if (profile.imgUrl == null || profile.imgUrl.isBlank()) {
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

                    facebook.setOnClickListener { openWebView(profile.facebook) }
                    linkedin.setOnClickListener { openWebView(profile.linkedin) }
                    twitter.setOnClickListener { openWebView(profile.twitter) }
                    github.setOnClickListener { openWebView(profile.github) }
                    link.setOnClickListener { openWebView(profile.website) }
                    bio.text = profile.bio

                }
                profile.error != null -> {
                    bindLoading(false)
                    // @feryel please fill this
                }

            }
        }

        profileViewModel.getIndividualProfile()
        overview.setOnClickListener {
            Timber.d("Overview")
        }

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

    }

    private fun openWebView(url: String?) {
        val intent = Intent(requireContext(), WebViewerActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }

    private fun bindLoading(isLoading: Boolean) {
//        Thread.sleep(3_000)
        if (isLoading) {
            content.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        } else {
            content.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

}
