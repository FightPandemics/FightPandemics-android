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
import com.fightpandemics.core.data.model.profile.*
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import com.google.android.material.textfield.TextInputEditText
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import kotlinx.android.synthetic.main.edit_profile_name_fragment.*
import kotlinx.android.synthetic.main.profile_toolbar.toolbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


class EditProfileNameFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @ExperimentalCoroutinesApi
    private val profileViewModel: ProfileViewModel by activityViewModels { viewModelFactory }

    @NotEmpty(messageResId = R.string.error_empty_name)
    lateinit var tvFirstName: TextInputEditText
    @NotEmpty
    lateinit var tvLastName: TextInputEditText


    companion object {
        fun newInstance() = EditProfileNameFragment()
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
        return inflater.inflate(R.layout.edit_profile_name_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvFirstName = et_first_name
        tvLastName = et_last_name
    }

    override fun onStart() {
        super.onStart()


        tvFirstName?.setText(profileViewModel.individualProfile.value?.firstName, TextView.BufferType.EDITABLE)
        tvLastName?.setText(profileViewModel.individualProfile.value?.lastName, TextView.BufferType.EDITABLE)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        name_save_button.setOnClickListener {
            validator.validate()
        }

        validationOk = {
            val firstName = et_first_name.text.toString()
            val lastName = et_last_name.text.toString()
            profileViewModel.updateAccount(
                PatchIndividualAccountRequest(
                    firstName = firstName,
                    hide = profileViewModel.currentProfile.hide,
                    lastName = lastName,
                    location = profileViewModel.currentProfile.location,
                    needs = profileViewModel.currentProfile.needs,
                    objectives = profileViewModel.currentProfile.objectives
                )
            )
            requireActivity().onBackPressed()
        }
    }
}