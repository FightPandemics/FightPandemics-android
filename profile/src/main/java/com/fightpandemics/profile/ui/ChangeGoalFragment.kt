package com.fightpandemics.profile.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.profile.Needs
import com.fightpandemics.core.data.model.profile.Objectives
import com.fightpandemics.core.data.model.profile.PatchIndividualAccountRequest
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.ui.profile.IndivProfileSettings
import com.fightpandemics.profile.ui.profile.ProfileViewModel
import com.fightpandemics.ui.splash.inject
import kotlinx.android.synthetic.main.donation_checkbox.*
import kotlinx.android.synthetic.main.information_checkbox.*
import kotlinx.android.synthetic.main.medical_help_checkbox.*
import kotlinx.android.synthetic.main.other_help_checkbox.*
import kotlinx.android.synthetic.main.profile_change_goal_fragment.*
import kotlinx.android.synthetic.main.volunteer_hrs_checkbok.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [ChangeGoalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangeGoalFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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
        return inflater.inflate(R.layout.profile_change_goal_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        bindListeners()
        profileViewModel.individualProfile.observe(
            viewLifecycleOwner
        ) { profile ->
            updateScreen(profile)
        }
    }

    @ExperimentalCoroutinesApi
    private fun bindListeners() {

        appBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        donation_save_btn.setOnClickListener {
            val(donate, shareInformation, volunteer) = getObjectives()
            val(medicalHelp, otherHelp) = getNeeds()
            profileViewModel.updateAccount(
                PatchIndividualAccountRequest(
                    firstName = profileViewModel.currentProfile.firstName,
                    hide = profileViewModel.currentProfile.hide,
                    lastName = profileViewModel.currentProfile.lastName,
                    location = profileViewModel.currentProfile.location,
                    needs = Needs(medicalHelp, otherHelp),
                    objectives = Objectives(donate, shareInformation, volunteer)
                )
            )
        }
    }
    private fun getObjectives(): Triple<Boolean, Boolean, Boolean> {
        val donate = donation_checkbox!!.isChecked
        val shareInformation = information_checkbox!!.isChecked
        val volunteer = volunteer_hrs_checkbox!!.isChecked

        return Triple(donate, shareInformation, volunteer)
    }
    private fun getNeeds(): Pair<Boolean, Boolean> {
        val medicalHelp = medical_help_checkbox!!.isChecked
        val otherHelp = other_help_checkbox!!.isChecked

        return Pair(medicalHelp, otherHelp)
    }
    @ExperimentalCoroutinesApi
    private fun updateScreen(profile: ProfileViewModel.IndividualProfileViewState) {
        donation_checkbox!!.isChecked = profile.objectives?.donate ?: false
        information_checkbox!!.isChecked = profile.objectives?.shareInformation ?: false
        volunteer_hrs_checkbox!!.isChecked = profile.objectives?.volunteer ?: false

        medical_help_checkbox!!.isChecked = profile.needs?.medicalHelp ?: false
        other_help_checkbox!!.isChecked = profile.needs?.otherHelp ?: false
    }

    companion object {
        fun newInstance() = IndivProfileSettings()
    }
}
