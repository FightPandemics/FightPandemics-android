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
import com.fightpandemics.core.data.model.login.*
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.login.ui.LoginViewModel
import com.fightpandemics.login.util.snack
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import timber.log.Timber
import javax.inject.Inject

class CompleteProfileFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    lateinit var completeProfileButton : ConstraintLayout
    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }
    private lateinit var complete_profile_toolbar: MaterialToolbar

    companion object{
        const val USER_PROFILE = "userProfile"
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
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_complete_profile, container, false)

        complete_profile_toolbar = rootView.findViewById(R.id.complete_profile_toolbar)
        complete_profile_toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

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
//            val notifyPrefs =
//                NotifyPrefs(
//                    Digest(true, true, true),
//                    Instant(true, true, true, true)
//                )
//            val url = Url()
            val hide = Hide(false)
            val location = Location("mock", "mock", listOf(0.0,0.0), "mock", "mock" )

            val completeProfileRequest = CompleteProfileRequest(firstName, hide, lastName, location, needs, objectives)
            onCompleteProfile(completeProfileRequest)
        }

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.getString(USER_PROFILE)

    }

    private fun onCompleteProfile(request : CompleteProfileRequest){
        loginViewModel.doCompleteProfile(request)
        loginViewModel.completeProfile.observe(viewLifecycleOwner, {
            when {
                it.isLoading -> {

                }
                it.error == null -> {

                    Timber.e("LOGGED IN ${it.email}")
                    if (it.token == null) {
                        val PACKAGE_NAME = "com.fightpandemics"
                        val intent = Intent().setClassName(
                            PACKAGE_NAME,
                            "$PACKAGE_NAME.ui.MainActivity"
                        )
                        startActivity(intent).apply { requireActivity().finish() }
                    }
                }
                else -> {
                    completeProfileButton.snack("it.error", Snackbar.LENGTH_LONG)
                    Timber.e("ERROR ${it.error}")
                    // TODO 8 - The user is informed that their credentials are invalid using a Snackbar.
                }
            }
        })
    }
}
