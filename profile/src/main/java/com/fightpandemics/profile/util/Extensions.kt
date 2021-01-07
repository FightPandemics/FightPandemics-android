package com.fightpandemics.profile.util

fun String.capitalizeFirstLetter(): String =
    split(" ").joinToString(" ") { it.toLowerCase().capitalize() }