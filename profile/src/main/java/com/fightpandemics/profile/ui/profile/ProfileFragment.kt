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
import com.fightpandemics.profile.util.userInitials
import com.fightpandemics.ui.MainActivity
import com.fightpandemics.utils.webviewer.WebViewerActivity
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.profile_fragment_content.*
import kotlinx.android.synthetic.main.profile_toolbar.*
import kotlinx.android.synthetic.main.user_posts_content.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

class ProfileFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @ExperimentalCoroutinesApi
    private val profileViewModel: ProfileViewModel by activityViewModels() { viewModelFactory }

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

    @ExperimentalCoroutinesApi
    private fun bindListeners() {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    Timber.d("Settings")
                    findNavController().navigate(com.fightpandemics.R.id.action_profileFragment_to_indivProfileSettings)
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }

        (activity as MainActivity).findViewById<MaterialButton>(com.fightpandemics.R.id.fabCreateAsOrg)
            .setOnClickListener {
                findNavController().navigate(com.fightpandemics.R.id.action_profileFragment_to_createPostFragment)
            }

        (activity as MainActivity).findViewById<MaterialButton>(com.fightpandemics.R.id.fabCreateAsIndiv)
            .setOnClickListener {
                findNavController().navigate(com.fightpandemics.R.id.action_profileFragment_to_createPostFragment)
            }

        button3?.setOnClickListener {
            findNavController().navigate(com.fightpandemics.R.id.action_profileFragment_to_editProfileFragment)
        }
        profileViewModel.individualProfile.observe(viewLifecycleOwner) { profile ->
            getIndividualProfileListener(profile)
        }
    }

    @ExperimentalCoroutinesApi
    private fun getIndividualProfileListener(profile: ProfileViewModel.IndividualProfileViewState) {
        when {
            profile.isLoading -> {
                bindLoading(true)
            }
            profile.error == null -> {
                bindLoading(false)
                user_full_name.text =
                    profile.firstName?.capitalizeFirstLetter() + " " + profile.lastName?.capitalizeFirstLetter()
                user_location.text = profile.location
                bio.text = profile.bio
                loadUserImage(profile, profile.imgUrl)
                facebook.setOnClickListener { openWebView(profile.facebook) }
                linkedin.setOnClickListener { openWebView(profile.linkedin) }
                twitter.setOnClickListener { openWebView(profile.twitter) }
                github.setOnClickListener { openWebView(profile.github) }
                link.setOnClickListener { openWebView(profile.website) }
                displayUserPosts(profile.id!!)
            }
            profile.error.isNotBlank() -> {
                bindLoading(false)
                // TODO add error treatment on get current profile
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun displayUserPosts(id: String) {
        val adapter = PostsAdapter()
        val rv = profile_activities_recyclerView
        rv.adapter = adapter
        profileViewModel.loadUserPosts(id)
        profileViewModel.postsState.observe(
            viewLifecycleOwner,
            {
                // ...
                when {
                    it.posts!!.isNotEmpty() -> {
                        bindLoading(it.isLoading)
                        adapter.data = it.posts
//                        homeAllFragmentBinding!!.postList.visibility = View.VISIBLE
//                        postsAdapter.submitList(it.posts)
//                        postsAdapter.onItemClickListener = { post ->
//                            Timber.e("${post.author?.name}")
//                            // findNavController().navigate(PokeListFragmentDirections
//                            .actionPokeListFragmentToPokeDetailFragment(post))
//                        }
                    }
                    it.error != null -> {
                        bindLoading(it.isLoading)
//                        homeAllFragmentBinding!!.postList.visibility = View.GONE
                        Timber.i("Debug: error ${it.error.exception}")
                        // bindError()
                    }
                }
            }
        )
    }

    @ExperimentalCoroutinesApi
    private fun loadUserImage(
        profile: ProfileViewModel.IndividualProfileViewState,
        imgUrl: String?
    ) {
        if (profile.imgUrl == null || imgUrl.toString().isBlank()) {
            user_avatar.setInitials(userInitials(profile.firstName, profile.lastName))
            user_avatar.invalidate()
        } else {
            GlideApp
                .with(requireContext())
                .load(profile.imgUrl)
                .centerCrop()
                .into(user_avatar)
        }
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

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
