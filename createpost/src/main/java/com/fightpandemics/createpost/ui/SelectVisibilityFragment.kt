package com.fightpandemics.createpost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fightpandemics.createpost.R
import com.fightpandemics.createpost.databinding.FragmentSelectVisibilityBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectVisibilityFragment : BottomSheetDialogFragment() {

    private var fragmentSelectVisibilityBinding: FragmentSelectVisibilityBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSelectVisibilityBinding.inflate(inflater, container, false)
        fragmentSelectVisibilityBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        fragmentSelectVisibilityBinding!!.neighbourhood.setOnClickListener {
            val visibilityText = fragmentSelectVisibilityBinding!!.neighbourhood.text.toString()
            findNavController().previousBackStackEntry?.savedStateHandle?.set("visibility", visibilityText)
            fragmentSelectVisibilityBinding!!.neighbourhood.apply {
                setTextColor(resources.getColor(R.color.colorWhite))
                setBackgroundColor(resources.getColor(R.color.colorPrimary))
            }
            fragmentSelectVisibilityBinding!!.city.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
            fragmentSelectVisibilityBinding!!.country.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
            fragmentSelectVisibilityBinding!!.anyone.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
        }
        fragmentSelectVisibilityBinding!!.city.setOnClickListener {
            val visibilityText = fragmentSelectVisibilityBinding!!.city.text.toString()
            findNavController().previousBackStackEntry?.savedStateHandle?.set("visibility", visibilityText)
            fragmentSelectVisibilityBinding!!.city.apply {
                setTextColor(resources.getColor(R.color.colorWhite))
                setBackgroundColor(resources.getColor(R.color.colorPrimary))
            }
            fragmentSelectVisibilityBinding!!.neighbourhood.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
            fragmentSelectVisibilityBinding!!.country.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
            fragmentSelectVisibilityBinding!!.anyone.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
        }
        fragmentSelectVisibilityBinding!!.country.setOnClickListener {
            val visibilityText = fragmentSelectVisibilityBinding!!.country.text.toString()
            findNavController().previousBackStackEntry?.savedStateHandle?.set("visibility", visibilityText)
            fragmentSelectVisibilityBinding!!.country.apply {
                setTextColor(resources.getColor(R.color.colorWhite))
                setBackgroundColor(resources.getColor(R.color.colorPrimary))
            }
            fragmentSelectVisibilityBinding!!.neighbourhood.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
            fragmentSelectVisibilityBinding!!.city.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
            fragmentSelectVisibilityBinding!!.anyone.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
        }
        fragmentSelectVisibilityBinding!!.anyone.setOnClickListener {
            val visibilityText = fragmentSelectVisibilityBinding!!.anyone.text.toString()
            findNavController().previousBackStackEntry?.savedStateHandle?.set("visibility", visibilityText)
            fragmentSelectVisibilityBinding!!.anyone.apply {
                setTextColor(resources.getColor(R.color.colorWhite))
                setBackgroundColor(resources.getColor(R.color.colorPrimary))
            }
            fragmentSelectVisibilityBinding!!.neighbourhood.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
            fragmentSelectVisibilityBinding!!.city.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
            fragmentSelectVisibilityBinding!!.country.apply {
                setBackgroundColor(resources.getColor(R.color.fightPandemicsWhiteSmoke))
                setTextColor(resources.getColor(R.color.textColorPrimary))
            }
        }
    }
}