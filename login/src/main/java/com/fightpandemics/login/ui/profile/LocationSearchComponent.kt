package com.fightpandemics.login.ui.profile

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.coroutineScope
import com.fightpandemics.core.data.model.userlocationpredictions.Prediction
import com.fightpandemics.login.databinding.CompleteProfileLocationBinding
import com.fightpandemics.login.ui.LoginViewModel
import com.fightpandemics.login.util.dismissKeyboard
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect

// todo should i null terminate the binding?
@ExperimentalCoroutinesApi
@FlowPreview
class LocationSearchComponent(
    private val loginViewModel: LoginViewModel,
    private val locationAutocompleteBinding: CompleteProfileLocationBinding,
    lifecycle: Lifecycle
) : LifecycleObserver, LocationAdapter.OnItemClickListener {
    private lateinit var scope: LifecycleCoroutineScope
    private val adapter = LocationAdapter(this)

    init {
        registerLifecycleOwner(lifecycle)
    }

    private fun registerLifecycleOwner(lifecycle: Lifecycle) {
        // todo should i need to subscribe to the observer?
        lifecycle.addObserver(this)
        scope = lifecycle.coroutineScope
    }

    fun setupLocationAutocomplete() {
        locationAutocompleteBinding.autoCompleteLocationsRecyclerView.adapter =
            adapter

        locationAutocompleteBinding.etAddress.setOnFocusChangeListener { _, hasFocus ->
            val locationQuery = locationAutocompleteBinding.etAddress.text.toString()
            if (shouldHideLocationSuggestions(locationQuery, hasFocus)) {
                hideLocationSuggestions()
                if (shouldClearLocationText()) {
                    locationAutocompleteBinding.etAddress.text?.clear()
                }
            } else {
                showLocationSuggestions()
            }
        }

        locationAutocompleteBinding.etAddress.doAfterTextChanged { textChanged ->
            val inputLocation = textChanged.toString()
            val hasFocus = locationAutocompleteBinding.etAddress.hasFocus()
            if (shouldHideLocationSuggestions(inputLocation, hasFocus)) {
                hideLocationSuggestions()
            } else {
                showLocationSuggestions()
                searchLocationPredictions(inputLocation)
                loginViewModel.resetOldLocationSelected()
            }
        }
    }

    private fun searchLocationPredictions(inputLocation: String) {
        scope.launchWhenStarted {
            loginViewModel.searchQuery.value = inputLocation
            loginViewModel.searchLocationState.collect { predictions ->
                adapter.placesNames = predictions
            }
        }
    }

    private fun hideLocationSuggestions() {
        locationAutocompleteBinding.autoCompleteLocationsRecyclerView.visibility = View.GONE
        locationAutocompleteBinding.itemLineDivider1.visibility = View.GONE
    }

    private fun showLocationSuggestions() {
        locationAutocompleteBinding.autoCompleteLocationsRecyclerView.visibility = View.VISIBLE
        locationAutocompleteBinding.itemLineDivider1.visibility = View.VISIBLE
    }

    private fun shouldHideLocationSuggestions(locationQuery: String, hasFocus: Boolean) =
        locationQuery.isBlank() || locationQuery.length < LENGTH_TO_SHOW_SUGGESTIONS || !hasFocus

    private fun shouldClearLocationText() =
        loginViewModel.locationSelected.value.isNullOrBlank()

    fun onSelectedLocation(selectedLocation: String) {
        // display selected location in editText
        locationAutocompleteBinding.etAddress.setText(selectedLocation)
        // save location selected in liveData view model to prevent not-selected input from being accepted
        loginViewModel.locationSelected.value = selectedLocation
        removeFocusAndKeyboardFromLocationEditText()
    }

    private fun removeFocusAndKeyboardFromLocationEditText() {
        locationAutocompleteBinding.tilLocation.clearFocus()
        dismissKeyboard(locationAutocompleteBinding.tilLocation)
    }

    override fun onAutocompleteLocationClick(locationSelected: Prediction) {
        onSelectedLocation(locationSelected.description)
        loginViewModel.getLocationDetails(locationSelected.placeId)
    }

    companion object {
        const val LENGTH_TO_SHOW_SUGGESTIONS = 3
    }
}
