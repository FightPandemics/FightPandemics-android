package com.fightpandemics.profile.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.ui.profile.ProfileViewModel
import com.fightpandemics.ui.splash.inject
import com.google.android.material.textfield.TextInputEditText
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import kotlinx.android.synthetic.main.email_fragment.*
import kotlinx.android.synthetic.main.profile_change_goal_fragment.appBar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class ChangeEmailFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @ExperimentalCoroutinesApi
    private val profileViewModel: ProfileViewModel by activityViewModels { viewModelFactory }
    @Email
    @NotEmpty(messageResId = R.string.error_empty_email)
    private lateinit var tvEmail: TextInputEditText

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.email_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvEmail = user_email
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        bindListeners()
//        profileViewModel.individualProfile.observe(viewLifecycleOwner) { it ->
//            ?.text = it.email?:""
//        }
        user_email?.setText(
            profileViewModel.individualProfile.value?.email,
            TextView.BufferType.EDITABLE
        )

        validationOk = {
            updateEmail()
            requireActivity().onBackPressed()
        }
    }

    private fun updateEmail() {

//    profileViewModel.updateAccount(
//        PatchIndividualAccountRequest(
//            firstName = firstName,
//            hide = profileViewModel.currentProfile.hide,
//            lastName = lastName,
//            location = profileViewModel.currentProfile.location,
//            needs = profileViewModel.currentProfile.needs,
//            objectives = profileViewModel.currentProfile.objectives
//        )
//    )
    }
    private fun bindListeners() {

        appBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        email_save_btn.setOnClickListener {
            validator.validate()
        }
    }
    companion object {
        fun newInstance() = ChangeEmailFragment()
    }
}
