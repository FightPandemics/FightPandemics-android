package com.fightpandemics.home.utils

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.fightpandemics.core.utils.Constants
import com.fightpandemics.home.R
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

fun userInitials(userName: String?): String {
    val splitName = userName!!.trim().split("\\s+".toRegex()).toMutableList()
    val firstInitials = splitName[0][0]

    return when {
        splitName.size > 1 -> {
            val secondInitials = splitName[1][0]
            "$firstInitials$secondInitials".toUpperCase(Locale.ROOT)
        }
        else -> "$firstInitials".toUpperCase(Locale.ROOT)
    }
}

fun sharePost(postTitle: String?, postId: String): Intent {
    val postURL = Constants.FIGHTPANDEMICS_POST_URL.plus(postId)
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, R.string.share)
        putExtra(Intent.EXTRA_TITLE, postTitle)
        putExtra(Intent.EXTRA_TEXT, postURL)
    }
    return Intent.createChooser(sendIntent, "Share link to")
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
