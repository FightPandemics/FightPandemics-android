package com.fightpandemics.login.ui.profile

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.login.CompleteProfileRequest
import com.fightpandemics.core.data.model.login.Hide
import com.fightpandemics.core.data.model.login.Needs
import com.fightpandemics.core.data.model.login.Objectives
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.core.widgets.BaseLocationFragment
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.login.databinding.FragmentCompleteProfileBinding
import com.fightpandemics.login.ui.LoginViewModel
import com.fightpandemics.login.util.snack
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.complete_profile_location.etAddress
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
class CompleteProfileFragment : BaseLocationFragment() {

    // handles location edit text, text layout, and recycler view
    private var locationSearchComponent: LocationSearchComponent? = null

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }

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

        locationSearchComponent = LocationSearchComponent(
            loginViewModel,
            fragmentCompleteProfileBinding.tilAddress,
            lifecycle
        ).apply {
            setupLocationAutocomplete()
        }
        setupShareLocation()

        return fragmentCompleteProfileBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.getString(USER_PROFILE)
    }

    override fun updateLocation(location: Location) {
        loginViewModel.updateCurrentLocation(location)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentCompleteProfileBinding = null
        locationSearchComponent = null
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

        val firstNameErrorStrings = NameErrorStrings("First name is required.", "30 characters max.")
        val lastNameErrorStrings = NameErrorStrings("Last name is required.", "30 characters max.")
        validateName(
            firstName,
            fragmentCompleteProfileBinding.tilFirstName,
            firstNameErrorStrings
        )
        validateName(
            lastName,
            fragmentCompleteProfileBinding.tilLastName,
            lastNameErrorStrings
        )
        validateLocation()

        if (
            !(
                fragmentCompleteProfileBinding.tilFirstName.isErrorEnabled ||
                    fragmentCompleteProfileBinding.tilLastName.isErrorEnabled ||
                    fragmentCompleteProfileBinding.tilAddress.tilLocation.isErrorEnabled
                )
        ) {
            val location = loginViewModel.completeProfileLocation
            val completeProfileRequest =
                CompleteProfileRequest(firstName, hide, lastName, location, needs, objectives)
            Timber.i("my complete profile request is $completeProfileRequest")
            onCompleteProfile(completeProfileRequest)
        }
    }

    private fun validateLocation() {
        if (loginViewModel.locationSelected.value.isNullOrBlank()) {
            fragmentCompleteProfileBinding.root.tilLocation.error = "Please select a location"
            fragmentCompleteProfileBinding.root.tilLocation.isErrorEnabled = true
        } else {
            fragmentCompleteProfileBinding.root.tilLocation.isErrorEnabled = false
        }
    }

    private fun validateName(name: String, nameTextInputLayout: TextInputLayout, errorStrings: NameErrorStrings) {
        nameTextInputLayout.isErrorEnabled = true
        when {
            name.isBlank() ->
                nameTextInputLayout.error = errorStrings.emptyError
            name.length > MAX_NAME_LENGTH ->
                nameTextInputLayout.error = errorStrings.lengthError
            else -> {
                nameTextInputLayout.isErrorEnabled = false
            }
        }
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

    private fun setupShareLocation() {
        fragmentCompleteProfileBinding.root.share_my_location.setOnClickListener { view ->
            etAddress.setText("")
            getCurrentLocation()
            lifecycleScope.launchWhenStarted {
                loginViewModel.currentLocationState.collect { locationState ->
                    when {
                        locationState.isLoading -> bindLoading(locationState.isLoading)
                        locationState.userLocation!!.isNotEmpty() -> {
                            bindLoading(locationState.isLoading)
                            locationSearchComponent?.onSelectedLocation(locationState.userLocation)
                        }
                        locationState.error != null -> {
                            bindLoading(locationState.isLoading)
                        }
                    }
                }
            }
        }
    }

    private fun bindLoading(isLoading: Boolean) {
        fragmentCompleteProfileBinding.root.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val USER_PROFILE = "userProfile"
        const val MAX_NAME_LENGTH = 30
    }
}

data class NameErrorStrings(
    val emptyError: String,
    val lengthError: String
)
