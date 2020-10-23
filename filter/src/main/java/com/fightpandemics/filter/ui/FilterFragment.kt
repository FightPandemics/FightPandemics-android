package com.fightpandemics.filter.ui

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.provider.VoicemailContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fightpandemics.filter.dagger.inject
import com.fightpandemics.home.BuildConfig
import com.fightpandemics.home.R
import com.fightpandemics.home.databinding.FilterStartFragmentBinding
import com.fightpandemics.utils.ViewModelFactory
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.filter_location_options.view.*
import kotlinx.android.synthetic.main.filter_start_fragment.view.*
import timber.log.Timber
import javax.inject.Inject

class FilterFragment : Fragment() {

    @Inject
    lateinit var filterViewModelFactory: ViewModelFactory
    private lateinit var filterViewModel: FilterViewModel
    private lateinit var binding: FilterStartFragmentBinding

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

        binding.filterLocationExpandable.locationEmptyCard.apply {
            setOnClickListener {
                filterViewModel.toggleView(filterViewModel.isLocationOptionsExpanded)
            }
        }

        binding.filterFromWhomExpandable.fromWhomEmptyCard.apply {
            setOnClickListener {
                filterViewModel.toggleView(filterViewModel.isFromWhomOptionsExpanded)
            }
        }

        binding.filterTypeExpandable.typeEmptyCard.apply {
            setOnClickListener {
                filterViewModel.toggleView(filterViewModel.isTypeOptionsExpanded)
            }
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
//                    val selectedLocationQuery = binding.locationOptions.root.location_search.query
//                    if (selectedLocationQuery.isNotEmpty()) {
//                        binding.filterLocationExpandable.filtersAppliedText.visibility =
//                            View.VISIBLE
//                    }
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
                    val selectedChips =
                        binding.fromWhomOptions.fromWhomChipGroup.checkedChipIds.size
                    if (selectedChips > 0) {
                        binding.filterFromWhomExpandable.filtersAppliedText.visibility =
                            View.VISIBLE
                        binding.filterFromWhomExpandable.filtersAppliedText.text =
                            "${selectedChips} applied"
                    }

                }
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
                val selectedChips = binding.typeOptions.typeChipGroup.checkedChipIds.size
                if (!binding.root.type_options.isVisible && selectedChips > 0) {
                    binding.filterTypeExpandable.filtersAppliedText.visibility = View.VISIBLE
                    binding.filterTypeExpandable.filtersAppliedText.text =
                        "${selectedChips} applied"
                }

            }
        })

        setupPlaces()

    }

    private fun setupPlaces(){

//        val apiKey = "AIzaSyCASyXjbvJ-0zNDcmFU625zaPLfEfGIFBA"
        // Initialize the SDK
        Places.initialize(requireActivity().applicationContext, PLACES_API_KEY)
        // Create a new PlacesClient instance
        val placesClient = Places.createClient(requireActivity().applicationContext)

        // Initialize the AutocompleteSupportFragment.
//        val autocompleteFragment = binding.locationOptions.autocompleteFragment as AutocompleteSupportFragment
        val autocompleteFragment  = childFragmentManager.findFragmentByTag("autocomplete_fragment") as? AutocompleteSupportFragment ?: return Timber.i("Couldnt find the child")

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Timber.i( "Place: ${place.name}, ${place.id}")
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Timber.i( "An error occurred: $status")
            }
        })

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

