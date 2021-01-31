package com.fightpandemics.profile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.util.capitalizeFirstLetter
import com.fightpandemics.ui.splash.inject
import kotlinx.android.synthetic.main.activity_logged_in.toolbar
import kotlinx.android.synthetic.main.email_item.*
import kotlinx.android.synthetic.main.location_item.*
import kotlinx.android.synthetic.main.name_item.*
import kotlinx.android.synthetic.main.profile_setting_my_account.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class EditAccountFragment : Fragment() {
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
        return inflater.inflate(R.layout.profile_setting_my_account, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        bindListeners()
        profileViewModel.individualProfile.observe(
            viewLifecycleOwner,
            { profile ->
                updateScreen(profile)
            }
        )
    }

    @ExperimentalCoroutinesApi
    private fun updateScreen(profile: ProfileViewModel.IndividualProfileViewState) {
        user_name?.text =
            profile.firstName?.capitalizeFirstLetter() + " " + profile.lastName?.capitalizeFirstLetter()
        user_location?.text =
            profile.location?.capitalizeFirstLetter()
        user_email?.text =
            profile.email
    }
    private fun bindListeners() {

        name_item.setOnClickListener {
            findNavController().navigate(com.fightpandemics.R.id.action_editAccountFragment_to_changeNameFragment)
        }
        location_item.setOnClickListener {
            findNavController().navigate(com.fightpandemics.R.id.action_editAccountFragment_to_changeLocationFragment)
        }
        donation_item.setOnClickListener {
            findNavController().navigate(com.fightpandemics.R.id.action_editAccountFragment_to_changeGoalFragment)
        }
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
    companion object {
        fun newInstance() = IndivProfileSettings()
    }
}
