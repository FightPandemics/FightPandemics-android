package com.fightpandemics.createpost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.fightpandemics.createpost.databinding.FragmentSelectTagBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class SelectTagFragment : BottomSheetDialogFragment() {

    private var fragmentSelectTagBinding: FragmentSelectTagBinding? = null
    private val chipTexts: ArrayList<String> = ArrayList()

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

        setupView()
    }

    private fun setupView() {
        fragmentSelectTagBinding!!.close.setOnClickListener {
            if (chipTexts.size == 3) {
                findNavController().previousBackStackEntry?.savedStateHandle?.set("tag1", chipTexts[0])
                findNavController().previousBackStackEntry?.savedStateHandle?.set("tag2", chipTexts[1])
                findNavController().previousBackStackEntry?.savedStateHandle?.set("tag3", chipTexts[2])
            }
        }
        fragmentSelectTagBinding!!.confirm.setOnClickListener {
            if (chipTexts.size == 3) {
                findNavController().previousBackStackEntry?.savedStateHandle?.set("tag1", chipTexts[0])
                findNavController().previousBackStackEntry?.savedStateHandle?.set("tag2", chipTexts[1])
                findNavController().previousBackStackEntry?.savedStateHandle?.set("tag3", chipTexts[2])
            }
        }

        populateArray(fragmentSelectTagBinding!!.chipMedical)
        populateArray(fragmentSelectTagBinding!!.chipUniversity)
        populateArray(fragmentSelectTagBinding!!.chipRAndD)
        populateArray(fragmentSelectTagBinding!!.chipBusiness)
        populateArray(fragmentSelectTagBinding!!.chipLegal)
        populateArray(fragmentSelectTagBinding!!.chipInformation)
        populateArray(fragmentSelectTagBinding!!.chipFunding)
        populateArray(fragmentSelectTagBinding!!.chipEntertainment)
        populateArray(fragmentSelectTagBinding!!.chipGroceries)
        populateArray(fragmentSelectTagBinding!!.chipWellBeing)
        populateArray(fragmentSelectTagBinding!!.chipTech)
        populateArray(fragmentSelectTagBinding!!.chipOthers)
    }

    private fun populateArray(chip: Chip) {
        chip.setOnCheckedChangeListener { buttonView, isChecked ->
            var chipText: String?
            if (isChecked) {
                chipText = buttonView!!.text.toString()
                if (chipTexts.size < 3 && !chipTexts.contains(chipText)) {
                    chipTexts.add(chipText)
                } else {
                    buttonView.isChecked = false
                }
            }

            if (!isChecked) {
                chipText = buttonView!!.text.toString()
                if (chipTexts.contains(chipText)) {
                    chipTexts.remove(chipText)
                }
            }
        }
    }
}