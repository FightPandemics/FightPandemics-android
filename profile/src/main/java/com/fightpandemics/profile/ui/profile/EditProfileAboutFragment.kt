package com.fightpandemics.profile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.profile.PatchIndividualProfileRequest
import com.fightpandemics.core.data.model.profile.RequestUrls
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import com.fightpandemics.profile.ui.BaseFragment
import com.google.android.material.textfield.TextInputEditText
import com.mobsandgeeks.saripaar.annotation.Length
import kotlinx.android.synthetic.main.edit_profile_about_fragment.*
import kotlinx.android.synthetic.main.profile_toolbar.toolbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class EditProfileAboutFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Length(min = 0, max = 160, messageResId = R.string.error_size_bio)
    lateinit var tvAbout: TextInputEditText

    @ExperimentalCoroutinesApi
    private val profileViewModel: ProfileViewModel by activityViewModels { viewModelFactory }

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
        tvAbout = etAbout
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        initViews()
        validationOk = {
            val (urls, about) = getUpdateParams()
            profileViewModel.updateProfile(
                PatchIndividualProfileRequest(about, urls)
            )
            requireActivity().onBackPressed()
        }
    }

    @ExperimentalCoroutinesApi
    private fun initViews() {
        bindViews()
        bindListeners()
    }

    @ExperimentalCoroutinesApi
    private fun getUpdateParams(): Pair<RequestUrls, String> {
        val urls = RequestUrls(
            facebook = profileViewModel.currentProfile.urls.facebook ?: "",
            github = profileViewModel.currentProfile.urls.github ?: "",
            instagram = "",
            linkedin = profileViewModel.currentProfile.urls.linkedin ?: "",
            twitter = profileViewModel.currentProfile.urls.twitter ?: "",
            website = profileViewModel.currentProfile.urls.website ?: "",
        )
        val about = etAbout.text.toString()
        return Pair(urls, about)
    }

    @ExperimentalCoroutinesApi
    private fun bindViews() {
        etAbout.setText(profileViewModel.individualProfile.value?.bio, TextView.BufferType.EDITABLE)
    }

    private fun bindListeners() {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        aboutSaveButton.setOnClickListener {
            validator.validate()
        }
    }

    companion object {
        fun newInstance() = EditProfileAboutFragment()
    }
}
