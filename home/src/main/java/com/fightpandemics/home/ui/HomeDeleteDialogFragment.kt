package com.fightpandemics.home.ui

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeDeleteDialogFragment : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = MaterialAlertDialogBuilder(requireContext(), theme)
        return builder.create()
    }

}