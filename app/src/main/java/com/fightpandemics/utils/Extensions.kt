package com.fightpandemics.utils

import android.os.Build
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.navigation.NavDestination
import com.fightpandemics.R
import com.google.android.material.bottomnavigation.BottomNavigationView

fun TextView.applyStyle(isSelected: Boolean) {
    val style = if (isSelected) R.style.TabText_Selected else R.style.TabText_Unselected
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.setTextAppearance(style)
    } else {
        this.setTextAppearance(context, style)
    }
}

