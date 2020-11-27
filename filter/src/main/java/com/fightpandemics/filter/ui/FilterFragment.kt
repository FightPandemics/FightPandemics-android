package com.fightpandemics.filter.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.LayoutTransition
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.core.widgets.BaseLocationFragment
import com.fightpandemics.filter.dagger.inject
import com.fightpandemics.filter.utils.*
import com.fightpandemics.home.R
import com.fightpandemics.home.databinding.FilterStartFragmentBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

/*
* created by Osaigbovo Odiase & Jose Li
* */
@FlowPreview
@ExperimentalCoroutinesApi
class FilterFragment : BaseLocationFragment(), FilterAdapter.OnItemClickListener {

    // constant for showing autocomplete suggestions
    private val LENGTH_TO_SHOW_SUGGESTIONS = 3
    private val adapter = FilterAdapter(this)

    @Inject
    lateinit var filterViewModelFactory: ViewModelFactory
    private val filterViewModel: FilterViewModel by viewModels { filterViewModelFactory }
    private var filterStartFragmentBinding: FilterStartFragmentBinding? = null
    private lateinit var defaultTransition: LayoutTransition

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FilterStartFragmentBinding.inflate(inflater)
        filterStartFragmentBinding = binding
        filterStartFragmentBinding!!.filterToolbar.setNavigationOnClickListener {
            dismissKeyboard(it)
            findNavController().navigateUp()
        }
        return binding.root
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get default transition
        defaultTransition = filterStartFragmentBinding!!.constraintLayoutOptions.layoutTransition

