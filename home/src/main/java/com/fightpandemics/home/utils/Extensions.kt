package com.fightpandemics.home.utils

import android.os.Build
import android.widget.TextView
import com.fightpandemics.R

val TAB_TITLES = arrayOf(
    com.fightpandemics.home.R.string.tab_all,
    com.fightpandemics.home.R.string.tab_offers,
    com.fightpandemics.home.R.string.tab_requests
)

fun TextView.applyStyle(isSelected: Boolean) {
    val style = if (isSelected) R.style.TabText_Selected else R.style.TabText_Unselected
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.setTextAppearance(style)
    } else {
        this.setTextAppearance(context, style)
    }
}
