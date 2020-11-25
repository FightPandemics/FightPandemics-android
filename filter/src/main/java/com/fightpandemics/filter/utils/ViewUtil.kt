package com.fightpandemics.filter.utils

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
