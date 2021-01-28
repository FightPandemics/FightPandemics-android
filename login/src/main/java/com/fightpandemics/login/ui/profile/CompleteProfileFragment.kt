package com.fightpandemics.login.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.login.CompleteProfileRequest
import com.fightpandemics.core.data.model.login.Hide
import com.fightpandemics.core.data.model.login.Location
import com.fightpandemics.core.data.model.login.Needs
import com.fightpandemics.core.data.model.login.Objectives
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.login.ui.LoginViewModel
import com.fightpandemics.login.util.snack
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

class CompleteProfileFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    private lateinit var completeProfileButton: ConstraintLayout
    @ExperimentalCoroutinesApi
    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }
    private lateinit var completeProfileToolbar: MaterialToolbar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_complete_profile, container, false)

        completeProfileToolbar = rootView.findViewById(R.id.complete_profile_toolbar)
        completeProfileToolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        completeProfileButton = rootView.findViewById(R.id.clBtnComplete)
        completeProfileButton.setOnClickListener {

            val firstName: String =
                rootView.findViewById<TextInputEditText>(R.id.etFirstName).text.toString()
            val lastName: String =
                rootView.findViewById<TextInputEditText>(R.id.etLastName).text.toString()
//            val address: String = rootView.findViewById(R.id.etLastName)
            // objectives offering
            val donation: Boolean =
                rootView.findViewById<MaterialCheckBox>(R.id.donation_checkbox).isChecked
            val information: Boolean =
                rootView.findViewById<MaterialCheckBox>(R.id.information_checkbox).isChecked
            val volunteerHrsOffering: Boolean =
                rootView.findViewById<MaterialCheckBox>(R.id.hours_offer_checkbox).isChecked
            // requesting
            val volunteerHrsRequest: Boolean =
                rootView.findViewById<MaterialCheckBox>(R.id.hours_request_checkbox).isChecked
            val otherHelp: Boolean =
                rootView.findViewById<MaterialCheckBox>(R.id.other_help_checkbox).isChecked

            val objectives = Objectives(donation, information, volunteerHrsOffering)
            val needs = Needs(volunteerHrsRequest, otherHelp)
            val hide = Hide(false)
            val location = Location("mock", "mock", listOf(0.0, 0.0), "mock", "mock")

            val completeProfileRequest = CompleteProfileRequest(firstName, hide, lastName, location, needs, objectives)
            onCompleteProfile(completeProfileRequest)
        }

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.getString(USER_PROFILE)
    }

    @ExperimentalCoroutinesApi
    private fun onCompleteProfile(request: CompleteProfileRequest) {
        loginViewModel.doCompleteProfile(request)
        loginViewModel.completeProfile.observe(
            viewLifecycleOwner,
            {
                when (it.error) {
                    null -> {

                        Timber.e("LOGGED IN ${it.email}")
                        if (it.token == null) {
                            val packageName = "com.fightpandemics"
                            val intent = Intent().setClassName(
                                packageName,
                                "$packageName.ui.MainActivity"
                            )
                            startActivity(intent).apply { requireActivity().finish() }
                        }
                    }
                    else -> {
                        completeProfileButton.snack("it.error", Snackbar.LENGTH_LONG)
                        Timber.e("ERROR ${it.error}")
                    }
                }
            }
        )
    }

    companion object {
        const val USER_PROFILE = "userProfile"
    }
}
