package com.fightpandemics.createpost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fightpandemics.createpost.databinding.FragmentSelectDurationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectDurationFragment : BottomSheetDialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = SelectDurationFragment()
    }

    private var fragmentSelectDurationBinding: FragmentSelectDurationBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSelectDurationBinding.inflate(inflater, container, false)
        fragmentSelectDurationBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}