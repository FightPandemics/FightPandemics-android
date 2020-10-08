package com.fightpandemics.filter.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.filter.dagger.inject
import com.fightpandemics.home.R
import com.fightpandemics.home.databinding.FilterStartFragmentBinding
import com.fightpandemics.utils.ViewModelFactory
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.filter_location_options.view.*
import javax.inject.Inject

class FilterFragment : Fragment() {

    @Inject
    lateinit var filterViewModelFactory: ViewModelFactory

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
        savedInstanceState: Bundle?
    ): View? {
        binding = FilterStartFragmentBinding.inflate(inflater)
        binding.filterToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.filterLocationExpandable.apply {
            this.locationEmptyCard.apply {
                setOnClickListener {
                    toggleContents(binding.locationOptions.root, binding.filterLocationExpandable.locationEmptyCard)
                    var selectedLocationQuery = binding.locationOptions.root.location_search.query
                    if (selectedLocationQuery.isNotEmpty()){
                        binding.filterLocationExpandable.selectedChipsText.visibility = View.VISIBLE
                    }else{
                        binding.filterLocationExpandable.selectedChipsText.visibility = View.GONE
                    }
                }
            }
        }

        binding.filterFromWhomExpandable.apply {
            this.fromWhomEmptyCard.apply {
                setOnClickListener {
                    val selectedChips = binding.fromWhomOptions.fromWhomChipGroup.checkedChipIds
                    toggleContents(binding.fromWhomOptions.root, binding.filterFromWhomExpandable.fromWhomEmptyCard)
                }
            }
        }

        binding.filterTypeExpandable.apply {
            this.typeEmptyCard.apply {
                setOnClickListener {
                    toggleContents(binding.typeOptions.root, binding.filterTypeExpandable.typeEmptyCard)
                }
            }
        }

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
