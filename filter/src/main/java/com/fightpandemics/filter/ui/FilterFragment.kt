package com.fightpandemics.filter.ui

import android.content.Context
import android.os.Bundle
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
import com.fightpandemics.home.R
import com.fightpandemics.home.databinding.FilterStartFragmentBinding
import com.fightpandemics.utils.ViewModelFactory
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.filter_location_options.view.*
import kotlinx.android.synthetic.main.filter_start_fragment.view.*
import javax.inject.Inject

class FilterFragment : Fragment() {

    @Inject
    lateinit var filterViewModelFactory: ViewModelFactory
    private lateinit var filterViewModel: FilterViewModel

    private lateinit var binding: FilterStartFragmentBinding

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
            setOnClickListener{
                filterViewModel.toggleView(filterViewModel.isLocationOptionsExpanded)
            }
        }

        filterViewModel.isLocationOptionsExpanded.observe(viewLifecycleOwner, Observer {isExpanded->
            if (isExpanded){
                expandContents(binding.locationOptions.root, binding.filterLocationExpandable.locationEmptyCard)
                binding.filterLocationExpandable.filtersAppliedText.visibility = View.GONE
            }else {
                collapseContents(binding.locationOptions.root, binding.filterLocationExpandable.locationEmptyCard)

                // TODO: find a better way of writing this
                val selectedLocationQuery = binding.locationOptions.root.location_search.query
                if (selectedLocationQuery.isNotEmpty()) {
                    binding.filterLocationExpandable.filtersAppliedText.visibility = View.VISIBLE
                }

            }
        })

//        binding.filterLocationExpandable.apply {
//            this.locationEmptyCard.apply {
//                setOnClickListener {
//                    toggleContents(binding.locationOptions.root, binding.filterLocationExpandable.locationEmptyCard)
//                    var selectedLocationQuery = binding.locationOptions.root.location_search.query
//                    if (selectedLocationQuery.isNotEmpty() && !binding.root.location_options.isVisible) {
//                        binding.filterLocationExpandable.filtersAppliedText.visibility =
//                            View.VISIBLE
//                    } else {
//                        binding.filterLocationExpandable.filtersAppliedText.visibility = View.GONE
//                    }
//                }
//            }
//        }

//        binding.filterLocationExpandable.locationEmptyCard.apply {
//            setOnClickListener{
//                filterViewModel.toggleView(filterViewModel.isLocationOptionsExpanded)
//            }
//        }

        binding.filterFromWhomExpandable.apply {
            this.fromWhomEmptyCard.apply {
                setOnClickListener {
                    val selectedChips =
                        binding.fromWhomOptions.fromWhomChipGroup.checkedChipIds.size
                    toggleContents(binding.fromWhomOptions.root,
                        binding.filterFromWhomExpandable.fromWhomEmptyCard)
                    if (!binding.root.from_whom_options.isVisible && selectedChips > 0) {
                        binding.filterFromWhomExpandable.filtersAppliedText.visibility =
                            View.VISIBLE
                        binding.filterFromWhomExpandable.filtersAppliedText.text =
                            "${selectedChips} applied"
                    } else {
                        binding.filterFromWhomExpandable.filtersAppliedText.visibility = View.GONE
                    }
                }
            }
        }

        binding.filterTypeExpandable.apply {
            this.typeEmptyCard.apply {
                setOnClickListener {
                    val selectedChips = binding.typeOptions.typeChipGroup.checkedChipIds.size
                    toggleContents(binding.typeOptions.root,
                        binding.filterTypeExpandable.typeEmptyCard)
                    if (!binding.root.type_options.isVisible && selectedChips > 0) {
                        binding.filterTypeExpandable.filtersAppliedText.visibility = View.VISIBLE
                        binding.filterTypeExpandable.filtersAppliedText.text =
                            "${selectedChips} applied"
                    } else {
                        binding.filterTypeExpandable.filtersAppliedText.visibility = View.GONE
                    }
                }
            }

        }
    }


    private fun expandContents(optionsView: View, clickableTextView: TextView){
        optionsView.visibility = View.VISIBLE
        clickableTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_minus_sign,
            0
        )
    }

    private fun collapseContents(optionsView: View, clickableTextView: TextView){
        optionsView.visibility = View.GONE
        clickableTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_plus_sign,
            0
        )
    }


    private fun toggleContents(optionsView: View, clickableTextView: TextView) {
        if (optionsView.visibility == View.VISIBLE) {
            optionsView.visibility = View.GONE
            clickableTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_plus_sign,
                0
            )
        } else {
            optionsView.visibility = View.VISIBLE
            clickableTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_minus_sign,
                0
            )
        }
    }

}

