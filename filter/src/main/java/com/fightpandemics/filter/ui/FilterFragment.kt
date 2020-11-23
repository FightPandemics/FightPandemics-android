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
import androidx.lifecycle.ViewModelProvider
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

    @Inject
    lateinit var filterViewModelFactory: ViewModelFactory
    private lateinit var filterViewModel: FilterViewModel
    private lateinit var binding: FilterStartFragmentBinding
    private lateinit var locationManager: LocationManager
    private lateinit var defaultTransition: LayoutTransition

    // Places API variables
    private val LOCATION_PERMISSION_CODE = 1

    // constant for showing autocomplete suggestions
    private val LENGTH_TO_SHOW_SUGGESTIONS = 3

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FilterStartFragmentBinding.inflate(inflater)

        binding.filterToolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the viewmodel
        filterViewModel = ViewModelProvider(this).get(FilterViewModel::class.java)
        // Create a new Location Manager
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Get default transition
        defaultTransition = binding.constraintLayoutOptions.layoutTransition

        // set up apply and clear filters buttons
        binding.clearFiltersButton.setOnClickListener{
            clearFilters()
        }

        // send data to home module
        binding.applyFiltersButton.setOnClickListener {
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
        binding.locationOptions.autoCompleteLocationsRecyclerView.adapter = adapter

        // update recycler view adapter list if data observed changes
        filterViewModel.autocomplete_locations.observe(viewLifecycleOwner, {
            adapter.placesNames = it["names"]!!
            adapter.placesIds = it["ids"]!!
        })

        // Set toggle functionality to clickable filter cards
        binding.filterLocationExpandable.locationEmptyCard.setOnClickListener {
            filterViewModel.toggleView(filterViewModel.isLocationOptionsExpanded)
        }
        binding.filterFromWhomExpandable.fromWhomEmptyCard.setOnClickListener {
            filterViewModel.toggleView(filterViewModel.isFromWhomOptionsExpanded)
        }
        binding.filterTypeExpandable.typeEmptyCard.setOnClickListener {
            filterViewModel.toggleView(filterViewModel.isTypeOptionsExpanded)
        }

        // setonclick listeners for fromWhom and Type chips
        for (fromWhomChip in binding.fromWhomOptions.fromWhomChipGroup.children){
            fromWhomChip.setOnClickListener {
                updateFromWhomFiltersData()
            }
        }
        for (typeChip in binding.typeOptions.typeChipGroup.children){
            typeChip.setOnClickListener {
                updateTypeFiltersData()
            }
        }

        filterViewModel.isLocationOptionsExpanded.observe(
            viewLifecycleOwner,
            { isExpanded ->
                if (isExpanded) {
                    // close other two options cards, and stop transitions when closing to prevent glitchy look
                    binding.constraintLayoutOptions.layoutTransition = null
                    filterViewModel.isFromWhomOptionsExpanded.value = false
                    filterViewModel.isTypeOptionsExpanded.value = false

                    // re-enable transitions and open card
                    binding.constraintLayoutOptions.layoutTransition = defaultTransition
                    expandContents(
                        binding.locationOptions.root,
                        binding.filterLocationExpandable.locationEmptyCard
                    )
                    binding.filterLocationExpandable.filtersAppliedText.visibility = View.GONE
                } else {
                    collapseContents(
                        binding.locationOptions.root,
                        binding.filterLocationExpandable.locationEmptyCard
                    )

                    // logic for hiding the applied text
                    filterViewModel.locationQuery.value?.let {
                        if (it.isNotBlank()){
                            binding.filterLocationExpandable.filtersAppliedText.visibility =
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
                    binding.constraintLayoutOptions.layoutTransition = null
                    filterViewModel.isLocationOptionsExpanded.value = false
                    filterViewModel.isTypeOptionsExpanded.value = false

                    // re-enable transitions and open card
                    binding.constraintLayoutOptions.layoutTransition = defaultTransition
                    expandContents(
                        binding.fromWhomOptions.root,
                        binding.filterFromWhomExpandable.fromWhomEmptyCard
                    )
                    binding.filterFromWhomExpandable.filtersAppliedText.visibility = View.GONE
                } else {
                    collapseContents(
                        binding.fromWhomOptions.root,
                        binding.filterFromWhomExpandable.fromWhomEmptyCard
                    )

                    // applied text visibility logic
                    if (filterViewModel.fromWhomCount.value!! > 0) {
                        binding.filterFromWhomExpandable.filtersAppliedText.visibility = View.VISIBLE
                        binding.filterFromWhomExpandable.filtersAppliedText.text =
                            requireContext().getString( R.string.card_applied_filters, filterViewModel.fromWhomCount.value!!)
                    }
                }
            })

        filterViewModel.isTypeOptionsExpanded.observe(viewLifecycleOwner, { isExpanded ->
            if (isExpanded) {
                // close other two options cards, and stop transitions when closing to prevent glitchy look
                binding.constraintLayoutOptions.layoutTransition = null
                filterViewModel.isLocationOptionsExpanded.value = false
                filterViewModel.isFromWhomOptionsExpanded.value = false

                // re-enable transitions and open card
                binding.constraintLayoutOptions.layoutTransition = defaultTransition
                expandContents(binding.typeOptions.root, binding.filterTypeExpandable.typeEmptyCard)
                binding.filterTypeExpandable.filtersAppliedText.visibility = View.GONE
            } else {
                collapseContents(
                    binding.typeOptions.root,
                    binding.filterTypeExpandable.typeEmptyCard
                )

                // applied text visibility logic
                if (filterViewModel.typeCount.value!! > 0) {
                    binding.filterTypeExpandable.filtersAppliedText.visibility = View.VISIBLE
                    binding.filterTypeExpandable.filtersAppliedText.text =
                        requireContext().getString(R.string.card_applied_filters, filterViewModel.typeCount.value!!)
                }
            }
        })

        // The next three observers check if apply filter button should be enabled
        filterViewModel.fromWhomCount.observe(viewLifecycleOwner, {fromWhomCount ->
            handleApplyFilterEnableState()
            updateApplyFiltersText()
        })
        filterViewModel.typeCount.observe(viewLifecycleOwner, {typeCount ->
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
                binding.locationOptions.locationSearch.setText(onSelectedLocation)
                filterViewModel.locationQuery.value = onSelectedLocation
                // todo maybe find a better way of doing this
                // Take away focus from edit text once an option has been selected
                binding.locationOptions.locationSearch.isEnabled = false
                binding.locationOptions.locationSearch.isEnabled = true
                // hide recycler view autocomplete location suggestions
                binding.locationOptions.autoCompleteLocationsRecyclerView.visibility = View.GONE
                binding.locationOptions.itemLineDivider1.visibility = View.GONE
                // todo maybe make a function in the view model of this
                // reset onSelectedLocation event to null because we finished selecting
                filterViewModel.onSelectedLocation.value = null
            }
        })

        // do not start autocomplete until 3 chars in and delete location input that is not selected
        binding.locationOptions.locationSearch.doAfterTextChanged { inputLocation ->
            // do not search autocomplete suggestions until 3 chars in
            inputLocation?.let {
                handleAutocompleteVisibility(it.toString())
                if (it.length >= LENGTH_TO_SHOW_SUGGESTIONS){
                    // TODO: Do API autocomplete call here
                    filterViewModel.autocompleteLocation(it.toString())
                }
            }
            // if location in the editText is edited, delete location, lat, lgn live data
            filterViewModel.locationQuery.value = ""
            filterViewModel.latitude.value = null
            filterViewModel.longitude.value = null
        }

        binding.locationOptions.shareMyLocation.setOnClickListener {
            getCurrentLocation()
        }

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
                    locationManager.removeUpdates(this)
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

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, locationListener)
            }else{
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
        }
        else {
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
    private fun updateApplyFiltersText(){
        when(val total = filterViewModel.getFiltersAppliedCount()){
            0 -> binding.applyFiltersButton.text = requireContext().getString(R.string.button_apply_filter)
            1 -> binding.applyFiltersButton.text = requireContext().getString(R.string.button_apply_filter_, total)
            else -> binding.applyFiltersButton.text = requireContext().getString(R.string.button_apply_filters, total)
        }
    }

    // Returns a list with the text of all checked chips
    private fun getCheckedChipsText(chipGroup: ChipGroup): MutableList<String>{
        val textsList = mutableListOf<String>()
        for (id in chipGroup.checkedChipIds){
            val chip = chipGroup.findViewById<Chip>(id)
            textsList.add(chip.text.toString())
        }
        return textsList
    }

    private fun updateFromWhomFiltersData(){
        // get the names of all selected chips
        val whomChips = getCheckedChipsText(binding.fromWhomOptions.fromWhomChipGroup)
        // update live data
        filterViewModel.fromWhomFilters.value = whomChips
        filterViewModel.fromWhomCount.value = whomChips.size
    }

    private fun updateTypeFiltersData(){
        // get the names of all selected chips
        val typeChips = getCheckedChipsText(binding.typeOptions.typeChipGroup)
        // update live data
        filterViewModel.typeFilters.value = typeChips
        filterViewModel.typeCount.value = typeChips.size
    }

    private fun handleApplyFilterEnableState(){
        // button should be enabled if there is text in the exittext or if there are any chips selected in fromWhom or type
        binding.applyFiltersButton.isEnabled = filterViewModel.locationQuery.value!!.isNotBlank() || filterViewModel.fromWhomCount.value!! + filterViewModel.typeCount.value!! > 0
    }

    private fun handleAutocompleteVisibility(locationQuery: String){
        if (locationQuery.isBlank() || locationQuery.length < LENGTH_TO_SHOW_SUGGESTIONS) {
            binding.locationOptions.autoCompleteLocationsRecyclerView.visibility = View.GONE
            binding.locationOptions.itemLineDivider1.visibility = View.GONE
        } else {
            binding.locationOptions.autoCompleteLocationsRecyclerView.visibility = View.VISIBLE
            binding.locationOptions.itemLineDivider1.visibility = View.VISIBLE
        }
    }

    private fun uncheckChipGroup(chipGroup: ChipGroup){
        val checkedChipIdsList = chipGroup.checkedChipIds
        for (id in checkedChipIdsList){
            chipGroup.findViewById<Chip>(id).isChecked = false
        }
    }

    private fun clearFilters(){
        // close all option cards
        filterViewModel.closeOptionCards()
        // clear fromwhom selections
        uncheckChipGroup(binding.fromWhomOptions.fromWhomChipGroup)
        // clear type selections
        uncheckChipGroup(binding.typeOptions.typeChipGroup)
        // clear location box
        binding.locationOptions.locationSearch.text?.clear()
        // clear the live data
        filterViewModel.clearLiveDataFilters()
        // update text in apply filters button
        updateApplyFiltersText()
        // change visibility of any applied texts left
        binding.filterLocationExpandable.filtersAppliedText.visibility = View.GONE
        binding.filterFromWhomExpandable.filtersAppliedText.visibility = View.GONE
        binding.filterTypeExpandable.filtersAppliedText.visibility = View.GONE
    }

    // on click function for recycler view (autocomplete)
    override fun onAutocompleteLocationClick(locationSelected: String, placeId: String) {
        // update onSelectedLocation, latitude and longitude live data in view model
        filterViewModel.onSelectedLocation.value = locationSelected
        // TODO: Get Lat and Lng here for autocomplete selection from API
        filterViewModel.getLatLng(placeId)
    }

}

