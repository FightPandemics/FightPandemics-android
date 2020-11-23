package com.fightpandemics.createpost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fightpandemics.createpost.databinding.FragmentSelectOrganizationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectOrganizationFragment : BottomSheetDialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = SelectOrganizationFragment()
    }

    private var fragmentSelectOrganizationBinding: FragmentSelectOrganizationBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSelectOrganizationBinding.inflate(inflater, container, false)
        fragmentSelectOrganizationBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}