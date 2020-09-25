package com.fightpandemics.filter.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fightpandemics.home.R
import com.fightpandemics.home.databinding.FilterStartFragmentBinding

class FilterFragment : Fragment() {

    private val viewModel: FilterFragmentViewModel by viewModels()
    private lateinit var binding: FilterStartFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FilterStartFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.filterLocationExpandable.apply {
            setOnClickListener {
                toggleContents(binding.locationOptions.root, binding.filterLocationExpandable)
            }
        }

        binding.filterFromWhomExpandable.apply {
            setOnClickListener {
                toggleContents(binding.fromWhomOptions.root, binding.filterFromWhomExpandable)
            }
        }

        binding.filterTypeExpandable.apply {
            setOnClickListener {
                toggleContents(binding.typeOptions.root, binding.filterTypeExpandable)
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
