package com.fightpandemics.login.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.login.CompleteProfileRequest
import com.fightpandemics.core.data.model.login.Hide
import com.fightpandemics.core.data.model.login.Location
import com.fightpandemics.core.data.model.login.Needs
import com.fightpandemics.core.data.model.login.Objectives
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.core.widgets.BaseLocationFragment
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.login.ui.LoginViewModel
import com.fightpandemics.login.util.snack
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.complete_profile_location.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

class CompleteProfileFragment : BaseLocationFragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    lateinit var completeProfileButton: ConstraintLayout
    @ExperimentalCoroutinesApi
    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }
    private lateinit var completeProfileToolbar: MaterialToolbar

    companion object {
        const val USER_PROFILE = "userProfile"
    }

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
            val donation: Boolean =
                rootView.findViewById<MaterialCheckBox>(R.id.donation_checkbox).isChecked
            val information: Boolean =
                rootView.findViewById<MaterialCheckBox>(R.id.information_checkbox).isChecked
            val volunteerHrsOffering: Boolean =
                rootView.findViewById<MaterialCheckBox>(R.id.hours_offer_checkbox).isChecked
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

        shareLocation(rootView) // get user location and display it

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.getString(USER_PROFILE)
    }

    override fun updateLocation(location: android.location.Location) {
        Timber.i("My filters from filter $location")
        // filterViewModel.updateCurrentLocation(location)
        loginViewModel.updateCurrentLocation(location)
    }

    private fun shareLocation(root: View?) {
        val shareMyLocation = root!!.findViewById<MaterialTextView>(R.id.share_my_location)
        shareMyLocation.setOnClickListener {
            etAddress.setText("")
            getCurrentLocation()
            lifecycleScope.launchWhenStarted {
                loginViewModel.currentLocationState.collect {
                    when {
                        it.isLoading -> bindLoading(root, it.isLoading)
                        it.userLocation!!.isNotEmpty() -> {
                            bindLoading(root, it.isLoading)
                            displayLocation(root, it.userLocation)
                        }
                        it.error != null -> {
                            bindLoading(root, it.isLoading)
                        }
                    }
                }
            }
        }
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
}

private fun displayLocation(root: View?, address: String) {

    root!!.findViewById<EditText>(R.id.etAddress).setText(address)

//    filterViewModel.locationQuery.value = address
//    // todo maybe find a better way of doing this -
//    //  Take away focus from edit text once an option has been selected binding.searchText.requestFocus()
//    filterStartFragmentBinding!!.locationOptions.locationSearch.isEnabled = false
//    filterStartFragmentBinding!!.locationOptions.locationSearch.isEnabled = true
//    // hide recycler view autocomplete location suggestions
//    filterStartFragmentBinding!!.locationOptions.autoCompleteLocationsRecyclerView.visibility =
//        View.GONE
//    filterStartFragmentBinding!!.locationOptions.itemLineDivider1.visibility = View.GONE
}

private fun bindLoading(root: View?, isLoading: Boolean) {
    root!!.findViewById<ProgressBar>(R.id.progressBar).visibility =
        if (isLoading) View.VISIBLE else View.GONE
}
