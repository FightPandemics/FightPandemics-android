package com.fightpandemics.createpost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fightpandemics.createpost.R
import com.fightpandemics.createpost.databinding.FragmentSelectDurationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectDurationFragment : BottomSheetDialogFragment() {

    private var fragmentSelectDurationBinding: FragmentSelectDurationBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSelectDurationBinding.inflate(inflater, container, false)
        fragmentSelectDurationBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        fragmentSelectDurationBinding!!.day.setOnClickListener {
            val durationText = fragmentSelectDurationBinding!!.day.text.toString()
            findNavController().previousBackStackEntry?.savedStateHandle?.set("duration", durationText)
            fragmentSelectDurationBinding!!.day.apply {
                setTextColor(resources.getColor(R.color.colorWhite))
                setBackgroundColor(resources.getColor(R.color.colorPrimary))
            }
            fragmentSelectDurationBinding!!.week.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
            fragmentSelectDurationBinding!!.month.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
            fragmentSelectDurationBinding!!.forever.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
        }
        fragmentSelectDurationBinding!!.week.setOnClickListener {
            val durationText = fragmentSelectDurationBinding!!.week.text.toString()
            findNavController().previousBackStackEntry?.savedStateHandle?.set("duration", durationText)
            fragmentSelectDurationBinding!!.week.apply {
                setTextColor(resources.getColor(R.color.colorWhite))
                setBackgroundColor(resources.getColor(R.color.colorPrimary))
            }
            fragmentSelectDurationBinding!!.day.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
            fragmentSelectDurationBinding!!.month.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
            fragmentSelectDurationBinding!!.forever.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
        }
        fragmentSelectDurationBinding!!.month.setOnClickListener {
            val durationText = fragmentSelectDurationBinding!!.month.text.toString()
            findNavController().previousBackStackEntry?.savedStateHandle?.set("duration", durationText)
            fragmentSelectDurationBinding!!.month.apply {
                setTextColor(resources.getColor(R.color.colorWhite))
                setBackgroundColor(resources.getColor(R.color.colorPrimary))
            }
            fragmentSelectDurationBinding!!.week.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
            fragmentSelectDurationBinding!!.day.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
            fragmentSelectDurationBinding!!.forever.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
        }
        fragmentSelectDurationBinding!!.forever.setOnClickListener {
            val durationText = fragmentSelectDurationBinding!!.forever.text.toString()
            findNavController().previousBackStackEntry?.savedStateHandle?.set("duration", durationText)
            fragmentSelectDurationBinding!!.forever.apply {
                setTextColor(resources.getColor(R.color.colorWhite))
                setBackgroundColor(resources.getColor(R.color.colorPrimary))
            }
            fragmentSelectDurationBinding!!.week.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
            fragmentSelectDurationBinding!!.month.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
            fragmentSelectDurationBinding!!.day.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
        }
    }
}