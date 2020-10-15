package com.fightpandemics.home.utils

import android.os.Build
import android.widget.TextView
import com.fightpandemics.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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

fun userInitials(userName: String?): String {
    val user_avatar_first_initials = userName?.split("\\s".toRegex())?.get(0)?.get(0)
    val user_avatar_second_initials = userName?.split("\\s".toRegex())?.get(1)?.get(0) // TODO - May throw IndexOutOfBoundsException if no second name
    val userInitials = "$user_avatar_first_initials$user_avatar_second_initials"
    return userInitials
}

// TODO 4 - Convert raw time to 2020-10-14T 22:57:54.009Z  - "yyyy-MM-dd'T'HH:mm:ss.SSSZ"	2001-07-04T12:08:56.235-0700
fun getDate(dateString: String?): String? {
    var df = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    var newDate: Date? = null
    try {
        newDate = df.parse(dateString)
        df = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return df.format(newDate)
}

fun getDisplayRuntime(runtime: Int): String? {
    val hours = runtime / 60
    val minutes = runtime % 60
    return "$hours h. $minutes m."
}