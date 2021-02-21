package com.fightpandemics.profile.ui

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.core.widgets.BaseLocationFragment
import com.fightpandemics.profile.dagger.inject
import com.fightpandemics.profile.databinding.ProfileLocationFragmentBinding
import com.fightpandemics.profile.ui.profile.ProfileViewModel
import kotlinx.android.synthetic.main.profile_location_fragment.*
import kotlinx.android.synthetic.main.profile_location_fragment.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [ChangeLocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangeLocationFragment : BaseLocationFragment(), LocationAdapter.OnItemClickListener {

    private val adapter = LocationAdapter(this)
    private val LENGTH_TO_SHOW_SUGGESTIONS = 3
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var profileLocationFragmentBinding: ProfileLocationFragmentBinding? = null

    @ExperimentalCoroutinesApi
    private val profileViewModel: ProfileViewModel by activityViewModels { viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val binding = ProfileLocationFragmentBinding.inflate(inflater)
        profileLocationFragmentBinding = binding
        profileLocationFragmentBinding!!.appBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileLocationFragmentBinding!!.profileLocationRecyclerView.adapter = adapter
        adapter.placesNames = mutableListOf("sfncdf", "sdfef")
        searchLocation()
        shareLocation() // get user location and display it
    }

    override fun onDestroyView() {
        profileLocationFragmentBinding = null
        super.onDestroyView()
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        bindListeners()
        user_location?.setText(
            profileViewModel.individualProfile.value?.location,
            TextView.BufferType.EDITABLE
        )
    }

    private fun bindListeners() {

        appBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun shareLocation() {
        profileLocationFragmentBinding!!.shareLocationText.setOnClickListener {
            profileLocationFragmentBinding!!.userLocation.setText("")
            getCurrentLocation()
            lifecycleScope.launchWhenStarted {
                profileViewModel.currentLocationState.collect {
                    when {
                        it.isLoading -> bindLoading(it.isLoading)
                        it.userLocation!!.isNotEmpty() -> {
                            bindLoading(it.isLoading)
                            displayLocation(it.userLocation!!)
                        }
                        it.error != null -> {
                            bindLoading(it.isLoading)
                        }
                    }
                }
            }
        }
    }

    private fun bindLoading(isLoading: Boolean) {
        profileLocationFragmentBinding!!.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }
    private fun displayLocation(address: String) {
        profileLocationFragmentBinding!!.userLocation.setText(address)

        profileViewModel.locationQuery.value = address
        // todo maybe find a better way of doing this -
        //  Take away focus from edit text once an option has been selected binding.searchText.requestFocus()
        profileLocationFragmentBinding!!.userLocation.isEnabled = false
        profileLocationFragmentBinding!!.userLocation.isEnabled = true
        // hide recycler view autocomplete location suggestions
        profileLocationFragmentBinding!!.profileLocationRecyclerView.visibility =
            View.GONE
//        filterStartFragmentBinding!!.locationOptions.itemLineDivider1.visibility = View.GONE
    }
    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun searchLocation() {
        // do not start autocomplete until 3 chars in and delete location input that is not selected
        profileLocationFragmentBinding!!.userLocation.doAfterTextChanged { inputLocation ->

            lifecycleScope.launchWhenStarted {
                // do not search autocomplete suggestions until 3 chars in
                inputLocation?.let {
                    handleAutocompleteVisibility(it.toString())
                    Timber.e(it.toString())
                    profileViewModel.searchQuery.value = it.toString()
                    profileViewModel.searchLocationState.collect {
                        adapter.placesNames = it
                    }
                }
            }
            // if location in the editText is edited, delete location, lat, lgn live data
            profileViewModel.locationQuery.value = ""
        }
    }

    // On click function for recycler view (autocomplete)
    override fun onAutocompleteLocationClick(locationSelected: String) {
        displayLocation(locationSelected)
    }

    // this function gets called inside getCurrentLocation from BaseLocationFragment
    override fun updateLocation(location: Location) {
        Timber.i("My filters from filter $location")
        profileViewModel.updateCurrentLocation(location)
    }
    // constant for showing autocomplete suggestions
    private fun handleAutocompleteVisibility(locationQuery: String) {
        if (locationQuery.isBlank() || locationQuery.length < LENGTH_TO_SHOW_SUGGESTIONS) {
            profileLocationFragmentBinding!!.profileLocationRecyclerView.visibility =
                View.GONE
        } else {
            profileLocationFragmentBinding!!.profileLocationRecyclerView.visibility =
                View.VISIBLE
        }
    }
}
