package com.fightpandemics.profile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.profile.PatchIndividualAccountRequest
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import com.fightpandemics.profile.ui.BaseFragment
import com.google.android.material.textfield.TextInputEditText
import com.mobsandgeeks.saripaar.annotation.Length
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

    @Length(min = 0, max = 30, messageResId = R.string.error_name_length)
    @NotEmpty(messageResId = R.string.error_empty_first_name)
    lateinit var tvFirstName: TextInputEditText

    @Length(min = 0, max = 30, messageResId = R.string.error_name_length)
    @NotEmpty(messageResId = R.string.error_empty_last_name)
    lateinit var tvLastName: TextInputEditText

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

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        bindTextViews()
        bindListeners()
        validationOk = {
            updateAccount()
            requireActivity().onBackPressed()
        }
    }

    @ExperimentalCoroutinesApi
    private fun updateAccount() {
        val (firstName, lastName) = getNamesValue()
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
    }

    private fun getNamesValue(): Pair<String, String> {
        val firstName = et_first_name.text.toString()
        val lastName = et_last_name.text.toString()
        return Pair(firstName, lastName)
    }

    @ExperimentalCoroutinesApi
    private fun bindTextViews() {
        tvFirstName.setText(
            profileViewModel.individualProfile.value?.firstName,
            TextView.BufferType.EDITABLE
        )
        tvLastName.setText(
            profileViewModel.individualProfile.value?.lastName,
            TextView.BufferType.EDITABLE
        )
    }

    private fun bindListeners() {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        name_save_button.setOnClickListener {
            validator.validate()
        }
    }

    companion object {
        fun newInstance() = EditProfileNameFragment()
    }
}
