package com.fightpandemics.home.utils

import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.fightpandemics.R
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
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
    val user_avatar_second_initials = userName?.split("\\s".toRegex())?.get(1)
        ?.get(0) // TODO 3 - May throw IndexOutOfBoundsException if no second name
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

fun getPostCreated(createdAt: String?): String? {

    // FOR API >= 26
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        val readDateTimeFormatter = DateTimeFormatter
//            .ISO_OFFSET_DATE_TIME
//            .withZone(ZoneOffset.UTC)
//        val utcLocalDateTime = LocalDateTime.parse(createdAt, readDateTimeFormatter)
//        val localZonedDateTime = utcLocalDateTime
//            .atOffset(ZoneOffset.UTC)
//            .atZoneSameInstant(ZoneId.systemDefault())
//        val created = localZonedDateTime.toInstant()
//
//        val now = Instant.now()
//        val elapsedDuration: Duration = Duration.between(created, now)
//
//        Timber.e(elapsedDuration.seconds.toString())
//
//        val elapsed = calculateTime(elapsedDuration)
//        return elapsed
    } else {


    }

    val readDateTimeFormatter = DateTimeFormatter
        .ISO_OFFSET_DATE_TIME
        .withZone(ZoneOffset.UTC)
    val utcLocalDateTime = LocalDateTime.parse(createdAt, readDateTimeFormatter)
    val localZonedDateTime = utcLocalDateTime
        .atOffset(ZoneOffset.UTC)
        .atZoneSameInstant(ZoneId.systemDefault())
    
    val created = localZonedDateTime.toInstant()

    val now = Instant.now()

    val elapsedDuration: Duration = Duration.between(created, now)

    Timber.e(elapsedDuration.seconds.toString())

    return created.toString()

}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateTime(elapsedDuration: Duration): String? {
    val day = elapsedDuration.toDays().toInt()
    val hr = elapsedDuration.toHours().toInt()
    val min = elapsedDuration.toMinutes().toInt()
    val sec = elapsedDuration.seconds.toInt()

    when {
        day >= 365 -> return "${day / 365}y"
        day in 30..364 -> return "${day / 30}m"
        day in 7..29 -> return "${day / 7}w"
        day >= 1 -> return "${day}d"
        hr in 1..23 -> return "${hr}h"
        min in 1..60 -> return "${min}m"
        sec in 1..60 -> return "${sec}s"
        sec == 0 -> return "now"
    }
    return null
}
