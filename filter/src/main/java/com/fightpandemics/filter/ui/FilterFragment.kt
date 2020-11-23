package com.fightpandemics.filter.ui

import android.Manifest
import android.animation.LayoutTransition
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.filter.dagger.inject
import com.fightpandemics.home.R
import com.fightpandemics.home.databinding.FilterStartFragmentBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import timber.log.Timber
import javax.inject.Inject

/*
* created by Osaigbovo Odiase & Jose Li
* */
class FilterFragment : Fragment(), FilterAdapter.OnItemClickListener {

    // Places API variables
    private val LOCATION_PERMISSION_CODE = 1

    // constant for showing autocomplete suggestions
    private val LENGTH_TO_SHOW_SUGGESTIONS = 3

    @Inject
    lateinit var filterViewModelFactory: ViewModelFactory
    private val filterViewModel: FilterViewModel by viewModels { filterViewModelFactory }
    private var filterStartFragmentBinding: FilterStartFragmentBinding? = null
    private var locationManager: LocationManager? = null
    private lateinit var defaultTransition: LayoutTransition

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FilterStartFragmentBinding.inflate(inflater)
        filterStartFragmentBinding = binding
        filterStartFragmentBinding!!.filterToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Create a new Location Manager
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Get default transition
        defaultTransition = filterStartFragmentBinding!!.constraintLayoutOptions.layoutTransition

        // set up apply and clear filters buttons
        filterStartFragmentBinding!!.clearFiltersButton.setOnClickListener {
            clearFilters()
        }

