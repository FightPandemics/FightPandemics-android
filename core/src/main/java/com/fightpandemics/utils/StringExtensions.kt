package com.fightpandemics.utils


import android.util.Base64
import android.util.Patterns
import java.util.*

fun String.extractValueBetween(firstValue: String, secondValue: String): String {

    return substringAfter(firstValue).substringBefore(secondValue)
}

fun String.encodeToBase64(): String {
    return Base64.encodeToString(this.toByteArray(charset("UTF-8")), Base64.NO_WRAP)
}

fun CharSequence.second(): Char {
    if (isEmpty() || this.length < 2)
        throw NoSuchElementException("Char sequence is empty.")
    return this[1]
}

fun String.getInitials(): String {
    return this.toUpperCase(Locale.getDefault()).split(' ')
        .mapNotNull { it.firstOrNull()?.toString() }
        .reduce { allFirstLetter, lastLetter ->
            val initialsLetters = allFirstLetter + lastLetter
            initialsLetters.first().toString() + initialsLetters.second().toString()
        }
}

fun String.firstName(): String {
    return if (this.split(" ").size > 1) this.substring(0, this.indexOf(" ")) else this
}

fun String.firstAndSecondName(): String {
    var firstAndSecondName: String
    this.split(" ").let { list ->
        firstAndSecondName = if (list.size > 1) {
            list.first().plus(" ").plus(list[1])
        } else this
    }

    return firstAndSecondName
}

fun String.isValidEmail(): Boolean = this.isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this.trim()).matches()

// TODO check if there are other rules for password like at least 8 chars
fun String.isValidPassword(): Boolean = this.isNotEmpty()


fun String.isValidRePassword(pass : String): Boolean = this?.isNotEmpty() && pass?.isNotEmpty() && this.equals(pass)






