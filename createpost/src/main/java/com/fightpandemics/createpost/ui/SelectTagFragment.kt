package com.fightpandemics.createpost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fightpandemics.createpost.databinding.FragmentSelectTagBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectTagFragment : BottomSheetDialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = SelectTagFragment()
    }

    private var fragmentSelectTagBinding: FragmentSelectTagBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSelectTagBinding.inflate(inflater, container, false)
        fragmentSelectTagBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}