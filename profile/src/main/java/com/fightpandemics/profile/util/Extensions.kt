package com.fightpandemics.profile.util

import java.util.*

fun String.capitalizeFirstLetter(): String =
    split(" ").joinToString(" ") { it.toLowerCase().capitalize() }

fun userInitials(userName: String?): String {
    val splitName = userName!!.split("\\s".toRegex()).toMutableList()
    val firstInitials = splitName[0][0]

    return when {
        splitName.size > 1 -> {
            val secondInitials = splitName[1][0]
            "$firstInitials$secondInitials".toUpperCase(Locale.ROOT)
        }
        else -> "$firstInitials".toUpperCase(Locale.ROOT)
    }
}

fun userInitials(firstName: String?, lastName: String?): String =
    firstName?.substring(0, 1)
        ?.toUpperCase() + lastName?.split(" ")?.last()
        ?.substring(0, 1)?.toUpperCase()