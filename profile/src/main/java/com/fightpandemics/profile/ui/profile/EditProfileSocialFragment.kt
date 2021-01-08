package com.fightpandemics.profile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.profile.IndividualProfileResponse
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileRequest
import com.fightpandemics.core.data.model.profile.RequestUrls
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import com.google.android.material.textfield.TextInputEditText
import com.mobsandgeeks.saripaar.QuickRule
import com.mobsandgeeks.saripaar.annotation.*
import kotlinx.android.synthetic.main.edit_profile_social_fragment.*
import kotlinx.android.synthetic.main.profile_toolbar.toolbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject


class EditProfileSocialFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Pattern(regex = "[A-Za-z0-9.]*$", messageResId = R.string.error_facebook_valid_characters)
    lateinit var etFfacebook: TextInputEditText

    @Pattern(regex = "[A-Za-z0-9._-]*$", messageResId = R.string.error_instagram_valid_characters)
    lateinit var etInstagram: TextInputEditText

    @Length(max = 15, messageResId = R.string.error_size_social)
    @Pattern(regex = "[A-Za-z0-9_]*$", messageResId = R.string.error_github_twitter_valid_characters)
    lateinit var etGithub: TextInputEditText

    @Pattern(regex = "[A-Za-z0-9-]*$", messageResId = R.string.error_linkedin_valid_characters)
    lateinit var etLlinkedin: TextInputEditText

    @Pattern(regex = "[A-Za-z0-9_]*$", messageResId = R.string.error_github_twitter_valid_characters)
    lateinit var etTwitter: TextInputEditText

    @Url
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
       initViews(profileViewModel.currentProfile)

        validationOk = {
            val urls = getSocialUrls()
            val about = profileViewModel.currentProfile.about ?: ""
            profileViewModel.updateProfile(
                PatchIndividualProfileRequest(about, urls)
            )
            requireActivity().onBackPressed()
        }

    }

    private fun getSocialUrls() = RequestUrls(
        facebook = facebook_url_edittext?.text.toString(),
        github = github_url_edittext?.text.toString(),
        instagram = instagram_url_edittext?.text.toString(),
        linkedin = linkedin_url_edittext?.text.toString(),
        twitter = twitter_url_edittext?.text.toString(),
        website = website_url_edittext?.text.toString()
    )

    private fun initViews(profile: IndividualProfileResponse) {
        bindEditTexts()
        bindSocialLinks(profile)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        validator.put(etFfacebook, FacebookRule(1))
        social_links_save_button.setOnClickListener {
            validator.validate()
        }
    }

    private fun bindEditTexts() {
        etFfacebook = facebook_url_edittext
        etInstagram = instagram_url_edittext
        etGithub = github_url_edittext
        etLlinkedin = linkedin_url_edittext
        etTwitter = twitter_url_edittext
        etWwebsite = website_url_edittext
    }

    private fun bindSocialLinks(profile: IndividualProfileResponse) {
        facebook_url_edittext.setText(profile.urls.facebook)
        instagram_url_edittext.setText(profile.urls.instagram)
        linkedin_url_edittext.setText(profile.urls.linkedin)
        twitter_url_edittext.setText(profile.urls.twitter)
        github_url_edittext.setText(profile.urls.github)
        website_url_edittext.setText(profile.urls.website)
    }

    class FacebookRule  // Override this constructor ONLY if you want sequencing.
        (sequence: Int) : QuickRule<EditText>(sequence) {
        override fun isValid(editText: EditText): Boolean {
            val text = editText.text.toString()
            val length = text.length
            Timber.i("Debug: my text is $text")
            return length == 0 || length >= 5
        }

        override fun getMessage(context: Context): String {
            return "Lenght should be at least 5"
        }
    }

}