package com.fightpandemics.filter.ui

import com.fightpandemics.home.BuildConfig
import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
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
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.transition.MaterialSharedAxis
import timber.log.Timber
import javax.inject.Inject

class FilterFragment : Fragment(), FilterAdapter.OnItemClickListener {

    @Inject
    lateinit var filterViewModelFactory: ViewModelFactory
    private lateinit var filterViewModel: FilterViewModel
    private lateinit var binding: FilterStartFragmentBinding
    private lateinit var placesClient: PlacesClient

    // Places API variables
    private val LOCATION_PERMISSION_CODE = 1
    private val PLACES_API_KEY: String = BuildConfig.PLACES_API_KEY

    // constant for showing autocomplete suggestions
    private val LENGTH_TO_SHOW_SUGGESTIONS = 3

//    var x: Int by Delegates.observable(0) { prop, old, new ->
//        when (new) {
//            0 -> {
//                binding.applyFiltersButton.isEnabled = false
//                binding.applyFiltersButton.text =
//                    getString(R.string.button_apply_filter)
//            }
//            1 -> {
//                binding.applyFiltersButton.isEnabled = true
//                binding.applyFiltersButton.text =
//                    getString(R.string.button_apply_filter_, new)
//            }
//            else -> {
//                binding.applyFiltersButton.isEnabled = true
//                binding.applyFiltersButton.text =
//                    getString(R.string.button_apply_filters, new)
//            }
//        }
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
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
        // Places API Logic - Initialize places sdk
        Places.initialize(requireActivity().applicationContext, PLACES_API_KEY)
        // Create a new PlacesClient instance
        placesClient = Places.createClient(requireContext())

        // set up apply and clear filters buttons
        binding.clearFiltersButton.setOnClickListener{
            clearFilters()
        }

        // send data to home module
        binding.applyFiltersButton.setOnClickListener {

//            // todo try to make this call wait for response before continuing
//            if (filterViewModel.locationQuery.value?.isNotBlank() == true){
//                filterViewModel.autocompleteLocationLatLng(placesClient)
//                Timber.i("LAT: after get call: ${filterViewModel.latLgn.value}")
//            }

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
            it?.let {
                adapter.data = it
            }
        })

        binding.filterLocationExpandable.locationEmptyCard.setOnClickListener {
            filterViewModel.toggleView(filterViewModel.isLocationOptionsExpanded)
        }

        binding.filterFromWhomExpandable.fromWhomEmptyCard.setOnClickListener {
            filterViewModel.toggleView(filterViewModel.isFromWhomOptionsExpanded)
        }

        binding.filterTypeExpandable.typeEmptyCard.setOnClickListener {
            filterViewModel.toggleView(filterViewModel.isTypeOptionsExpanded)
        }

        // setonclick listeners for fromWhom chips
        for (fromWhomChip in binding.fromWhomOptions.fromWhomChipGroup.children){
            fromWhomChip.setOnClickListener {
                updateFromWhomFiltersData()
            }
        }

        // setonclick listeners for type chips
        for (typeChip in binding.typeOptions.typeChipGroup.children){
            typeChip.setOnClickListener {
                updateTypeFiltersData()
            }
        }

        filterViewModel.isLocationOptionsExpanded.observe(
            viewLifecycleOwner,
            { isExpanded ->
                if (isExpanded) {
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

                    // update apply filters count
                    updateApplyFiltersText()
                }
            })

        filterViewModel.isFromWhomOptionsExpanded.observe(
            viewLifecycleOwner,
            { isExpanded ->
                if (isExpanded) {
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

                    // update apply filters count
                    updateApplyFiltersText()
                }

            })

        filterViewModel.isTypeOptionsExpanded.observe(viewLifecycleOwner, { isExpanded ->
            if (isExpanded) {
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

            // update apply filters count
            updateApplyFiltersText()

        })

        // The next three observers check if apply filter button should be enabled
        filterViewModel.fromWhomCount.observe(viewLifecycleOwner, {fromWhomCount ->
            handleApplyFilterEnableState()
        })
        filterViewModel.typeCount.observe(viewLifecycleOwner, {typeCount ->
            handleApplyFilterEnableState()
        })
        filterViewModel.locationQuery.observe(viewLifecycleOwner, { locationQuery ->
            handleApplyFilterEnableState()
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
                // todo maybe make a function in the view model of this
                // reset onSelectedLocation event to null because we finished selecting
                filterViewModel.onSelectedLocation.value = null
            }
        })

        // do not start autocomplete until 3 chars in and delete location input that is not selected
        binding.locationOptions.locationSearch.doAfterTextChanged { inputLocation ->
            // if statement to recognize a change to selectedlocation
            // todo -> delete live data
            // do not search autocomplete suggestions until 3 chars in
            inputLocation?.let {
                handleAutocompleteVisibility(it.toString())
                if (it.length >= LENGTH_TO_SHOW_SUGGESTIONS){
                    filterViewModel.autocompleteLocation(it.toString(), placesClient)
                }
            }
            filterViewModel.locationQuery.value = ""
            // todo if location is not selected, then delete it and dont update live data
            // todo if text is edited then delete our current locationQuery livedata
//            Timber.i("Filters Live data: im gonig to update live data with ${filterViewModel.locationQuery.value}")
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
            filterViewModel.requestCurrentLocation(placesClient)
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
//        Timber.i("Update FromWhom called: ${filterViewModel.fromWhomFilters.value}")
    }

    private fun updateTypeFiltersData(){
        // get the names of all selected chips
        val typeChips = getCheckedChipsText(binding.typeOptions.typeChipGroup)
        // update live data
        filterViewModel.typeFilters.value = typeChips
        filterViewModel.typeCount.value = typeChips.size
//        Timber.i("Update type called: ${filterViewModel.typeFilters.value}")
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
        // clear fromwhom selections
        uncheckChipGroup(binding.fromWhomOptions.fromWhomChipGroup)
        // clear type selections
        uncheckChipGroup(binding.typeOptions.typeChipGroup)
        // clear location box
        binding.locationOptions.locationSearch.text?.clear()
        // clear the live data
        filterViewModel.clearLiveDataFilters()
        // change visibility of any applied texts left
        binding.filterLocationExpandable.filtersAppliedText.visibility = View.GONE
        binding.filterFromWhomExpandable.filtersAppliedText.visibility = View.GONE
        binding.filterTypeExpandable.filtersAppliedText.visibility = View.GONE
    }

    // on click function for recycler view (autocomplete)
    override fun onAutocompleteLocationClick(locationSelected: String) {
        filterViewModel.onSelectedLocation.value = locationSelected
    }

}

