package com.fightpandemics.profile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.profile.PatchIndividualAccountRequest
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileRequest
import com.fightpandemics.core.data.model.profile.RequestUrls
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import kotlinx.android.synthetic.main.edit_profile_about_fragment.*
import kotlinx.android.synthetic.main.edit_profile_name_fragment.*
import kotlinx.android.synthetic.main.edit_profile_name_fragment.et_first_name
import kotlinx.android.synthetic.main.edit_profile_social_fragment.*
import kotlinx.android.synthetic.main.profile_toolbar.*
import kotlinx.android.synthetic.main.profile_toolbar.toolbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


class EditProfileAboutFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @ExperimentalCoroutinesApi
    private val profileViewModel: ProfileViewModel by activityViewModels { viewModelFactory }


    companion object {
        fun newInstance() = EditProfileAboutFragment()
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
        return inflater.inflate(R.layout.edit_profile_about_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        etAbout.setText(profileViewModel.individualProfile.value?.bio, TextView.BufferType.EDITABLE)

        aboutSaveButton.setOnClickListener {

            val urls = RequestUrls(
                facebook = profileViewModel.currentProfile.urls.facebook ?: "",
                github = profileViewModel.currentProfile.urls.github ?: "",
                instagram = "",
                linkedin = profileViewModel.currentProfile.urls.linkedin ?: "",
                twitter = profileViewModel.currentProfile.urls.twitter ?: "",
                website = profileViewModel.currentProfile.urls.website ?: "",
            )

            val about = etAbout.text.toString()
            profileViewModel.updateProfile(
                PatchIndividualProfileRequest(about, urls)
            )
            requireActivity().onBackPressed()
        }
    }
}