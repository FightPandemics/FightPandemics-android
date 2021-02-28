package com.fightpandemics.login.ui.profile

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.coroutineScope
import com.fightpandemics.login.databinding.CompleteProfileLocationBinding
import com.fightpandemics.login.ui.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect

// todo should i null terminate the binding?
@ExperimentalCoroutinesApi
@FlowPreview
class LocationSearchComponent(
    private val loginViewModel: LoginViewModel,
    private val locationAutocompleteBinding: CompleteProfileLocationBinding,
    private val adapter: LocationAdapter
) : LifecycleObserver {
    private lateinit var scope: LifecycleCoroutineScope

    // todo this might need to be in constructor to make sure scope is not null
    fun registerLifecycleOwner(lifecycle: Lifecycle) {
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

        locationAutocompleteBinding.etAddress.doAfterTextChanged {
            val inputLocation = it.toString()
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

//    fun setupShareLocation() {
//        locationAutocompleteBinding.shareMyLocation.setOnClickListener {
//            locationAutocompleteBinding.etAddress.setText("")
//            getCurrentLocation()
//            scope.launchWhenStarted {
// //            lifecycleScope.launchWhenStarted {
//                loginViewModel.currentLocationState.collect { locationState ->
//                    when {
//                        locationState.isLoading -> bindLoading(locationState.isLoading)
//                        locationState.userLocation!!.isNotEmpty() -> {
//                            bindLoading(locationState.isLoading)
//                            onSelectedLocation(locationState.userLocation)
//                        }
//                        locationState.error != null -> {
//                            bindLoading(locationState.isLoading)
//                        }
//                    }
//                }
//            }
//        }
//    }

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

    companion object {
        const val LENGTH_TO_SHOW_SUGGESTIONS = 3
    }
}
