package com.fightpandemics.profile.util

fun String.capitalizeFirstLetter(): String {
    return this.substring(0, 1).toUpperCase() + this.substring(1).toLowerCase();
}

