package com.fightpandemics.login.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.login.CompleteProfileRequest
import com.fightpandemics.core.data.model.login.Hide
import com.fightpandemics.core.data.model.login.Needs
import com.fightpandemics.core.data.model.login.Objectives
import com.fightpandemics.core.data.model.userlocationpredictions.Prediction
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.core.widgets.BaseLocationFragment
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.login.databinding.FragmentCompleteProfileBinding
import com.fightpandemics.login.ui.LoginViewModel
import com.fightpandemics.login.util.snack
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.complete_profile_location.etAddress
import kotlinx.android.synthetic.main.complete_profile_location.view.auto_complete_locations_recycler_view
import kotlinx.android.synthetic.main.complete_profile_location.view.etAddress
import kotlinx.android.synthetic.main.complete_profile_location.view.item_line_divider1
import kotlinx.android.synthetic.main.complete_profile_location.view.progressBar
import kotlinx.android.synthetic.main.complete_profile_location.view.share_my_location
import kotlinx.android.synthetic.main.complete_profile_location.view.tilLocation
import kotlinx.android.synthetic.main.donation_checkbox.view.donation_checkbox
import kotlinx.android.synthetic.main.information_checkbox.view.information_checkbox
import kotlinx.android.synthetic.main.medical_help_checkbox.view.hours_request_checkbox
import kotlinx.android.synthetic.main.other_help_checkbox.view.other_help_checkbox
import kotlinx.android.synthetic.main.volunteer_hrs_checkbox.view.hours_offer_checkbox
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class CompleteProfileFragment : BaseLocationFragment(), LocationAdapter.OnItemClickListener {
    private val LENGTH_TO_SHOW_SUGGESTIONS = 3
    private val adapter = LocationAdapter(this)

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }
    private lateinit var completeProfileToolbar: MaterialToolbar

    private var _fragmentCompleteProfileBinding: FragmentCompleteProfileBinding? = null
    private val fragmentCompleteProfileBinding get() = _fragmentCompleteProfileBinding!!

    companion object {
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
    ): View {
        // Inflate the layout for this fragment
        _fragmentCompleteProfileBinding = FragmentCompleteProfileBinding.inflate(inflater)

        fragmentCompleteProfileBinding.completeProfileToolbar.root.setOnClickListener {
            findNavController().navigateUp()
        }

        fragmentCompleteProfileBinding.clBtnComplete.setOnClickListener {
            val firstName: String =
                fragmentCompleteProfileBinding.etFirstName.text.toString()
            val lastName: String =
                fragmentCompleteProfileBinding.etLastName.text.toString()
            val donation: Boolean =
                fragmentCompleteProfileBinding.root.donation_checkbox.isChecked
            val information: Boolean =
                fragmentCompleteProfileBinding.root.information_checkbox.isChecked
            val volunteerHrsOffering: Boolean =
                fragmentCompleteProfileBinding.root.hours_offer_checkbox.isChecked
            val volunteerHrsRequest: Boolean =
                fragmentCompleteProfileBinding.root.hours_request_checkbox.isChecked
            val otherHelp: Boolean =
                fragmentCompleteProfileBinding.root.other_help_checkbox.isChecked

            val objectives = Objectives(donation, information, volunteerHrsOffering)
            val needs = Needs(volunteerHrsRequest, otherHelp)
            val hide = Hide(false)
            // todo this need coordinates
//            val location = Location("mock", "mock", listOf(0.0, 0.0), "mock", "mock")
            val location = loginViewModel.completeProfileLocation

            val completeProfileRequest = CompleteProfileRequest(firstName, hide, lastName, location, needs, objectives)
            Timber.i("my complete profile request is $completeProfileRequest")
            // todo maybe add function getCoordinatesAndThenCompleteProfile
            onCompleteProfile(completeProfileRequest)
        }

        setupLocationAutocomplete()
        setupShareLocation() // get user location and display it

        return fragmentCompleteProfileBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.getString(USER_PROFILE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentCompleteProfileBinding = null
    }

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
                        fragmentCompleteProfileBinding.clBtnComplete.snack("Error: ${it.error}", Snackbar.LENGTH_LONG)
                        Timber.e("ERROR ${it.error}")
                    }
                }
            }
        )
    }

    override fun updateLocation(location: android.location.Location) {
        Timber.i("My filters from filter $location")
        // filterViewModel.updateCurrentLocation(location)
        loginViewModel.updateCurrentLocation(location)
    }

    private fun setupShareLocation() {
        fragmentCompleteProfileBinding.root.share_my_location.setOnClickListener {
            etAddress.setText("")
            getCurrentLocation()
            lifecycleScope.launchWhenStarted {
                loginViewModel.currentLocationState.collect {
                    when {
                        it.isLoading -> bindLoading(it.isLoading)
                        it.userLocation!!.isNotEmpty() -> {
                            bindLoading(it.isLoading)
                            displayLocation(it.userLocation)
                        }
                        it.error != null -> {
                            bindLoading(it.isLoading)
                        }
                    }
                }
            }
        }
    }

    private fun setupLocationAutocomplete() {
        // set the custom adapter to the RecyclerView
        fragmentCompleteProfileBinding.root.auto_complete_locations_recycler_view.adapter =
            adapter

        // when focus is gone, handle visibility logic
        fragmentCompleteProfileBinding.root.etAddress.setOnFocusChangeListener { _, hasFocus ->
            handleAutocompleteVisibility(fragmentCompleteProfileBinding.root.etAddress.text.toString(), hasFocus)
        }

        // when user starts typing, handle visibility and search for location predictions
        fragmentCompleteProfileBinding.root.etAddress.doAfterTextChanged {
            it.toString().also { inputLocation ->
                handleAutocompleteVisibility(inputLocation)
                searchLocationPredictions(inputLocation)
            }
        }
    }

    private fun searchLocationPredictions(inputLocation: String) {
        lifecycleScope.launchWhenStarted {
            loginViewModel.searchQuery.value = inputLocation
            loginViewModel.searchLocationState.collect {
                adapter.placesNames = it
            }
        }
    }

    private fun displayLocation(selected_location: String) {
        // set the selected location string in the textview
        fragmentCompleteProfileBinding.root.etAddress.setText(selected_location)
        //  Take away focus from edit text once an option has been selected 
        fragmentCompleteProfileBinding.root.tilLocation.isEnabled = false
        fragmentCompleteProfileBinding.root.tilLocation.isEnabled = true
    }

    private fun handleAutocompleteVisibility(locationQuery: String, hasFocus: Boolean = true) {
        if (locationQuery.isBlank() || locationQuery.length < LENGTH_TO_SHOW_SUGGESTIONS || !hasFocus) {
            fragmentCompleteProfileBinding.root.auto_complete_locations_recycler_view.visibility =
                View.GONE
            fragmentCompleteProfileBinding.root.item_line_divider1.visibility = View.GONE
        } else {
            fragmentCompleteProfileBinding.root.auto_complete_locations_recycler_view.visibility =
                View.VISIBLE
            fragmentCompleteProfileBinding.root.item_line_divider1.visibility = View.VISIBLE
        }
    }

    private fun bindLoading(isLoading: Boolean) {
        fragmentCompleteProfileBinding.root.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onAutocompleteLocationClick(locationSelected: Prediction) {
        displayLocation(locationSelected.description)
        loginViewModel.getDetails(locationSelected.place_id)
    }
}