        // set up clear filter button
        filterStartFragmentBinding!!.clearFiltersButton.setOnClickListener {
            clearFilters()
        }
        // send data to home module
        filterStartFragmentBinding!!.applyFiltersButton.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                "filters",
                filterViewModel.createFilterRequest()
            )
            findNavController().popBackStack()
        }

        // set the custom adapter to the RecyclerView
        filterStartFragmentBinding!!.locationOptions.autoCompleteLocationsRecyclerView.adapter =
            adapter

        // Set toggle functionality to clickable filter cards
        filterStartFragmentBinding!!.filterLocationExpandable.locationEmptyCard.setOnClickListener {
            filterViewModel.toggleView(filterViewModel.isLocationOptionsExpanded)
            dismissKeyboard(it)
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
                    filterStartFragmentBinding!!.filterLocationExpandable.locationEmptyCard.expandContents(
                        filterStartFragmentBinding!!.locationOptions.root
                    )
                    filterStartFragmentBinding!!.filterLocationExpandable.filtersAppliedText.visibility =
                        View.GONE
                } else {
                    filterStartFragmentBinding!!.filterLocationExpandable.locationEmptyCard.collapseContents(
                        filterStartFragmentBinding!!.locationOptions.root
                    )

                    // logic for hiding the applied text
                    filterViewModel.locationQuery.value?.let {
                        if (it.isNotBlank()) {
                            filterStartFragmentBinding!!.filterLocationExpandable.filtersAppliedText.visibility =
                                View.VISIBLE
                        }
                    }
                }
            }
        )

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
                    filterStartFragmentBinding!!.filterFromWhomExpandable.fromWhomEmptyCard.expandContents(
                        filterStartFragmentBinding!!.fromWhomOptions.root
                    )
                    filterStartFragmentBinding!!.filterFromWhomExpandable.filtersAppliedText.visibility =
                        View.GONE
                } else {
                    filterStartFragmentBinding!!.filterFromWhomExpandable.fromWhomEmptyCard.collapseContents(
                        filterStartFragmentBinding!!.fromWhomOptions.root
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
            }
        )

        filterViewModel.isTypeOptionsExpanded.observe(
            viewLifecycleOwner,
            { isExpanded ->
                if (isExpanded) {
                    // close other two options cards, and stop transitions when closing to prevent glitchy look
                    // TODO: maybe make function that take list of card views to be closed in View Model
                    filterStartFragmentBinding!!.constraintLayoutOptions.layoutTransition = null
                    filterViewModel.isLocationOptionsExpanded.value = false
                    filterViewModel.isFromWhomOptionsExpanded.value = false

                    // re-enable transitions and open card
                    filterStartFragmentBinding!!.constraintLayoutOptions.layoutTransition =
                        defaultTransition
                    filterStartFragmentBinding!!.filterTypeExpandable.typeEmptyCard.expandContents(
                        filterStartFragmentBinding!!.typeOptions.root
                    )
                    filterStartFragmentBinding!!.filterTypeExpandable.filtersAppliedText.visibility =
                        View.GONE
                } else {
                    filterStartFragmentBinding!!.filterTypeExpandable.typeEmptyCard.collapseContents(
                        filterStartFragmentBinding!!.typeOptions.root
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
            }
        )

        // The next three observers check if apply filter button should be enabled
        filterViewModel.fromWhomCount.observe(
            viewLifecycleOwner,
            { fromWhomCount ->
                handleApplyFilterEnableState()
                updateApplyFiltersText()
            }
        )
        filterViewModel.typeCount.observe(
            viewLifecycleOwner,
            {
                handleApplyFilterEnableState()
                updateApplyFiltersText()
            }
        )
        filterViewModel.locationQuery.observe(
            viewLifecycleOwner,
            {
                handleApplyFilterEnableState()
                updateApplyFiltersText()
            }
        )

        searchLocation()
        shareLocation() // get user location and display it
    }

    // this function gets called inside getCurrentLocation from BaseLocationFragment
    override fun updateLocation(location: Location) {
        Timber.i("My filters from filter $location")
        filterViewModel.updateCurrentLocation(location)
    }

    override fun onDestroyView() {
        filterStartFragmentBinding = null
        super.onDestroyView()
    }

    private fun shareLocation() {
        filterStartFragmentBinding!!.locationOptions.shareMyLocation.setOnClickListener {
            filterStartFragmentBinding!!.locationOptions.locationSearch.setText("")
            getCurrentLocation()
            lifecycleScope.launchWhenStarted {
                filterViewModel.currentLocationState.collect {
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

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun searchLocation() {
        // do not start autocomplete until 3 chars in and delete location input that is not selected
        filterStartFragmentBinding!!.locationOptions.locationSearch.doAfterTextChanged { inputLocation ->

            lifecycleScope.launchWhenStarted {
                // do not search autocomplete suggestions until 3 chars in
                inputLocation?.let {
                    handleAutocompleteVisibility(it.toString())
                    Timber.e(it.toString())
                    filterViewModel.searchQuery.value = it.toString()
                    filterViewModel.searchLocationState.collect {
                        adapter.placesNames = it
                    }
                }
            }
            // if location in the editText is edited, delete location, lat, lgn live data
            filterViewModel.locationQuery.value = ""
        }
    }

    private fun bindLoading(isLoading: Boolean) {
        filterStartFragmentBinding!!.locationOptions.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    private fun displayLocation(address: String) {
        filterStartFragmentBinding!!.locationOptions
            .locationSearch
            .setText(address)

        filterViewModel.locationQuery.value = address
        // todo maybe find a better way of doing this -
        //  Take away focus from edit text once an option has been selected binding.searchText.requestFocus()
        filterStartFragmentBinding!!.locationOptions.locationSearch.isEnabled = false
        filterStartFragmentBinding!!.locationOptions.locationSearch.isEnabled = true
        // hide recycler view autocomplete location suggestions
        filterStartFragmentBinding!!.locationOptions.autoCompleteLocationsRecyclerView.visibility =
            View.GONE
        filterStartFragmentBinding!!.locationOptions.itemLineDivider1.visibility = View.GONE
    }

    // update number in text of apply filters button
    private fun updateApplyFiltersText() {
        when (val total = filterViewModel.getFiltersAppliedCount()) {
            0 ->
                filterStartFragmentBinding!!.applyFiltersButton.text =
                    requireContext().getString(R.string.button_apply_filter)
            1 ->
                filterStartFragmentBinding!!.applyFiltersButton.text =
                    requireContext().getString(R.string.button_apply_filter_, total)
            else ->
                filterStartFragmentBinding!!.applyFiltersButton.text =
                    requireContext().getString(R.string.button_apply_filters, total)
        }
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
        // button should be enabled if there is text in the exittext or
        // if there are any chips selected in fromWhom or type
        filterStartFragmentBinding!!.applyFiltersButton.isEnabled =
            filterViewModel.locationQuery.value!!.isNotBlank() ||
                    filterViewModel.fromWhomCount.value!! + filterViewModel.typeCount.value!! > 0
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

    private fun clearFilters() {
        // close all option cards
        filterViewModel.closeAllOptionCards()
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

    // On click function for recycler view (autocomplete)
    override fun onAutocompleteLocationClick(locationSelected: String) {
        displayLocation(locationSelected)
    }
}
