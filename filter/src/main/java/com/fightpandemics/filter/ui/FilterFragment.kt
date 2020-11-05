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
import kotlinx.android.synthetic.main.filter_start_fragment.view.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

class FilterFragment : Fragment() , FilterAdapter.OnItemClickListener {

    @Inject
    lateinit var filterViewModelFactory: ViewModelFactory
    private lateinit var filterViewModel: FilterViewModel
    private lateinit var binding: FilterStartFragmentBinding
    private lateinit var placesClient: PlacesClient
    private var whomSelectedChips: Int? = 0
    private var typeSelectedChips: Int? = 0
    private var total: Int = 0

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

    // Places API variables
    private val STORAGE_PERMISSION_CODE = 1
    private val PLACES_API_KEY: String = BuildConfig.PLACES_API_KEY

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
        binding.locationOptions.autoCompleteLocationsRecyclerView.apply {
            // RecyclerView behavior
            // set the custom adapter to the RecyclerView
            this.adapter = adapter
        }

        // update recycler view list if data observed changes
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
                            requireContext().getString(R.string.card_applied_filters, whomSelectedChips)
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
        filterViewModel.locationQuery.observe(viewLifecycleOwner, {locationQuery ->
            if (locationQuery.isEmpty()){
                binding.locationOptions.autoCompleteLocationsRecyclerView.visibility = View.GONE
                binding.locationOptions.itemLineDivider1.visibility = View.GONE
            }else{

//                binding.locationOptions.locationSearch.setText( locationQuery )

                binding.locationOptions.autoCompleteLocationsRecyclerView.visibility = View.VISIBLE
                binding.locationOptions.itemLineDivider1.visibility = View.VISIBLE
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
            Timber.d("Places: %s", text)
            filterViewModel.autocompleteLocation(text.toString(), placesClient)
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
//            filterViewModel.requestCurrentLocation(placesClient)
            requestCurrentLocation()
        } else {
            // A local method to request required permissions;
            getLocationPermission()
        }
    }

    private fun requestCurrentLocation(){

        // Use fields to define the data types to return.
        val placeFields: List<Place.Field> = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG)

        // Use the builder to create a FindCurrentPlaceRequest.
        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

        val placeResponse = placesClient.findCurrentPlace(request)
        placeResponse.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val response = task.result
                binding.locationOptions.locationSearch.setText(
                    response.placeLikelihoods[0].place.address ?: "Not found"
                )
//                    for (placeLikelihood: PlaceLikelihood in response?.placeLikelihoods
//                        ?: emptyList()) {
//                        binding.locationOptions.locationSearch.setText(placeLikelihood.place.address)
//                        break
//                        Timber.i("Place '${placeLikelihood.place.address}', '${placeLikelihood.place.latLng}' has likelihood: ${placeLikelihood.likelihood}")
//                        Toast.makeText(requireContext(), "Place '${placeLikelihood.place.address}', '${placeLikelihood.place.latLng}' has likelihood: ${placeLikelihood.likelihood}", Toast.LENGTH_SHORT).show()
//                    }
            } else {
                val exception = task.exception
                if (exception is ApiException) {
                    Timber.e("Place not found: ${exception.statusCode}")
                    Toast.makeText(
                        requireContext(),
                        "Place not found: Exception status code ${exception.statusCode}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    private fun getLocationPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            val dialogView = layoutInflater.inflate(R.layout.location_permission_dialog,null)
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
//        Toast.makeText(requireContext(), "hello from $position", Toast.LENGTH_SHORT).show()

//        filterViewModel.locationQuery.value = locationSelected
        // TODO change back if doesnt work
        binding.locationOptions.locationSearch.setText(
            locationSelected
        )
    }

}

