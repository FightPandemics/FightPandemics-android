package com.fightpandemics.filter.ui

import com.fightpandemics.home.BuildConfig
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.filter.dagger.inject
import com.fightpandemics.home.R
import com.fightpandemics.home.databinding.FilterStartFragmentBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.filter_location_options.view.*
import kotlinx.android.synthetic.main.filter_start_fragment.view.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

class FilterFragment : Fragment() {

    @Inject
    lateinit var filterViewModelFactory: ViewModelFactory
    private lateinit var filterViewModel: FilterViewModel
    private lateinit var binding: FilterStartFragmentBinding
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
    private val AUTOCOMPLETE_REQUEST_CODE = 1
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
            Observer { isExpanded ->
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
            Observer { isExpanded ->
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
                            "${whomSelectedChips} applied"
                    }
                }
                //whomSelectedChips?.let { x.plus(it) }
            })


        filterViewModel.isTypeOptionsExpanded.observe(viewLifecycleOwner, Observer { isExpanded ->
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
                        "${typeSelectedChips} applied"
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
        binding.locationOptions.locationSearch.setOnClickListener {
            launchPlacesIntent()
        }

        binding.locationOptions.shareMyLocation.setOnClickListener {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        Places.initialize(requireActivity().applicationContext, PLACES_API_KEY)
        // Create a new PlacesClient instance
        val placesClient = Places.createClient(requireContext())

        // Use fields to define the data types to return.
        val placeFields: List<Place.Field> = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG)

        // Use the builder to create a FindCurrentPlaceRequest.
        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

//            Toast.makeText(requireContext(), "You already have permissions, great!", Toast.LENGTH_SHORT).show()

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
        } else {
            // A local method to request required permissions;
            // See https://developer.android.com/training/permissions/requesting
            getLocationPermission()
        }
    }

    private fun getLocationPermission() {
        // TODO: Add custom alert dialog
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            AlertDialog.Builder(requireContext())
                .setTitle("Allow FightPandemics to access your location?")
                .setMessage("FightPandemics uses location to show hospitals and aid information near you.")
                .setPositiveButton("ALLOW", DialogInterface.OnClickListener { dialog, which ->
                    requestPermissions(
                        arrayOf("android.permission.ACCESS_FINE_LOCATION"),
                        STORAGE_PERMISSION_CODE
                    )
                })
                .setNegativeButton("CANCEL", DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                }).create().show()
        } else {
            requestPermissions(
                arrayOf("android.permission.ACCESS_FINE_LOCATION"),
                STORAGE_PERMISSION_CODE
            )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }

    }

    private fun launchPlacesIntent() {
        Places.initialize(requireActivity().applicationContext, PLACES_API_KEY)

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = listOf(Place.Field.ID, Place.Field.NAME)

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(requireActivity().applicationContext)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Timber.i("Place: ${place.name}, ${place.id}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Timber.i(status.statusMessage)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
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

}