        // send data to home module
        filterStartFragmentBinding!!.applyFiltersButton.setOnClickListener {
            // send info to home module
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                "filters",
                filterViewModel.createFilterRequest()
            )
            findNavController().popBackStack()
        }

        // setup recycler view
        val adapter = FilterAdapter(this)
        // set the custom adapter to the RecyclerView
        filterStartFragmentBinding!!.locationOptions.autoCompleteLocationsRecyclerView.adapter =
            adapter

        // update recycler view adapter list if data observed changes
        filterViewModel.autocomplete_locations.observe(viewLifecycleOwner, {
            adapter.placesNames = it["names"]!!
            adapter.placesIds = it["ids"]!!
        })

        // Set toggle functionality to clickable filter cards
        filterStartFragmentBinding!!.filterLocationExpandable.locationEmptyCard.setOnClickListener {
            filterViewModel.toggleView(filterViewModel.isLocationOptionsExpanded)
        }
        filterStartFragmentBinding!!.filterFromWhomExpandable.fromWhomEmptyCard.setOnClickListener {
            filterViewModel.toggleView(filterViewModel.isFromWhomOptionsExpanded)
        }
        filterStartFragmentBinding!!.filterTypeExpandable.typeEmptyCard.setOnClickListener {
            filterViewModel.toggleView(filterViewModel.isTypeOptionsExpanded)
        }

        // setonclick listeners for fromWhom and Type chips
        for (fromWhomChip in filterStartFragmentBinding!!.fromWhomOptions.fromWhomChipGroup.children) {
            fromWhomChip.setOnClickListener {
                updateFromWhomFiltersData()
            }
        }
        for (typeChip in filterStartFragmentBinding!!.typeOptions.typeChipGroup.children) {
            typeChip.setOnClickListener {
                updateTypeFiltersData()
            }
        }

        filterViewModel.isLocationOptionsExpanded.observe(
            viewLifecycleOwner,
            { isExpanded ->
                if (isExpanded) {
                    // close other two options cards, and stop transitions when closing to prevent glitchy look
                    filterStartFragmentBinding!!.constraintLayoutOptions.layoutTransition = null
                    filterViewModel.isFromWhomOptionsExpanded.value = false
                    filterViewModel.isTypeOptionsExpanded.value = false

                    // re-enable transitions and open card
                    filterStartFragmentBinding!!.constraintLayoutOptions.layoutTransition =
                        defaultTransition
                    expandContents(
                        filterStartFragmentBinding!!.locationOptions.root,
                        filterStartFragmentBinding!!.filterLocationExpandable.locationEmptyCard
                    )
                    filterStartFragmentBinding!!.filterLocationExpandable.filtersAppliedText.visibility =
                        View.GONE
                } else {
                    collapseContents(
                        filterStartFragmentBinding!!.locationOptions.root,
                        filterStartFragmentBinding!!.filterLocationExpandable.locationEmptyCard
                    )

                    // logic for hiding the applied text
                    filterViewModel.locationQuery.value?.let {
                        if (it.isNotBlank()) {
                            filterStartFragmentBinding!!.filterLocationExpandable.filtersAppliedText.visibility =
                                View.VISIBLE
                        }
                    }
                }
            })

        filterViewModel.isFromWhomOptionsExpanded.observe(
            viewLifecycleOwner,
            { isExpanded ->
                if (isExpanded) {
                    // close other two options cards, and stop transitions when closing to prevent glitchy look
                    filterStartFragmentBinding!!.constraintLayoutOptions.layoutTransition = null
                    filterViewModel.isLocationOptionsExpanded.value = false
                    filterViewModel.isTypeOptionsExpanded.value = false

                    // re-enable transitions and open card
                    filterStartFragmentBinding!!.constraintLayoutOptions.layoutTransition =
                        defaultTransition
                    expandContents(
                        filterStartFragmentBinding!!.fromWhomOptions.root,
                        filterStartFragmentBinding!!.filterFromWhomExpandable.fromWhomEmptyCard
                    )
                    filterStartFragmentBinding!!.filterFromWhomExpandable.filtersAppliedText.visibility =
                        View.GONE
                } else {
                    collapseContents(
                        filterStartFragmentBinding!!.fromWhomOptions.root,
                        filterStartFragmentBinding!!.filterFromWhomExpandable.fromWhomEmptyCard
                    )

                    // applied text visibility logic
                    if (filterViewModel.fromWhomCount.value!! > 0) {
                        filterStartFragmentBinding!!.filterFromWhomExpandable.filtersAppliedText.visibility =
                            View.VISIBLE
                        filterStartFragmentBinding!!.filterFromWhomExpandable.filtersAppliedText.text =
                            requireContext().getString(
                                R.string.card_applied_filters,
                                filterViewModel.fromWhomCount.value!!
                            )
                    }
                }
            })

        filterViewModel.isTypeOptionsExpanded.observe(viewLifecycleOwner, { isExpanded ->
            if (isExpanded) {
                // close other two options cards, and stop transitions when closing to prevent glitchy look
                filterStartFragmentBinding!!.constraintLayoutOptions.layoutTransition = null
                filterViewModel.isLocationOptionsExpanded.value = false
                filterViewModel.isFromWhomOptionsExpanded.value = false

                // re-enable transitions and open card
                filterStartFragmentBinding!!.constraintLayoutOptions.layoutTransition =
                    defaultTransition
                expandContents(
                    filterStartFragmentBinding!!.typeOptions.root,
                    filterStartFragmentBinding!!.filterTypeExpandable.typeEmptyCard
                )
                filterStartFragmentBinding!!.filterTypeExpandable.filtersAppliedText.visibility =
                    View.GONE
            } else {
                collapseContents(
                    filterStartFragmentBinding!!.typeOptions.root,
                    filterStartFragmentBinding!!.filterTypeExpandable.typeEmptyCard
                )

                // applied text visibility logic
                if (filterViewModel.typeCount.value!! > 0) {
                    filterStartFragmentBinding!!.filterTypeExpandable.filtersAppliedText.visibility =
                        View.VISIBLE
                    filterStartFragmentBinding!!.filterTypeExpandable.filtersAppliedText.text =
                        requireContext().getString(
                            R.string.card_applied_filters,
                            filterViewModel.typeCount.value!!
                        )
                }
            }
        })

        // The next three observers check if apply filter button should be enabled
        filterViewModel.fromWhomCount.observe(viewLifecycleOwner, { fromWhomCount ->
            handleApplyFilterEnableState()
            updateApplyFiltersText()
        })
        filterViewModel.typeCount.observe(viewLifecycleOwner, { typeCount ->
            handleApplyFilterEnableState()
            updateApplyFiltersText()
        })
        filterViewModel.locationQuery.observe(viewLifecycleOwner, { locationQuery ->
            handleApplyFilterEnableState()
            updateApplyFiltersText()
        })

        // handle selection of location in recycler view and from get current location
        filterViewModel.onSelectedLocation.observe(viewLifecycleOwner, { onSelectedLocation ->
            if (onSelectedLocation != null) {
                filterStartFragmentBinding!!.locationOptions.locationSearch.setText(
                    onSelectedLocation
                )
                filterViewModel.locationQuery.value = onSelectedLocation
                // todo maybe find a better way of doing this
                // Take away focus from edit text once an option has been selected
                filterStartFragmentBinding!!.locationOptions.locationSearch.isEnabled = false
                filterStartFragmentBinding!!.locationOptions.locationSearch.isEnabled = true
                // hide recycler view autocomplete location suggestions
                filterStartFragmentBinding!!.locationOptions.autoCompleteLocationsRecyclerView.visibility =
                    View.GONE
                filterStartFragmentBinding!!.locationOptions.itemLineDivider1.visibility = View.GONE
                // todo maybe make a function in the view model of this
                // reset onSelectedLocation event to null because we finished selecting
                filterViewModel.onSelectedLocation.value = null
            }
        })

        // do not start autocomplete until 3 chars in and delete location input that is not selected
        filterStartFragmentBinding!!.locationOptions.locationSearch.doAfterTextChanged { inputLocation ->
            // do not search autocomplete suggestions until 3 chars in
            inputLocation?.let {
                handleAutocompleteVisibility(it.toString())
                if (it.length >= LENGTH_TO_SHOW_SUGGESTIONS) {
                    // TODO: Do API autocomplete call here
                    filterViewModel.autocompleteLocation(it.toString())
                }
            }
            // if location in the editText is edited, delete location, lat, lgn live data
            filterViewModel.locationQuery.value = ""
            filterViewModel.latitude.value = null
            filterViewModel.longitude.value = null
        }

        filterStartFragmentBinding!!.locationOptions.shareMyLocation.setOnClickListener {
            getCurrentLocation()
        }
    }

    override fun onDestroyView() {
        filterStartFragmentBinding = null
        locationManager = null
        super.onDestroyView()
    }

    private fun getCurrentLocation() {
        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Create location listener for LocationManager
            val locationListener: LocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    Timber.i("My filters : locationManager ${location.latitude}, ${location.longitude}")
                    locationManager!!.removeUpdates(this)
                    // update live data
                    filterViewModel.updateCurrentLocation(location)
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                override fun onProviderEnabled(provider: String) {}

                override fun onProviderDisabled(provider: String) {
                    Timber.i("My filters : locationManager GPS OFF")
                    Toast.makeText(requireContext(), "GPS Disabled", Toast.LENGTH_SHORT).show()
                }
            }

            if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    1f,
                    locationListener
                )
            } else {
                Toast.makeText(requireContext(), "Please enable GPS", Toast.LENGTH_SHORT).show()
            }

        } else {
            // A local method to request required permissions;
            getLocationPermission()
        }
    }

    private fun getLocationPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            val dialogView = layoutInflater.inflate(R.layout.location_permission_dialog, null)
            AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("ALLOW") { dialog, which ->
                    requestPermissions(
                        arrayOf("android.permission.ACCESS_FINE_LOCATION"),
                        LOCATION_PERMISSION_CODE
                    )
                }
                .setNegativeButton("CANCEL") { dialog, id ->
                    dialog.dismiss()
                }.create().show()
        } else {
            requestPermissions(
                arrayOf("android.permission.ACCESS_FINE_LOCATION"),
                LOCATION_PERMISSION_CODE
            )
//            Toast.makeText(context, "Permissions were denied", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT)
                        .show()
                    getCurrentLocation()
                }
            }
        }
    }

    private fun expandContents(optionsView: View, clickableTextView: TextView) {
        optionsView.visibility = View.VISIBLE
        clickableTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_minus_sign,
            0
        )
    }

    private fun collapseContents(optionsView: View, clickableTextView: TextView) {
        optionsView.visibility = View.GONE
        clickableTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_plus_sign,
            0
        )
    }

    // update number in text of apply filters button
    private fun updateApplyFiltersText() {
        when (val total = filterViewModel.getFiltersAppliedCount()) {
            0 -> filterStartFragmentBinding!!.applyFiltersButton.text =
                requireContext().getString(R.string.button_apply_filter)
            1 -> filterStartFragmentBinding!!.applyFiltersButton.text =
                requireContext().getString(R.string.button_apply_filter_, total)
            else -> filterStartFragmentBinding!!.applyFiltersButton.text =
                requireContext().getString(R.string.button_apply_filters, total)
        }
    }

    // Returns a list with the text of all checked chips
    private fun getCheckedChipsText(chipGroup: ChipGroup): MutableList<String> {
        val textsList = mutableListOf<String>()
        for (id in chipGroup.checkedChipIds) {
            val chip = chipGroup.findViewById<Chip>(id)
            textsList.add(chip.text.toString())
        }
        return textsList
    }

    private fun updateFromWhomFiltersData() {
        // get the names of all selected chips
        val whomChips =
            getCheckedChipsText(filterStartFragmentBinding!!.fromWhomOptions.fromWhomChipGroup)
        // update live data
        filterViewModel.fromWhomFilters.value = whomChips
        filterViewModel.fromWhomCount.value = whomChips.size
    }

    private fun updateTypeFiltersData() {
        // get the names of all selected chips
        val typeChips = getCheckedChipsText(filterStartFragmentBinding!!.typeOptions.typeChipGroup)
        // update live data
        filterViewModel.typeFilters.value = typeChips
        filterViewModel.typeCount.value = typeChips.size
    }

    private fun handleApplyFilterEnableState() {
        // button should be enabled if there is text in the exittext or if there are any chips selected in fromWhom or type
        filterStartFragmentBinding!!.applyFiltersButton.isEnabled =
            filterViewModel.locationQuery.value!!.isNotBlank() || filterViewModel.fromWhomCount.value!! + filterViewModel.typeCount.value!! > 0
    }

    private fun handleAutocompleteVisibility(locationQuery: String) {
        if (locationQuery.isBlank() || locationQuery.length < LENGTH_TO_SHOW_SUGGESTIONS) {
            filterStartFragmentBinding!!.locationOptions.autoCompleteLocationsRecyclerView.visibility =
                View.GONE
            filterStartFragmentBinding!!.locationOptions.itemLineDivider1.visibility = View.GONE
        } else {
            filterStartFragmentBinding!!.locationOptions.autoCompleteLocationsRecyclerView.visibility =
                View.VISIBLE
            filterStartFragmentBinding!!.locationOptions.itemLineDivider1.visibility = View.VISIBLE
        }
    }

    private fun uncheckChipGroup(chipGroup: ChipGroup) {
        val checkedChipIdsList = chipGroup.checkedChipIds
        for (id in checkedChipIdsList) {
            chipGroup.findViewById<Chip>(id).isChecked = false
        }
    }

    private fun clearFilters() {
        // close all option cards
        filterViewModel.closeOptionCards()
        // clear fromwhom selections
        uncheckChipGroup(filterStartFragmentBinding!!.fromWhomOptions.fromWhomChipGroup)
        // clear type selections
        uncheckChipGroup(filterStartFragmentBinding!!.typeOptions.typeChipGroup)
        // clear location box
        filterStartFragmentBinding!!.locationOptions.locationSearch.text?.clear()
        // clear the live data
        filterViewModel.clearLiveDataFilters()
        // update text in apply filters button
        updateApplyFiltersText()
        // change visibility of any applied texts left
        filterStartFragmentBinding!!.filterLocationExpandable.filtersAppliedText.visibility =
            View.GONE
        filterStartFragmentBinding!!.filterFromWhomExpandable.filtersAppliedText.visibility =
            View.GONE
        filterStartFragmentBinding!!.filterTypeExpandable.filtersAppliedText.visibility = View.GONE
    }

    // on click function for recycler view (autocomplete)
    override fun onAutocompleteLocationClick(locationSelected: String, placeId: String) {
        // update onSelectedLocation, latitude and longitude live data in view model
        filterViewModel.onSelectedLocation.value = locationSelected
        // TODO: Get Lat and Lng here for autocomplete selection from API
        filterViewModel.getLatLng(placeId)
    }
}

