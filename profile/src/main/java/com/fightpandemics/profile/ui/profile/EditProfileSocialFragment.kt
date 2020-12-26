package com.fightpandemics.profile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileRequest
import com.fightpandemics.core.data.model.profile.RequestUrls
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import kotlinx.android.synthetic.main.edit_profile_social_fragment.*
import kotlinx.android.synthetic.main.profile_toolbar.toolbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


class EditProfileSocialFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @ExperimentalCoroutinesApi
    private val profileViewModel: ProfileViewModel by activityViewModels { viewModelFactory }

    companion object {
        fun newInstance() = EditProfileSocialFragment()
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
        return inflater.inflate(R.layout.edit_profile_social_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bindSocialLinks(profileViewModel.currentProfile)

        // todo save button
        // navigate up, then indicate profile view model to patch
        social_links_save_button.setOnClickListener {
            val urls = RequestUrls(
                facebook = facebook_url_edittext.text.toString(),
                github = github_url_edittext.text.toString(),
                instagram = "this is missing in android",
                linkedin = linkedin_url_edittext.text.toString(),
                twitter = twitter_url_edittext.text.toString(),
                website = website_url_edittext.text.toString()
            )
            val about = profileViewModel.currentProfile.about ?: ""
            profileViewModel.updateProfile(
                PatchIndividualProfileRequest(about, urls)
            )
            requireActivity().onBackPressed()
        }

    }

    private fun bindSocialLinks(profile: IndividualProfileResponse) {
        facebook_url_edittext.setText(profile.urls.facebook)
        linkedin_url_edittext.setText(profile.urls.linkedin)
        twitter_url_edittext.setText(profile.urls.twitter)
        github_url_edittext.setText(profile.urls.github)
        website_url_edittext.setText(profile.urls.website)
    }

}