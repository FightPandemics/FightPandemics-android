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
import com.fightpandemics.login.util.dismissKeyboard
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
    private val adapter = LocationAdapter(this)

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }
    private lateinit var completeProfileToolbar: MaterialToolbar

    private var _fragmentCompleteProfileBinding: FragmentCompleteProfileBinding? = null
    private val fragmentCompleteProfileBinding get() = _fragmentCompleteProfileBinding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _fragmentCompleteProfileBinding = FragmentCompleteProfileBinding.inflate(inflater)

        fragmentCompleteProfileBinding.completeProfileToolbar.root.setOnClickListener {
            findNavController().navigateUp()
        }

        fragmentCompleteProfileBinding.clBtnComplete.setOnClickListener {
            validateAndCompleteProfile()
        }

        setupLocationAutocomplete()
        setupShareLocation()

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

    private fun validateAndCompleteProfile() {
        val firstName: String = fragmentCompleteProfileBinding.etFirstName.text.toString()
        val lastName: String = fragmentCompleteProfileBinding.etLastName.text.toString()
        val donation: Boolean = fragmentCompleteProfileBinding.root.donation_checkbox.isChecked
        val information: Boolean =
            fragmentCompleteProfileBinding.root.information_checkbox.isChecked
        val volunteerHrsOffering: Boolean =
            fragmentCompleteProfileBinding.root.hours_offer_checkbox.isChecked
        val volunteerHrsRequest: Boolean =
            fragmentCompleteProfileBinding.root.hours_request_checkbox.isChecked
        val otherHelp: Boolean = fragmentCompleteProfileBinding.root.other_help_checkbox.isChecked
        val objectives = Objectives(donation, information, volunteerHrsOffering)
        val needs = Needs(volunteerHrsRequest, otherHelp)
        val hide = Hide(false)

        if (isFirstNameValid(firstName) and isLastNameValid(lastName) and isLocationValid()) {
            val location = loginViewModel.completeProfileLocation
            val completeProfileRequest =
                CompleteProfileRequest(firstName, hide, lastName, location, needs, objectives)
            Timber.i("my complete profile request is $completeProfileRequest")
            onCompleteProfile(completeProfileRequest)
        }
    }

    private fun isLocationValid(): Boolean {
        val isValid = !loginViewModel.locationSelected.value.isNullOrBlank()
        fragmentCompleteProfileBinding.root.tilLocation.isErrorEnabled = false
        if (!isValid) {
            fragmentCompleteProfileBinding.root.tilLocation.error = "Please select a location"
            fragmentCompleteProfileBinding.root.tilLocation.isErrorEnabled = true
        }
        return isValid
    }

    private fun isFirstNameValid(firstName: String): Boolean {
        var isValid = false
        fragmentCompleteProfileBinding.tilFirstName.isErrorEnabled = true
        when {
            firstName.isBlank() ->
                fragmentCompleteProfileBinding.tilFirstName.error = "First name is required."
            firstName.length > 30 ->
                fragmentCompleteProfileBinding.tilFirstName.error = "30 characters max."
            else -> {
                isValid = true
                fragmentCompleteProfileBinding.tilFirstName.isErrorEnabled = false
            }
        }
        return isValid
    }

    private fun isLastNameValid(lastName: String): Boolean {
        var isValid = false
        fragmentCompleteProfileBinding.tilLastName.isErrorEnabled = true
        when {
            lastName.isBlank() ->
                fragmentCompleteProfileBinding.tilLastName.error = "Last name is required."
            lastName.length > 30 ->
                fragmentCompleteProfileBinding.tilLastName.error = "30 characters max."
            else -> {
                isValid = true
                fragmentCompleteProfileBinding.tilLastName.isErrorEnabled = false
            }
        }
        return isValid
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
                        fragmentCompleteProfileBinding.clBtnComplete.snack(
                            "Error: ${it.error}",
                            Snackbar.LENGTH_LONG
                        )
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
                            onSelectedLocation(it.userLocation)
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

        // when focus is gone, handle suggestions visibility logic and textview deletion
        fragmentCompleteProfileBinding.root.etAddress.setOnFocusChangeListener { _, hasFocus ->
            handleAutocompleteVisibility(
                fragmentCompleteProfileBinding.root.etAddress.text.toString(),
                hasFocus
            )
            handleLocationSelectedText(hasFocus)
        }

        // when user starts typing, handle visibility, search for location predictions, and reset selected location
        fragmentCompleteProfileBinding.root.etAddress.doAfterTextChanged {
            it.toString().also { inputLocation ->
                handleAutocompleteVisibility(inputLocation)
                searchLocationPredictions(inputLocation)
            }
            // reset location selected since we are searching for a new location
            loginViewModel.resetLocation()
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

    private fun onSelectedLocation(selected_location: String) {
        // display selected location in editText
        fragmentCompleteProfileBinding.root.etAddress.setText(selected_location)
        // save location selected in liveData view model to prevent not-selected input from being accepted
        loginViewModel.locationSelected.value = selected_location
        removeFocusFromLocationEditText()
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

    private fun handleLocationSelectedText(hasFocus: Boolean) {
        if (!hasFocus && loginViewModel.locationSelected.value.isNullOrBlank()) {
            fragmentCompleteProfileBinding.root.etAddress.text?.clear()
        }
    }

    // This removes focus and hides keyboard
    private fun removeFocusFromLocationEditText() {
        fragmentCompleteProfileBinding.root.tilLocation.clearFocus()
        dismissKeyboard(fragmentCompleteProfileBinding.root.tilLocation)
    }

    private fun bindLoading(isLoading: Boolean) {
        fragmentCompleteProfileBinding.root.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onAutocompleteLocationClick(locationSelected: Prediction) {
        onSelectedLocation(locationSelected.description)
        loginViewModel.getDetails(locationSelected.place_id)
    }

    companion object {
        const val USER_PROFILE = "userProfile"
        const val LENGTH_TO_SHOW_SUGGESTIONS = 3
    }
}
