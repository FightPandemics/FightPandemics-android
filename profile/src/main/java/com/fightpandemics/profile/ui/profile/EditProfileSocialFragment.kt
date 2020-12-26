package com.fightpandemics.profile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileRequest
import com.fightpandemics.core.data.model.profile.RequestUrls
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import com.google.android.material.textfield.TextInputEditText
import com.mobsandgeeks.saripaar.annotation.Length
import com.mobsandgeeks.saripaar.annotation.Max
import com.mobsandgeeks.saripaar.annotation.Optional
import kotlinx.android.synthetic.main.edit_profile_social_fragment.*
import kotlinx.android.synthetic.main.profile_toolbar.toolbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


class EditProfileSocialFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Length(max = 15, messageResId = R.string.error_size_social)
    lateinit var etFfacebook: TextInputEditText
    @Length(max = 15, messageResId = R.string.error_size_social)
    lateinit var etGithub: TextInputEditText
    @Length(max = 15, messageResId = R.string.error_size_social)
    lateinit var etLlinkedin: TextInputEditText
    @Length(max = 15, messageResId = R.string.error_size_social)
    lateinit var etTwitter: TextInputEditText
    @Length(max = 15, messageResId = R.string.error_size_social)
    lateinit var etWwebsite: TextInputEditText

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

        etFfacebook = facebook_url_edittext
        etGithub = github_url_edittext
        etLlinkedin = linkedin_url_edittext
        etTwitter = twitter_url_edittext
        etWwebsite = website_url_edittext

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bindSocialLinks(profileViewModel.currentProfile)

        // todo save button
        // navigate up, then indicate profile view model to patch
        social_links_save_button.setOnClickListener {
            validator.validate()
        }
        validationOk = {
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