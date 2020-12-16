package com.fightpandemics.profile.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.GlideApp
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_fragment_content.*
import kotlinx.android.synthetic.main.profile_toolbar.*
import kotlinx.android.synthetic.main.profile_toolbar.toolbar
import timber.log.Timber
import javax.inject.Inject


class ProfileFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val profileViewModel: ProfileViewModel by viewModels { viewModelFactory }


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


        profileViewModel.individualProfile.observe(viewLifecycleOwner, { profile ->
            when {
                profile.isLoading -> {

                }
                profile.error == null -> {
                    //show data
                    user_full_name.text = profile.fullName
                    user_location.text = profile.location
                    // glide user_avatar


                    GlideApp
                        .with(requireContext())
                        .load(profile.imgUrl)
                        .centerCrop()
                        .into(user_avatar)

                    facebook.setOnClickListener { openWebView(profile.facebook) }
                    linkedin.setOnClickListener { openWebView(profile.linkedin) }
                    twitter.setOnClickListener { openWebView(profile.twitter) }
                    github.setOnClickListener { openWebView(profile.github) }
                    link.setOnClickListener { openWebView(profile.linkedin) }
                    bio.text = profile.bio

                }
                profile.error != null -> {
                    // @feryel please fill this
                }

            }
        })

        profileViewModel.getndividualProfile()
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
        url?.let {
            webview.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    webview.visibility = View.VISIBLE
                    view.loadUrl(url)
                    return false
                }
            }
            var wbc: WebChromeClient = object : WebChromeClient() {
                override fun onCloseWindow(w: WebView?) {
                    super.onCloseWindow(w)
                    webview.visibility = View.GONE
                }
            }
            webview.webChromeClient = wbc;

            webview.getSettings().setJavaScriptEnabled(true)
            webview.loadUrl("http://www.google.com.br")
        }

    }

}
