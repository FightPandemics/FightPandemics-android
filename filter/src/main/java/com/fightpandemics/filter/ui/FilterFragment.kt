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
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.filter.dagger.inject
import com.fightpandemics.home.R
import com.fightpandemics.home.databinding.FilterStartFragmentBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.*
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.filter_location_options.view.*
import kotlinx.android.synthetic.main.filter_start_fragment.*
import kotlinx.android.synthetic.main.filter_start_fragment.view.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

class FilterFragment : Fragment(), FilterAdapter.OnItemClickListener {

    @Inject
    lateinit var filterViewModelFactory: ViewModelFactory
    private lateinit var filterViewModel: FilterViewModel
    private lateinit var binding: FilterStartFragmentBinding
    private lateinit var placesClient: PlacesClient
    private var whomSelectedChips: Int? = 0
    private var typeSelectedChips: Int? = 0
    private var total: Int = 0

    // Places API variables
    private val STORAGE_PERMISSION_CODE = 1
    private val PLACES_API_KEY: String = BuildConfig.PLACES_API_KEY

    var x: Int by Delegates.observable(0) { prop, old, new ->
        when (new) {
            0 -> {
                binding.applyFiltersButton.isEnabled = false
                binding.applyFiltersButton.text =
                    getString(R.string.button_apply_filter)
            }
            1 -> {
                binding.applyFiltersButton.isEnabled = true
                binding.applyFiltersButton.text =
                    getString(R.string.button_apply_filter_, new)
            }
            else -> {
                binding.applyFiltersButton.isEnabled = true
                binding.applyFiltersButton.text =
                    getString(R.string.button_apply_filters, new)
            }
        }
    }

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

//                    // TODO: find a better way of writing this / uncomment this
                    // logic for hiding the applied text
                    val selectedLocationQuery =
                        binding.locationOptions.root.location_search.text.toString()
                    if (selectedLocationQuery != "") {
                        binding.filterLocationExpandable.filtersAppliedText.visibility =
                            View.VISIBLE
                    }

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

                    // TODO: find a better way of writing this
                    //Timber.e("QWERTYUIOP" + total.toString())
                    whomSelectedChips =
                        binding.fromWhomOptions.fromWhomChipGroup.checkedChipIds.size

                    total += whomSelectedChips!!

                    //Timber.e("ASDFGHJ" + total.toString())
                    if (whomSelectedChips!! > 0) {
                        binding.filterFromWhomExpandable.filtersAppliedText.visibility =
                            View.VISIBLE
                        binding.filterFromWhomExpandable.filtersAppliedText.text =
                            requireContext().getString(
                                R.string.card_applied_filters,
                                whomSelectedChips
                            )
                    }
                }
                //whomSelectedChips?.let { x.plus(it) }
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

                // TODO: find a better way of writing this
                typeSelectedChips = binding.typeOptions.typeChipGroup.checkedChipIds.size
                if (!binding.root.type_options.isVisible && typeSelectedChips!! > 0) {
                    binding.filterTypeExpandable.filtersAppliedText.visibility = View.VISIBLE
                    binding.filterTypeExpandable.filtersAppliedText.text =
                        requireContext().getString(R.string.card_applied_filters, typeSelectedChips)
                }
            }

//            when (typeSelectedChips) {
//                0 -> {
//                    binding.applyFiltersButton.isEnabled = false
//                    binding.applyFiltersButton.text =
//                        getString(R.string.button_apply_filter)
//                }
//                1 -> {
//                    binding.applyFiltersButton.isEnabled = true
//                    binding.applyFiltersButton.text =
//                        getString(R.string.button_apply_filter_, typeSelectedChips)
//                }
//                else -> {
//                    binding.applyFiltersButton.isEnabled = true
//                    binding.applyFiltersButton.text =
//                        getString(R.string.button_apply_filters, typeSelectedChips)
//                }
//            }

        })

        // Manage visibility for recycler view and line divider
        filterViewModel.locationQuery.observe(viewLifecycleOwner, { locationQuery ->
            if (locationQuery.isEmpty()) {
                binding.locationOptions.autoCompleteLocationsRecyclerView.visibility = View.GONE
                binding.locationOptions.itemLineDivider1.visibility = View.GONE
            } else {
                binding.locationOptions.autoCompleteLocationsRecyclerView.visibility = View.VISIBLE
                binding.locationOptions.itemLineDivider1.visibility = View.VISIBLE
            }
        })

        // handle selection of location in recycler view and from get current location
        filterViewModel.onSelectedLocation.observe(viewLifecycleOwner, { onSelectedLocation ->
            if (onSelectedLocation != null) {
                binding.locationOptions.locationSearch.setText(onSelectedLocation)
                binding.locationOptions.locationSearch.isEnabled = false
                binding.locationOptions.locationSearch.isEnabled = true
                // todo maybe make a function in the view model of this
                filterViewModel.onSelectedLocation.value = null
            }
        })

//        Timber.e("ZXCVBNM" + total.toString())
//        when (total) {
//            0 -> {
//                binding.applyFiltersButton.isEnabled = false
//                binding.applyFiltersButton.text =
//                    getString(R.string.button_apply_filter)
//            }
//            1 -> {
//                binding.applyFiltersButton.isEnabled = true
//                binding.applyFiltersButton.text =
//                    getString(R.string.button_apply_filter_, typeSelectedChips)
//            }
//            else -> {
//                binding.applyFiltersButton.isEnabled = true
//                binding.applyFiltersButton.text =
//                    getString(R.string.button_apply_filters, typeSelectedChips)
//            }
//        }

        binding.applyFiltersButton.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                "key",
                listOf("1", "2")
            )
            findNavController().popBackStack()
        }

        // Places API Logic
        // Initialize places sdk
        Places.initialize(requireActivity().applicationContext, PLACES_API_KEY)
        // Create a new PlacesClient instance
        placesClient = Places.createClient(requireContext())

        binding.locationOptions.locationSearch.doAfterTextChanged { text ->
            filterViewModel.autocompleteLocation(text.toString(), placesClient)
//            Timber.i("Live data: im gonig to update live data with $text")
            filterViewModel.locationQuery.value = text.toString()
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
                        STORAGE_PERMISSION_CODE
                    )
                }
                .setNegativeButton("CANCEL") { dialog, id ->
                    dialog.dismiss()
                }.create().show()
        }
        else {
            requestPermissions(
                arrayOf("android.permission.ACCESS_FINE_LOCATION"),
                STORAGE_PERMISSION_CODE
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
            STORAGE_PERMISSION_CODE -> {
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

    override fun onClick(locationSelected: String) {
        filterViewModel.onSelectedLocation.value = locationSelected
    }

}

