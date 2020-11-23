package com.fightpandemics.createpost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fightpandemics.createpost.databinding.FragmentSelectVisibilityBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectVisibilityFragment : BottomSheetDialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = SelectVisibilityFragment()
    }

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
    }
}