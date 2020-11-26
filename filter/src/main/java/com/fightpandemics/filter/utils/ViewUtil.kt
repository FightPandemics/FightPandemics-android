package com.fightpandemics.filter.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.fightpandemics.home.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

fun uncheckChipGroup(chipGroup: ChipGroup) {
    val checkedChipIdsList = chipGroup.checkedChipIds
    for (id in checkedChipIdsList) {
        chipGroup.findViewById<Chip>(id).isChecked = false
    }
}

// Returns a list with the text of all checked chips
fun getCheckedChipsText(chipGroup: ChipGroup): MutableList<String> {
    val textsList = mutableListOf<String>()
    for (id in chipGroup.checkedChipIds) {
        val chip = chipGroup.findViewById<Chip>(id)
        textsList.add(chip.text.toString())
    }
    return textsList
}

fun TextView.expandContents(optionsView: View) {
    optionsView.visibility = View.VISIBLE
    setCompoundDrawablesRelativeWithIntrinsicBounds(
        0,
        0,
        R.drawable.ic_minus_sign,
        0
    )
}

fun TextView.collapseContents(optionsView: View) {
    optionsView.visibility = View.GONE
    setCompoundDrawablesRelativeWithIntrinsicBounds(
        0,
        0,
        R.drawable.ic_plus_sign,
        0
    )
}

fun dismissKeyboard(view: View) {
    val imm =
        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
